package com.indieme.kalorie.data.model

sealed class CommonUiState<T> {
    object Empty : CommonUiState<Nothing>()
    object Loading : CommonUiState<Nothing>()
    data class Error(val exception: String?) : CommonUiState<Nothing>()
    data class Success<T>(val data: T) : CommonUiState<T>()
}
