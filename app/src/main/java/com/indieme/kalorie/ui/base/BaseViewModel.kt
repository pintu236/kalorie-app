package com.indieme.kalorie.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.indieme.kalorie.data.model.StandardError
import com.indieme.kalorie.data.network.RestClient
import com.indieme.kalorie.utils.CustomLogger
import com.indieme.kalorie.utils.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var noInternetConnection: MutableLiveData<Boolean>

    lateinit var showLoader: SingleLiveEvent<Boolean>

    lateinit var message: MutableStateFlow<String>

    lateinit var unAuthorized: MutableLiveData<Boolean>


    init {
        init()
    }

    private fun init() {
        noInternetConnection = MutableLiveData()
        showLoader = SingleLiveEvent()
        message = MutableStateFlow("")
        unAuthorized = MutableLiveData()
    }

    override fun onCleared() {
        super.onCleared()
        CustomLogger.log("View Model Cleared")
    }

    fun parseError(e: Throwable?): String? {
        return if (e is HttpException) {
            try {
                val responseBodyObjectConverter =
                    RestClient.retrofit.responseBodyConverter<StandardError>(
                        StandardError::class.java, arrayOfNulls(0)
                    )
                val error = responseBodyObjectConverter.convert(
                    e.response()!!.errorBody()
                )
                error!!.message
            } catch (exception: Exception) {
                exception.message
            }
        } else if (e is SocketTimeoutException) {
            "Connect to your network and try again"
        } else if (e is IOException) {
            if (e.message == null) "Some error occurred" + e.getLocalizedMessage() else e.message
        } else {
            if (e?.message == null) "Some error occurred" + e?.localizedMessage else e.message
        }
    }


}