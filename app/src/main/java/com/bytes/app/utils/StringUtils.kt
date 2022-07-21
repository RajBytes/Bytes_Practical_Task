package com.bytes.app.utils

import java.text.DecimalFormat

/**
 * This method is used to display the currency in specific format.
 */
fun Double.toCurrencyAmount(): String {
    val dec = DecimalFormat("##,##,##0.##")
    return "$ ${dec.format(this)}"
}
