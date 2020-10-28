package com.pangea.raas.domain

import com.pangea.raas.data.models.TokenResponse


interface CallBack{

    fun onResponse(tokenResponse: TokenResponse)

    fun onFailure(tokenResponse: TokenResponse, throwable: Throwable?)

}