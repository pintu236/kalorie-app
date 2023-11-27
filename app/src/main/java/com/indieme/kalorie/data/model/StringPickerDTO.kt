package com.indieme.kalorie.data.model

data class StringPickerDTO(val text: String) : INumberPicker {
    override fun getNumber(): String {
        return text
    }
}