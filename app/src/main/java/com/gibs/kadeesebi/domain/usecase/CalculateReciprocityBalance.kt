package com.gibs.kadeesebi.domain.usecase

import com.gibs.kadeesebi.domain.model.Balance
import com.gibs.kadeesebi.domain.model.Gift
import com.gibs.kadeesebi.domain.model.GiftDirection
import com.gibs.kadeesebi.domain.model.Person
import javax.inject.Inject

class CalculateReciprocityBalance @Inject constructor(
    private val suggest: SuggestGiftAmount,
) {
    operator fun invoke(
        person: Person,
        gifts: List<Gift>,
        todayMillis: Long,
    ): Balance {
        val received = gifts
            .filter { it.direction == GiftDirection.RECEIVED }
            .sumOf { it.amount }

        val given = gifts
            .filter { it.direction == GiftDirection.GIVEN }
            .sumOf { it.amount }

        return Balance(
            personId = person.id,
            received = received,
            given = given,
            net = given - received,
            suggested = suggest(gifts, todayMillis),
        )
    }
}
