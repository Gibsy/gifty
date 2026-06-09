package com.gibs.kadeesebi.domain.usecase

import com.gibs.kadeesebi.domain.model.Currency
import com.gibs.kadeesebi.domain.model.Gift
import com.gibs.kadeesebi.domain.model.GiftDirection
import com.gibs.kadeesebi.domain.util.Money
import javax.inject.Inject

class SuggestGiftAmount @Inject constructor() {

    operator fun invoke(
        gifts: List<Gift>,
        todayMillis: Long,
    ): Long {
        val received = gifts
            .filter { it.direction == GiftDirection.RECEIVED }
            .sumOf { it.amount }
        return roundUpNice(received)
    }

    /**
     * Округляет сумму ВВЕРХ до ближайшего «красивого» значения.
     * Шаг зависит от валюты: доллары — мелкий шаг (5), рубли — средний (500),
     * тенге — крупный (1000). Например: $12 → $15, 867₽ → 1000₽, 2480₸ → 3000₸.
     */
    private fun roundUpNice(amountMinor: Long): Long {
        if (amountMinor <= 0) return 0
        val stepUnits = when (Money.currency) {
            Currency.USD -> 5L
            Currency.RUB -> 500L
            Currency.KZT -> 1_000L
        }
        val step = stepUnits * Money.MINOR_PER_UNIT
        return ((amountMinor + step - 1) / step) * step
    }
}
