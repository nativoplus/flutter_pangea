package com.pangea.raas.data.models

import com.google.gson.annotations.Expose


data class TokenResponse(
    @Expose
    val token: String
)