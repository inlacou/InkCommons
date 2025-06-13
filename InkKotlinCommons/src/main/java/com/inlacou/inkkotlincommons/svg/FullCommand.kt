package com.inlacou.inkkotlincommons.svg

import com.inlacou.inkkotlincommons.svg.SvgPathManipulationMathUtils.calculateReflectionPoint

open class FullCommand(
    val command: SvgPathToken.Command,
    vararg val coords: SvgPathToken.Coordinate
) {
    open fun getDrawPointCoords(
        lastCommand: FullCommand?,
        cursorCoord: SvgPathToken.Coordinate?,
        lastControlPoint: SvgPathToken.Coordinate?,
    ): List<SvgPathToken.Coordinate> {
        return when(command.letterEnum) {
            SvgPathToken.Command.Letter.Z -> emptyList()
            SvgPathToken.Command.Letter.M -> coords.toList()
            SvgPathToken.Command.Letter.L -> coords.toList()
            SvgPathToken.Command.Letter.H -> listOf(
                SvgPathToken.Coordinate(
                    coords[0].x,
                    cursorCoord!!.y
                )
            )
            SvgPathToken.Command.Letter.V -> listOf(
                SvgPathToken.Coordinate(
                    cursorCoord!!.x,
                    coords[0].y
                )
            )
            SvgPathToken.Command.Letter.C -> {
                val cp1 = coords[0]
                val cp2 = coords[1]
                val point = coords[2]
                SvgPathManipulationMathUtils.calculateCubicBezierPoint(
                    startPoint = cursorCoord!!,
                    controlPoint1 = cp1,
                    controlPoint2 = cp2,
                    endPoint = point
                ) +point
            }
            SvgPathToken.Command.Letter.S -> {
                val controlPoint1 =
                    if(lastCommand!!.command.letterEnum == SvgPathToken.Command.Letter.S ||
                        lastCommand.command.letterEnum == SvgPathToken.Command.Letter.C
                    )
                        lastControlPoint!!.calculateReflectionPoint(cursorCoord!!)
                    else cursorCoord!!
                val controlPoint2 = coords[0]
                val endPoint = coords[1]
                SvgPathManipulationMathUtils.calculateCubicBezierPoint(
                    startPoint = cursorCoord,
                    controlPoint1 = controlPoint1,
                    controlPoint2 = controlPoint2,
                    endPoint = endPoint
                ) +endPoint
            }
            SvgPathToken.Command.Letter.Q -> {
                val cp = coords[0]
                val endPoint = coords[1]
                SvgPathManipulationMathUtils.calculateQuadraticBezierPoint(
                    startPoint = cursorCoord!!,
                    controlPoint = cp,
                    endPoint = endPoint
                ) +endPoint
            }
            SvgPathToken.Command.Letter.T -> {
                val cp = lastControlPoint!!.calculateReflectionPoint(cursorCoord!!)
                val endPoint = coords[0]
                SvgPathManipulationMathUtils.calculateQuadraticBezierPoint(
                    startPoint = cursorCoord,
                    controlPoint = cp,
                    endPoint = endPoint
                ) +endPoint
            }
            SvgPathToken.Command.Letter.A -> throw IllegalStateException("Should be handled by ACommand child class")
            null -> throw NullPointerException()
        }
    }

    open fun scale(scale: Double): FullCommand {
        return when(command.letterEnum) {
            SvgPathToken.Command.Letter.M,
            SvgPathToken.Command.Letter.L,
            SvgPathToken.Command.Letter.H,
            SvgPathToken.Command.Letter.V,
            SvgPathToken.Command.Letter.S,
            SvgPathToken.Command.Letter.Q,
            SvgPathToken.Command.Letter.T,
            SvgPathToken.Command.Letter.C -> {
                FullCommand(
                    SvgPathToken.Command(command.letterEnum),
                    *coords.map { it.scale(scale) }.toTypedArray()
                )
            }
            SvgPathToken.Command.Letter.Z -> this
            SvgPathToken.Command.Letter.A -> throw IllegalStateException("Should be handled by ACommand child class")
            null -> throw NullPointerException()
        }
    }

    open fun toAbsolute(cursorCoord: SvgPathToken.Coordinate?): FullCommand {
        return if(command.letter.isUpperCase()) this
        else when(command.letterEnum) {
            SvgPathToken.Command.Letter.M,
            SvgPathToken.Command.Letter.L,
            SvgPathToken.Command.Letter.H,
            SvgPathToken.Command.Letter.V,
            SvgPathToken.Command.Letter.S,
            SvgPathToken.Command.Letter.Q,
            SvgPathToken.Command.Letter.T,
            SvgPathToken.Command.Letter.C -> {
                try{
                    FullCommand(
                        SvgPathToken.Command(command.letterEnum),
                        *coords.map { it+cursorCoord!! }.toTypedArray()
                    )
                }catch (npe: NullPointerException) {
                    println("cursorCoord: $cursorCoord")
                    println("trying to absolute:\n\t$this (${this.coords.size})")
                    throw npe
                }
            }
            SvgPathToken.Command.Letter.Z -> this
            SvgPathToken.Command.Letter.A -> throw IllegalStateException("Should be handled by ACommand child class")
            null -> throw NullPointerException()
        }
    }

    open fun translate(dx: Double, dy: Double): FullCommand =
        FullCommand(
            SvgPathToken.Command(command.letter),
            *coords.map { it.translate(dx, dy) }.toTypedArray()
        )

    fun add(coord: SvgPathToken.Coordinate) = FullCommand(command, *coords.toMutableList().apply { add(coord) }.toTypedArray())
    fun isEmpty() = coords.isEmpty()

    override fun toString(): String {
        return "$command ${
            if(coords.isNotEmpty()) coords
                .map { it.toString() }
                .reduce { acc, coordinate -> "$acc $coordinate" }
            else ""
        }"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FullCommand) return false

        if (command != other.command) return false
        if (!coords.contentEquals(other.coords)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = command.hashCode()
        result = 31 * result + coords.contentHashCode()
        return result
    }

}
