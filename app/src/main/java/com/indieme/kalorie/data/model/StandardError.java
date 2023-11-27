package com.indieme.kalorie.data.model;

import com.google.gson.annotations.SerializedName;

public class StandardError {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}
