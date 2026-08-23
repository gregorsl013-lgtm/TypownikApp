package com.typownik.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.typownik.app.data.model.LeagueRound
import com.typownik.app.data.repository.MatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val rounds: List<LeagueRound>) : UiState()
    data class Error(val message: String) : UiState()
}

class MatchViewModel(
    private val repository: MatchRepository = MatchRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadPredictions()
    }

    fun loadPredictions() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getNextRoundPredictions()
                .onSuccess { rounds -> _uiState.value = UiState.Success(rounds) }
                .onFailure { e -> _uiState.value = UiState.Error(e.message ?: "Błąd pobierania danych") }
        }
    }
}
