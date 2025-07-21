package com.inlacou.inkkotlinextensions

fun String.hexToDecimal() = Integer.parseInt(this, 16)

fun String.hexColorToGrayScale(algorithm: GrayScaleAlgorithm = GrayScaleAlgorithm.AVERAGE): String {
    // Color String without the starting #
    var code = this.replace("#", "")
    var alpha: String? = null
    // Make it XX XX XX, like 0055AA
    when(code.length){
        3 -> { code = "${code[0]}${code[0]}${code[1]}${code[1]}${code[2]}${code[2]}" }
        4 -> { alpha = "${code[0]}"; code = "${code[1]}${code[1]}${code[2]}${code[2]}${code[3]}${code[3]}" }
        6 -> { /*Do nothing*/ }
        7 -> { alpha = "${code[0]}"; code = "${code[1]}${code[2]}${code[3]}${code[4]}${code[5]}${code[6]}"}
        8 -> { alpha = "${code[0]}${code[1]}"; code = "${code[2]}${code[3]}${code[4]}${code[5]}${code[6]}${code[7]}"}
        else -> return code
    }
    val r = "${code[0]}${code[1]}".hexToDecimal()
    val g = "${code[2]}${code[3]}".hexToDecimal()
    val b = "${code[4]}${code[5]}".hexToDecimal()

    val grayedColor = when(algorithm){
        GrayScaleAlgorithm.LIGHTNESS -> ((maxOf(r, g, b)+minOf(r, g, b)) / 2).decimalToHex()
        GrayScaleAlgorithm.AVERAGE -> ((r + g + b)/3).decimalToHex()
        GrayScaleAlgorithm.LUMINOSITY -> ((0.21*r + 0.72*g + 0.07*b)/3).toInt().decimalToHex()
    }

    return (alpha ?: "") + grayedColor + grayedColor + grayedColor
}
