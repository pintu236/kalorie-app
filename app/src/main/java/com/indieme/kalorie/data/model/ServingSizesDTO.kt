package com.indieme.kalorie.data.model

import com.google.gson.annotations.SerializedName

data class ServingSizesDTO(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("servingSize") var servingSize: String? = null

)