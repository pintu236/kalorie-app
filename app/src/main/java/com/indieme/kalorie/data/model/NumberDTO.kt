package com.indieme.kalorie.data.model

data class NumberPickerDTO(val number: Double) : INumberPicker {
    override fun getNumber(): String {
        return String.format("%.2f", number)
    }
}