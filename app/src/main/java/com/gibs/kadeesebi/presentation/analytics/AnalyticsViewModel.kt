package com.gibs.kadeesebi.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibs.kadeesebi.domain.model.EventType
import com.gibs.kadeesebi.domain.model.GiftDirection
import com.gibs.kadeesebi.domain.repository.EventTypeRepository
import com.gibs.kadeesebi.domain.repository.GiftRepository
import com.gibs.kadeesebi.domain.repository.ToiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

data class EventTypeSpend(
    val eventType: EventType?,
    val amount: Long,
)

data class AnalyticsState(
    val totalGivenThisYear: Long = 0,
    val totalReceivedThisYear: Long = 0,
    val byType: List<EventTypeSpend> = emptyList(),
    val toiCount: Int = 0,
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    giftRepository: GiftRepository,
    toiRepository: ToiRepository,
    eventTypeRepository: EventTypeRepository,
) : ViewModel() {

    val state = combine(
        giftRepository.observeGifts(),
        toiRepository.observeTois(),
        eventTypeRepository.observeEventTypes(),
    ) { gifts, tois, types ->
        val yearStart = startOfYear()
        val toiById = tois.associateBy { it.id }
        val typeById = types.associateBy { it.id }
        val thisYearGifts = gifts.filter { it.date >= yearStart }

        val given = thisYearGifts.filter { it.direction == GiftDirection.GIVEN }.sumOf { it.amount }
        val received = thisYearGifts.filter { it.direction == GiftDirection.RECEIVED }.sumOf { it.amount }

        val byType = thisYearGifts
            .filter { it.direction == GiftDirection.GIVEN }
            .groupBy { toiById[it.toiId]?.eventTypeId }
            .map { (typeId, list) ->
                EventTypeSpend(typeId?.let { typeById[it] }, list.sumOf { it.amount })
            }
            .sortedByDescending { it.amount }

        AnalyticsState(
            totalGivenThisYear = given,
            totalReceivedThisYear = received,
            byType = byType,
            toiCount = tois.count { it.date >= yearStart },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AnalyticsState())

    private fun startOfYear(): Long {
        val c = Calendar.getInstance()
        c.set(Calendar.MONTH, Calendar.JANUARY)
        c.set(Calendar.DAY_OF_MONTH, 1)
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.timeInMillis
    }
}
