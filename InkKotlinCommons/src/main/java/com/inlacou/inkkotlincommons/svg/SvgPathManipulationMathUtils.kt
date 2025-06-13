package com.inlacou.inkkotlincommons.svg

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

internal object SvgPathManipulationMathUtils {

    fun calculateQuadraticBezierPoint(
        startPoint: SvgPathToken.Coordinate,
        controlPoint: SvgPathToken.Coordinate,
        endPoint: SvgPathToken.Coordinate
    ): List<SvgPathToken.Coordinate> =
        calculateQuadraticBezierPoint(
            Point(startPoint.x!!, startPoint.y!!),
            Point(controlPoint.x!!, controlPoint.y!!),
            Point(endPoint.x!!, endPoint.y!!)
        ).map { SvgPathToken.Coordinate(it.x, it.y) }

        /**
     * [Source](https://blog.maximeheckel.com/posts/cubic-bezier-from-math-to-motion/)
     */
    fun calculateQuadraticBezierPoint(P0: Point, P1: Point, P2: Point): List<Point> {
        val x0 = P0.x
        val y0 = P0.y

        val x1 = P1.x
        val y1 = P1.y

        val x2 = P2.x
        val y2 = P2.y


        fun x(it: Double) =
            (1 - it).pow(2) * x0 +
                    2 * (1 - it) * it * x1 +
                    it.pow(2) * x2

        fun y(it: Double) =
            (1 - it).pow(2) * y0 +
                    2 * (1 - it) * it * y1 +
                    it.pow(2) * y2

        val result = mutableListOf<Point>()

        var t = 0.0
        while (t <= 1.0) {
            val valX = x(t)
            val valY = y(t)
            result.add(Point(valX, valY))
            t += 1.0 / PathManipulator.bezierSamplingPoints
        }

        return result
    }

    fun calculateCubicBezierPoint(
        startPoint: SvgPathToken.Coordinate,
        controlPoint1: SvgPathToken.Coordinate,
        controlPoint2: SvgPathToken.Coordinate,
        endPoint: SvgPathToken.Coordinate
    ): List<SvgPathToken.Coordinate> =
        calculateCubicBezierPoint(
            Point(startPoint.x!!, startPoint.y!!),
            Point(controlPoint1.x!!, controlPoint1.y!!),
            Point(controlPoint2.x!!, controlPoint2.y!!),
            Point(endPoint.x!!, endPoint.y!!)
        ).map { SvgPathToken.Coordinate(it.x, it.y) }

    /**
     * [Source](https://blog.maximeheckel.com/posts/cubic-bezier-from-math-to-motion/)
     */
    fun calculateCubicBezierPoint(P0: Point, P1: Point, P2: Point, P3: Point): List<Point> {
        val x0 = P0.x
        val y0 = P0.y

        val x1 = P1.x
        val y1 = P1.y

        val x2 = P2.x
        val y2 = P2.y

        val x3 = P3.x
        val y3 = P3.y

        fun calculate(it: Double, p0: Double, p1: Double, p2: Double, p3: Double) =
            (1 - it).pow(3) * p0 +
                    3 * (1 - it).pow(2) * it * p1 +
                    3 * (1 - it) * it.pow(2) * p2 +
                    it.pow(3) * p3

        val result = mutableListOf<Point>()

        var t = 0.0
        while (t <= 1.0) {
            val valX = calculate(t, x0, x1, x2, x3)
            val valY = calculate(t, y0, y1, y2, y3)
            result.add(Point(valX, valY))
            t += 1.0 / PathManipulator.bezierSamplingPoints
        }

        return result
    }

    fun degreesToRadians(degrees: Double): Double {
        return  degrees * PI / 180
    }

    fun radiansToDegrees(radians: Double): Double {
        return radians * 180 / PI
    }

