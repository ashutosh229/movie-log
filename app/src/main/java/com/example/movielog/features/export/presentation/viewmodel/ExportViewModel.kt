package com.example.movielog.features.export.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielog.features.export.domain.exporter.LibraryExporter
import com.example.movielog.features.export.domain.model.ExportFormat
import com.example.movielog.features.export.presentation.state.ExportUiState
import com.example.movielog.features.library.domain.repository.LibraryRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExportViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportUiState())
    val uiState: StateFlow<ExportUiState> = _uiState.asStateFlow()

    fun export(format: ExportFormat) {
        if (_uiState.value.isExporting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true, error = null) }

            try {
                val content = repository.observeAllContent().first()

                if (content.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            isExporting = false,
                            error = "Your library is empty. Add some titles first."
                        )
                    }
                    return@launch
                }

                val result = LibraryExporter.export(content, format)

                _uiState.update {
                    it.copy(isExporting = false, pendingShare = result)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isExporting = false,
                        error = e.message ?: "Unable to export library."
                    )
                }
            }
        }
    }

    fun onShareHandled() {
        _uiState.update { it.copy(pendingShare = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}