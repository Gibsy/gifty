package com.gibs.kadeesebi.domain.usecase

import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

/**
 * Пересчитывает прошлые суммы в "сегодняшние" тенге с учётом инфляции.
 *
 * Для MVP используется усреднённая годовая инфляция тенге. Позже значение
 * можно заменить на реальные данные (например, индекс Нацбанка РК),
 * подгружаемые из сети и кэшируемые локально.
 */
@Singleton
class InflationAdjuster @Inject constructor() {

    /** Усреднённая годовая инфляция (доля). 0.10 = 10% в год. */
    var annualRate: Double = DEFAULT_ANNUAL_RATE

    /**
     * @param amount сумма в тиынах на момент [pastMillis]
     * @return эквивалент в тиынах на момент [todayMillis]
     */
    fun adjust(amount: Long, pastMillis: Long, todayMillis: Long): Long {
        if (todayMillis <= pastMillis) return amount
        val years = yearsBetween(pastMillis, todayMillis)
        val factor = (1.0 + annualRate).pow(years)
        return (amount * factor).toLong()
    }

    private fun yearsBetween(startMillis: Long, endMillis: Long): Double {
        val start = Calendar.getInstance().apply { timeInMillis = startMillis }
        val end = Calendar.getInstance().apply { timeInMillis = endMillis }
        val millisPerYear = 365.2425 * 24 * 60 * 60 * 1000
        return (end.timeInMillis - start.timeInMillis) / millisPerYear
    }

    companion object {
        const val DEFAULT_ANNUAL_RATE = 0.10
    }
}
