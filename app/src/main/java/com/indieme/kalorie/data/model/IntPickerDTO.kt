package com.indieme.kalorie.data.model

data class IntPickerDTO(val number: Int) : INumberPicker {
    override fun getNumber(): String {
        return number.toString()
    }
}