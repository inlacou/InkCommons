package com.inlacou.inkkotlincommons.svg

class ACommand(
    command: SvgPathToken.Command,
    val xRadius: Double,
    val yRadius: Double,
    val rotation: Double,
    val largeArcFlag: Boolean,
    val sweepFlag: Boolean,
    vararg coords: SvgPathToken.Coordinate
): FullCommand(command, *coords) {

    init {
        require(command.letterEnum == SvgPathToken.Command.Letter.A)
    }

    override fun toString(): String =
        "$command $xRadius $yRadius $rotation " +
                "${if(largeArcFlag) "1" else "0"} ${if(sweepFlag) "1" else "0"} " +
                if(coords.isNotEmpty()) coords
                    .map { it.toString() }
                    .reduce { acc, coordinate -> "$acc $coordinate" }
                else ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ACommand) return false
        if (!super.equals(other)) return false

        if (xRadius != other.xRadius) return false
        if (yRadius != other.yRadius) return false
        if (rotation != other.rotation) return false
        if (largeArcFlag != other.largeArcFlag) return false
        if (sweepFlag != other.sweepFlag) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + xRadius.hashCode()
        result = 31 * result + yRadius.hashCode()
        result = 31 * result + rotation.hashCode()
        result = 31 * result + largeArcFlag.hashCode()
        result = 31 * result + sweepFlag.hashCode()
        return result
    }

    override fun getDrawPointCoords(
        lastCommand: FullCommand?,
        cursorCoord: SvgPathToken.Coordinate?,
        lastControlPoint: SvgPathToken.Coordinate?
    ): List<SvgPathToken.Coordinate> {
        return SvgPathManipulationMathUtils.sampleArcPoints(
            p0 = Point(cursorCoord!!.x!!, cursorCoord.y!!),
            rx = xRadius,
            ry = yRadius,
            xAxisRotation = rotation,
            largeArcFlag = largeArcFlag,
            sweepFlag = sweepFlag,
            p1 = coords[0].let { Point(it.x!!, it.y!!) },
            samples = PathManipulator.arcSamplingPoints,
        ).map { SvgPathToken.Coordinate(it.x, it.y) }
    }

    override fun scale(scale: Double): FullCommand {
        return ACommand(
            SvgPathToken.Command(command.letter),
            xRadius*scale,
            yRadius*scale,
            rotation,
            largeArcFlag,
            sweepFlag,
            *coords.map { it.scale(scale) }.toTypedArray()
        ).also {
            println("scale:  $this")
            println("scaled: $it")
        }
    }

    override fun translate(dx: Double, dy: Double): FullCommand {
        return ACommand(
            SvgPathToken.Command(command.letter),
            xRadius,
            yRadius,
            rotation,
            largeArcFlag,
            sweepFlag,
            *coords.map { it.translate(dx, dy) }.toTypedArray()
        )
    }

    override fun toAbsolute(previousCoordinate: SvgPathToken.Coordinate?): FullCommand {
        return if(command.letter.isUpperCase()) this else ACommand(
            SvgPathToken.Command(command.letter),
            xRadius,
            yRadius,
            rotation,
            largeArcFlag,
            sweepFlag,
            *coords.map { previousCoordinate!!+it }.toTypedArray()
        )
    }
}
