package com.gibs.kadeesebi.presentation.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibs.kadeesebi.domain.model.Circle
import com.gibs.kadeesebi.domain.model.Person
import com.gibs.kadeesebi.domain.repository.CircleRepository
import com.gibs.kadeesebi.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class PersonRow(
    val person: Person,
    val circle: Circle?,
    val circleColor: Int?,
)

data class PersonListState(
    val rows: List<PersonRow> = emptyList(),
    val circles: List<Circle> = emptyList(),
)

@HiltViewModel
class PersonListViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    private val circleRepository: CircleRepository,
) : ViewModel() {

    val state = combine(
        personRepository.observePeople(),
        circleRepository.observeCircles(),
    ) { people, circles ->
        val byId = circles.associateBy { it.id }
        PersonListState(
            rows = people.map { p ->
                val c = p.circleId?.let { byId[it] }
                PersonRow(p, c, c?.colorArgb)
            },
            circles = circles,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PersonListState())

    fun addPerson(fullName: String, circleId: String?, relation: String? = null) {
        if (fullName.isBlank()) return
        viewModelScope.launch {
            personRepository.upsert(
                Person(
                    id = UUID.randomUUID().toString(),
                    fullName = fullName.trim(),
                    circleId = circleId,
                    relation = relation?.trim()?.ifBlank { null },
                ),
            )
        }
    }
}
