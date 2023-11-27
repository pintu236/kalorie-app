package com.indieme.kalorie.data.model

sealed class NetworkResponse<out T> {

    object Loading : NetworkResponse<Nothing>()
    data class Idle<out T>(val data: T?) : NetworkResponse<T>()
    data class Success<out T>(val data: T?) : NetworkResponse<T>()
    data class Failure(val e: Throwable?) : NetworkResponse<Nothing>()
}
