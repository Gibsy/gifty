package com.gibs.kadeesebi.presentation.circles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibs.kadeesebi.domain.model.Circle
import com.gibs.kadeesebi.domain.repository.CircleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CirclesViewModel @Inject constructor(
    private val circleRepository: CircleRepository,
) : ViewModel() {

    val circles = circleRepository.observeCircles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList<Circle>())

    fun addCircle(name: String, colorArgb: Int) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            circleRepository.upsert(
                Circle(
                    id = UUID.randomUUID().toString(),
                    name = trimmed,
                    colorArgb = colorArgb,
                    builtInKey = null,
                ),
            )
        }
    }

    fun deleteCircle(id: String) {
        viewModelScope.launch { circleRepository.delete(id) }
    }
}