    fun sampleArcPoints(
        p0: Point,
        rx: Double,
        ry: Double,
        xAxisRotation: Double,
        largeArcFlag: Boolean,
        sweepFlag: Boolean,
        p1: Point,
        samples: Int = 200
    ): List<Point> {
        // Convert rotation to radians
        val phi = degreesToRadians(xAxisRotation % 360)

        // Step 1: Compute transformed start and end point
        val dx = (p0.x - p1.x) / 2.0
        val dy = (p0.y - p1.y) / 2.0

        val cosPhi = cos(phi)
        val sinPhi = sin(phi)

        val x1Prime = cosPhi * dx + sinPhi * dy
        val y1Prime = -sinPhi * dx + cosPhi * dy

        var adjRx = abs(rx)
        var adjRy = abs(ry)

        // Step 2: Correct radii if too small
        val lambda = (x1Prime * x1Prime) / (adjRx * adjRx) + (y1Prime * y1Prime) / (adjRy * adjRy)
        if (lambda > 1) {
            val scale = sqrt(lambda)
            adjRx *= scale
            adjRy *= scale
        }

        // Step 3: Compute center cx', cy'
        val sign = if (largeArcFlag != sweepFlag) 1.0 else -1.0
        val rx2 = adjRx * adjRx
        val ry2 = adjRy * adjRy
        val x1p2 = x1Prime * x1Prime
        val y1p2 = y1Prime * y1Prime

        val numerator = rx2 * ry2 - rx2 * y1p2 - ry2 * x1p2
        val denom = rx2 * y1p2 + ry2 * x1p2
        val c = sqrt(max(0.0, numerator / denom)) * sign

        val cxPrime = c * (adjRx * y1Prime / adjRy)
        val cyPrime = c * -(adjRy * x1Prime / adjRx)

        // Step 4: Compute center in global coordinates
        val cx = cosPhi * cxPrime - sinPhi * cyPrime + (p0.x + p1.x) / 2.0
        val cy = sinPhi * cxPrime + cosPhi * cyPrime + (p0.y + p1.y) / 2.0

        // Step 5: Compute angles
        fun angle(u: Point, v: Point): Double {
            val dot = u.x * v.x + u.y * v.y
            val len = hypot(u.x, u.y) * hypot(v.x, v.y)
            val acos = acos((dot / len).coerceIn(-1.0, 1.0))
            val cross = u.x * v.y - u.y * v.x
            return if (cross >= 0) acos else -acos
        }

        val startVec = Point((x1Prime - cxPrime) / adjRx, (y1Prime - cyPrime) / adjRy)
        val endVec = Point((-x1Prime - cxPrime) / adjRx, (-y1Prime - cyPrime) / adjRy)

        val theta1 = angle(Point(1.0, 0.0), startVec)
        var deltaTheta = angle(startVec, endVec)

        if (!sweepFlag && deltaTheta > 0) deltaTheta -= 2 * PI
        if (sweepFlag && deltaTheta < 0) deltaTheta += 2 * PI

        // Step 6: Sample points
        return (0..samples).map { i ->
            val t = i.toDouble() / samples
            val theta = theta1 + t * deltaTheta
            val x = adjRx * cos(theta)
            val y = adjRy * sin(theta)

            val globalX = cosPhi * x - sinPhi * y + cx
            val globalY = sinPhi * x + cosPhi * y + cy

            Point(globalX, globalY)
        }
    }

    fun Point.calculateReflectionPoint(anchor: Point): Point {
        val dx = abs(x-anchor.x)
        val dy = abs(y-anchor.y)

        return Point(
            if(x < anchor.x) x+dx*2
            else if(x > anchor.x) x-dx*2
            else x,
            if(y < anchor.y) y+dy*2
            else if(y > anchor.y) y-dy*2
            else y,
        )
    }

    fun SvgPathToken.Coordinate.calculateReflectionPoint(anchor: SvgPathToken.Coordinate): SvgPathToken.Coordinate =
        Point(this.x!!, this.y!!).calculateReflectionPoint(Point(anchor.x!!, anchor.y!!))
            .let { SvgPathToken.Coordinate(it.x, it.y) }
}
