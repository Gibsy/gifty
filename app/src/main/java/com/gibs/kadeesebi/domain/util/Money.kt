package com.gibs.kadeesebi.domain.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gibs.kadeesebi.domain.model.Currency
import java.text.NumberFormat

object Money {
    const val MINOR_PER_UNIT = 100L

    var currency by mutableStateOf(Currency.KZT)

    fun unitToMinor(unit: Long): Long = unit * MINOR_PER_UNIT
    fun minorToUnit(minor: Long): Long = minor / MINOR_PER_UNIT

    fun format(minor: Long): String {
        val unit = minor / MINOR_PER_UNIT
        val nf = NumberFormat.getNumberInstance(currency.locale())
        val number = nf.format(unit)
        return if (currency.symbolFirst) "${currency.symbol}$number" else "$number ${currency.symbol}"
    }

    fun masked(): String = "\u2022\u2022\u2022\u2022\u2022 ${currency.symbol}"
}
