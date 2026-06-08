package com.gibs.kadeesebi.domain.usecase

import com.gibs.kadeesebi.domain.model.Gift
import com.gibs.kadeesebi.domain.model.GiftDirection
import javax.inject.Inject

class SuggestGiftAmount @Inject constructor() {

    operator fun invoke(
        gifts: List<Gift>,
        @Suppress("UNUSED_PARAMETER") todayMillis: Long,
    ): Long {
        val received = gifts
            .filter { it.direction == GiftDirection.RECEIVED }
            .sumOf { it.amount }
        return roundToNice(received)
    }

    private fun roundToNice(amountMinor: Long): Long {
        val step = 100_000L
        if (amountMinor <= 0) return 0
        val rem = amountMinor % step
        return if (rem == 0L) amountMinor else amountMinor + (step - rem)
    }
}
