package com.gibs.kadeesebi.presentation.person

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibs.kadeesebi.domain.model.Balance
import com.gibs.kadeesebi.domain.model.EventType
import com.gibs.kadeesebi.domain.model.Gift
import com.gibs.kadeesebi.domain.model.GiftDirection
import com.gibs.kadeesebi.domain.model.Person
import com.gibs.kadeesebi.domain.model.Toi
import com.gibs.kadeesebi.domain.repository.EventTypeRepository
import com.gibs.kadeesebi.domain.repository.GiftRepository
import com.gibs.kadeesebi.domain.repository.PersonRepository
import com.gibs.kadeesebi.domain.repository.ToiRepository
import com.gibs.kadeesebi.domain.usecase.CalculateReciprocityBalance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class PersonDetailState(
    val person: Person? = null,
    val gifts: List<Gift> = emptyList(),
    val balance: Balance? = null,
)

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val personRepository: PersonRepository,
    private val giftRepository: GiftRepository,
    private val toiRepository: ToiRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val calculateBalance: CalculateReciprocityBalance,
) : ViewModel() {

    private val personId: String = checkNotNull(savedStateHandle["personId"])

    val state = combine(
        personRepository.observePerson(personId),
        giftRepository.observeGiftsForPerson(personId),
    ) { person, gifts ->
        val balance = person?.let {
            calculateBalance(it, gifts, System.currentTimeMillis())
        }
        PersonDetailState(person, gifts, balance)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PersonDetailState())

    val tois = toiRepository.observeTois()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList<Toi>())

    val eventTypes = eventTypeRepository.observeEventTypes()
        .map { list -> list.associateBy { it.id } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap<String, EventType>())

    fun deleteGift(id: String) {
        viewModelScope.launch { giftRepository.delete(id) }
    }

    fun deletePerson(onDeleted: () -> Unit) {
        viewModelScope.launch {
            giftRepository.observeGiftsForPerson(personId).first().forEach {
                giftRepository.delete(it.id)
            }
            personRepository.delete(personId)
            onDeleted()
        }
    }

    fun addGift(toiId: String, direction: GiftDirection, amountTenge: Long, photoUri: String? = null) {
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
}
