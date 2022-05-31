package com.inlacou.inkkotlinextensions

fun String.hexToDecimal() = Integer.parseInt(this, 16)

fun String.hexColorToGrayScale(algorithm: GrayScaleAlgorithm = GrayScaleAlgorithm.AVERAGE): String {
    var aux = this.replace("#", "")
    var alpha: String? = null
    when(aux.length){
        3 -> { aux = "${aux[0]}${aux[0]}${aux[1]}${aux[1]}${aux[2]}${aux[2]}" }
        4 -> { alpha = "${aux[0]}"; aux = "${aux[1]}${aux[1]}${aux[2]}${aux[2]}${aux[3]}${aux[3]}" }
        6 -> { /*Do nothing*/ }
        7 -> { alpha = "${aux[0]}"; aux = "${aux[1]}${aux[2]}${aux[3]}${aux[4]}${aux[5]}${aux[6]}"}
        8 -> { alpha = "${aux[0]}${aux[1]}"; aux = "${aux[2]}${aux[3]}${aux[4]}${aux[5]}${aux[6]}${aux[7]}"}
        else -> return aux
    }
    val r = "${aux[0]}${aux[1]}".hexToDecimal()
    val g = "${aux[2]}${aux[3]}".hexToDecimal()
    val b = "${aux[4]}${aux[5]}".hexToDecimal()

    aux = when(algorithm){
        GrayScaleAlgorithm.LIGHTNESS -> ((maxOf(r, g, b)+minOf(r, g, b)) / 2).decimalToHex()
        GrayScaleAlgorithm.AVERAGE -> ((r + g + b)/3).decimalToHex()
        GrayScaleAlgorithm.LUMINOSITY -> ((0.21*r + 0.72*g + 0.07*b)/3).toInt().decimalToHex()
    }

    return "" + (alpha ?: "") + aux + aux + aux
}
