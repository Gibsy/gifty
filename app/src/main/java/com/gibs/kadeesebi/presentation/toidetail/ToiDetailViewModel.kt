package com.gibs.kadeesebi.presentation.toidetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibs.kadeesebi.domain.model.EventType
import com.gibs.kadeesebi.domain.model.Gift
import com.gibs.kadeesebi.domain.model.GiftDirection
import com.gibs.kadeesebi.domain.model.Person
import com.gibs.kadeesebi.domain.model.Toi
import com.gibs.kadeesebi.domain.repository.EventTypeRepository
import com.gibs.kadeesebi.domain.repository.GiftRepository
import com.gibs.kadeesebi.domain.repository.PersonRepository
import com.gibs.kadeesebi.domain.repository.ToiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class GiftRow(val gift: Gift, val personName: String)

data class ToiDetailState(
    val toi: Toi? = null,
    val eventType: EventType? = null,
    val hostName: String? = null,
    val gifts: List<GiftRow> = emptyList(),
    val people: List<Person> = emptyList(),
    val totalReceived: Long = 0,
    val totalGiven: Long = 0,
)

@HiltViewModel
class ToiDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val giftRepository: GiftRepository,
    toiRepository: ToiRepository,
    personRepository: PersonRepository,
    eventTypeRepository: EventTypeRepository,
) : ViewModel() {

    private val toiId: String = checkNotNull(savedStateHandle["toiId"])

    val state = combine(
        toiRepository.observeToi(toiId),
        giftRepository.observeGiftsForToi(toiId),
        personRepository.observePeople(),
        eventTypeRepository.observeEventTypes(),
    ) { toi, gifts, people, types ->
        val byId = people.associateBy { it.id }
        val eventType = toi?.let { t -> types.firstOrNull { it.id == t.eventTypeId } }
        ToiDetailState(
            toi = toi,
            eventType = eventType,
            hostName = toi?.hostPersonId?.let { byId[it]?.fullName },
            gifts = gifts.map { GiftRow(it, byId[it.personId]?.fullName ?: "\u2014") },
            people = people,
            totalReceived = gifts.filter { it.direction == GiftDirection.RECEIVED }.sumOf { it.amount },
            totalGiven = gifts.filter { it.direction == GiftDirection.GIVEN }.sumOf { it.amount },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ToiDetailState())

    fun addGift(personId: String, direction: GiftDirection, amountTenge: Long, photoUri: String?) {
        viewModelScope.launch {
            giftRepository.upsert(
                Gift(
                    id = UUID.randomUUID().toString(),
                    toiId = toiId,
                    personId = personId,
                    direction = direction,
                    amount = amountTenge * 100,
                    date = System.currentTimeMillis(),
                    photoUri = photoUri,
                ),
            )
        }
    }

    fun deleteGift(id: String) {
        viewModelScope.launch { giftRepository.delete(id) }
    }
}
