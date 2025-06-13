package com.inlacou.inkkotlincommons.svg

import com.inlacou.inkkotlincommons.svg.SvgPathManipulationMathUtils.calculateReflectionPoint
import com.inlacou.inkkotlincommons.svg.SvgPathToken.Command.Letter.*

object PathManipulator {

    var factor = 100000000.0
    var bezierSamplingPoints = 50
    var arcSamplingPoints = 200

    fun tokenize(path: String): List<FullCommand> {
        val commandPattern = Regex("([MmLlHhVvCcSsQqTtAaZz])|([-+]?[0-9]*\\.?[0-9]+)")
        val rawTokens = commandPattern.findAll(path).map { it.value }.toList()

        val cleanTokens = mutableListOf<FullCommand>()

        var index = 0
        var current: FullCommand? = null
        while(index < rawTokens.size) {
            val token = rawTokens[index]
            if(token.toDoubleOrNull() == null) {
                if(current?.isEmpty() == true && current.command.letterEnum != Z)
                    throw IllegalArgumentException("empty fullCommand: $current")
                else {
                    if(current != null) cleanTokens.add(current)
                    current = token.first().let {
                        if(it.equals('A', ignoreCase = true))
                            ACommand(
                                command = SvgPathToken.Command(it),
                                xRadius = rawTokens[index + 1].toDouble(),
                                yRadius = rawTokens[index + 2].toDouble(),
                                rotation = rawTokens[index + 3].toDouble(),
                                largeArcFlag = rawTokens[index + 4].toInt() == 1,
                                sweepFlag = rawTokens[index + 5].toInt() == 1,
                                SvgPathToken.Coordinate(
                                    rawTokens[index + 6].toDouble(),
                                    rawTokens[index + 7].toDouble()
                                )
                            ).also { index += 8 }
                        else FullCommand(SvgPathToken.Command(it)).also { index += 1 }
                    }
                }
            } else {
                val coord = when(current?.command?.letterEnum) {
                    null -> throw NullPointerException()
                    Z -> throw IllegalArgumentException()
                    M, C, S,
                    Q, T, L -> SvgPathToken.Coordinate(
                        token.toDouble(),
                        rawTokens[index + 1].toDouble()
                    )
                        .also { index += 2 }
                    H -> SvgPathToken.Coordinate(
                        token.toDouble(),
                        null
                    )
                        .also { index += 1 }
                    V -> SvgPathToken.Coordinate(
                        null,
                        token.toDouble()
                    )
                        .also { index += 1 }
                    A -> throw IllegalStateException("Should be handled by ACommand child class")
                }
                current = current.add(coord)
            }
        }
        cleanTokens.add(current!!)

        return cleanTokens
    }

    internal fun getDrawnCoordinates(path: String): List<SvgPathToken.Coordinate> =
        toAbsolute(tokenize(path)).getDrawnCoordinates()

    private fun List<FullCommand>.getDrawnCoordinates(): List<SvgPathToken.Coordinate> {
        var cursor: SvgPathToken.Coordinate? = null
        var lastCommand: FullCommand? = null
        var lastControlPoint: SvgPathToken.Coordinate? = null
        return this.flatMapIndexed { index, fullCommand ->
            if(fullCommand.command.letter.isLowerCase() && fullCommand.command.letterEnum != Z)
                throw IllegalArgumentException("Use this method only with Uppercase commands (L, C, etc. instead of l, c, etc.)")
            fullCommand.getDrawPointCoords(lastCommand, cursor, lastControlPoint)
                .also {
                    // Save cursor and lastControlPoint for the next
                    cursor = it.lastOrNull()
                    lastCommand = fullCommand
                    lastControlPoint = when(fullCommand.command.letterEnum) {
                        A, M, L, H, V, Z -> null
                        C -> fullCommand.coords[1]
                        S -> fullCommand.coords[0]
                        Q -> fullCommand.coords[0]
                        T -> lastControlPoint?.calculateReflectionPoint(fullCommand.coords[0])
                        null -> fullCommand.coords.last()
                    }
                }
        }
    }

    fun centerPath(path: String): String {
        val tokens = toAbsolute(tokenize(path))

        if (tokens.isEmpty()) return path // nothing to do

        val drawnCoords = tokens.getDrawnCoordinates()

        val xs = drawnCoords.mapNotNull { it.x }
        val ys = drawnCoords.mapNotNull { it.y }

        val minX = xs.minOrNull()!!
        val maxX = xs.maxOrNull()!!
        val minY = ys.minOrNull()!!
        val maxY = ys.maxOrNull()!!

        val width = maxX - minX
        val height = maxY - minY

        val dx = (1.0 - width) / 2 - minX
        val dy = (1.0 - height) / 2 - minY

        val translatedCoords = tokens.map { it.translate(dx, dy) }

        return translatedCoords.toPathString()
    }

    fun joinPaths(list: List<String>) =
        if(list.isNotEmpty()) list.reduce { acc, s -> "$acc $s" }
        else ""

    fun splitPaths(path: String) = path.split('Z', 'z')

    fun scalePath(path: String): String {
        val tokens = toAbsolute(tokenize(path))

        if (tokens.isEmpty()) return path // nothing to do

        val drawnCoords = tokens.getDrawnCoordinates()

        val xs = drawnCoords.mapNotNull { it.x }
        val ys = drawnCoords.mapNotNull { it.y }

        val minX = xs.minOrNull()!!
        val maxX = xs.maxOrNull()!!
        val minY = ys.minOrNull()!!
        val maxY = ys.maxOrNull()!!

        val scale = 1.0 / (maxOf(maxX-minX, maxY-minY))

        val scaledCoordsTokens = tokens.map { it.scale(scale) }

        return scaledCoordsTokens.toPathString()
    }

    fun toAbsolute(path: String): List<FullCommand> =
        toAbsolute(tokenize(path))

    internal fun toAbsolute(commands: List<FullCommand>): List<FullCommand> {
        val result = mutableListOf<FullCommand>()
        var cursor: SvgPathToken.Coordinate? = null
        commands.onEachIndexed { index, fullCommand ->
            val newCommand = fullCommand.toAbsolute(cursor)
            result.add(newCommand)
            cursor = cursor.let { cursor ->
                if(cursor == null) newCommand.coords.last()
                else when(newCommand.command.letterEnum) {
                    Z -> null
                    H -> SvgPathToken.Coordinate(
                        if (newCommand.command.letter.isLowerCase()) cursor.x!! + newCommand.coords[0].x!!
                        else newCommand.coords[0].x,
                        cursor.y
                    )
                    V -> SvgPathToken.Coordinate(
                        cursor.x,
                        if (newCommand.command.letter.isLowerCase()) cursor.y!! + newCommand.coords[0].y!!
                        else newCommand.coords[0].y
                    )
                    else -> newCommand.coords.last()
                }
            }
        }
        return result
    }

    fun List<FullCommand>.toPathString(): String {
        return map { it.toString() }.reduce { acc, s -> "$acc$s " }
    }
}

