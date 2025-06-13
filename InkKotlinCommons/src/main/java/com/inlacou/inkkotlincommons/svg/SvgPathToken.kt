package com.inlacou.inkkotlincommons.svg

import kotlin.math.abs

sealed class SvgPathToken {
    class Command(val letter: Char): SvgPathToken() {
        constructor(l: Letter): this(l.name.first())
        val letterEnum = Letter.entries.find { it.name.equals(letter.toString(), ignoreCase = true) }
        /**
         * Command in...
         *
         *      - Uppercase: refers to absolute coordinates while
         *      - Lowercase: refers to relative distances
         *
         *  So these are equal:
         *
         *      - M 0.5,0 L 1.0,1.0 L  0.0,1.0 z
         *      - M 0.5,0 l 0.5,1.0 l -1.0,0.0 Z
         *
         *  [Source](https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorials/SVG_from_scratch/Paths)
         */
        enum class Letter {
            /**
             * close
             */
            Z,
            /**
             * move
             *
             * M coordinatePoint
             */
            M,
            /**
             * line
             *
             * L coordinatePoint coordinatePoint
             */
            L,
            /**
             * line(horizontal)
             *
             * H value
             */
            H,
            /**
             * line(vertical)
             *
             * V value
             */
            V,
            /**
             * curve(bezier)(cubic)
             *
             * C controlPoint1 controlPoint2 coordinatePoint
             *
             * > The cubic curve, C, is the slightly more complex curve. Cubic Béziers take in two control points for each point. Therefore, to create a cubic Bézier, three sets of coordinates need to be specified.
             *
             *
             */
            C,
            /**
             * curve(bezier)(cubic)(shorthand)
             *
             * Like C but the first control point is derived from previous step/current cursor
             *
             * Variants:
             * - S controlPoint2 coordinatePoint
             * - S x2 y2 x y
             * - s dx2 dy2 dx dy
             *
             * Notes:
             *  - if it follows another S command or a C command, the first control point is assumed to be a reflection of the one used previously
             *  - if the S command doesn't follow another S or C command, then the current position of the cursor is used as the first control point
             */
            S,
            /**
             * curve(bezier)(quadratic)
             *
             * Like C but only one control point is provided and it is used twice
             * - so `Q 0 0 1 1` is equal to `C 0 0 0 0 1 1`
             *
             * Variants:
             * - Q controlPoint coordinatePoint
             * - q controlPoint coordinatePoint
             */
            Q,
            /**
             * curve(bezier)(quadratic)(shorthand)
             *
             * Like Q but the control point is derived from previous step/current cursor by mirroring commands last control point
             *
             * Variants:
             * - T coordinatePoint
             * - t coordinatePoint
             */
            T,
            /**
             * elliptical arc
             *
             * Variants:
             * - A x-radius y-radius x-axis-rotation large-arc-flag sweep-flag x y
             * - a x-radius y-radius x-axis-rotation large-arc-flag sweep-flag dx dy
             *
             * Notes:
             * - rotation 0 to 360 degrees angle
             * - flag 0 or 1
             */
            A,
        }

        override fun toString(): String = letter.toString()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Command) return false

            if (letter != other.letter && (letter=='Z' || letter=='z').not()) return false
            if (letterEnum != other.letterEnum) return false

            return true
        }

        override fun hashCode(): Int {
            var result = letter.hashCode()
            result = 31 * result + (letterEnum?.hashCode() ?: 0)
            return result
        }
    }

    class Coordinate(val x: Double?, val y: Double?): SvgPathToken() {
        override fun toString(): String =
            if(x != null && y != null) "${x.toStringAvoidScientificNotation()} ${y.toStringAvoidScientificNotation()}"
            else x?.toStringAvoidScientificNotation() ?: y?.toStringAvoidScientificNotation() ?: ""

        operator fun plus(other: Coordinate): Coordinate {
            val factor = PathManipulator.factor
            return Coordinate(
                x = if(x!=null) ((x*factor).toInt()+(other.x!!*factor).toInt()).toDouble()/factor else null,
                y = if(y!=null) ((y*factor).toInt()+(other.y!!*factor).toInt()).toDouble()/factor else null,
            )
        }

        fun scale(scale: Double): Coordinate {
            return Coordinate(
                if(x!=null) x*scale else null,
                if(y!=null) y*scale else null,
            )
        }

        fun translate(dx: Double, dy: Double): Coordinate {
            return Coordinate(
                if(x!=null) { if(dx.isNaN()) x else x+dx }
                else null,
                if(y!=null) { if(dy.isNaN()) y else y+dy }
                else null,
            )
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Coordinate) return false

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x?.hashCode() ?: 0
            result = 31 * result + (y?.hashCode() ?: 0)
            return result
        }

    }
}

fun Double.toStringAvoidScientificNotation(): String {
    if(this == 0.0) return "0.0"
    if(this == this.toLong().toDouble()) return this.toLong().toString()

    return (this.toString().let {
        if(it.contains('E')) {
            val factor = it.substring(it.indexOf('E')+1).toInt()
            var result = it.substring(0, it.indexOf('E'))
            result = result.replace(".", "")
            val isNegative = result.startsWith('-')
            if(isNegative) result = result.drop(1)
            if(factor < 0) {
                repeat(abs(factor)-1) {
                    result = "0$result"
                }
                result = "0.$result"
            } else {
                repeat(abs(factor)-1) {
                    result = "${result}0"
                }
            }
            if(isNegative) {
                result = "-$result"
            }
            result
        } else it
    }).also {
        if(it.toDoubleOrNull() == null) {
            throw NumberFormatException("Failed to stringify without scientific notation." +
                    "\n$this" +
                    "\n$it"
            )
        }
    }
}
