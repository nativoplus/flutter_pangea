package com.pangea.raas.remote



import com.pangea.raas.data.models.TokenRequest
import com.pangea.raas.data.models.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


internal interface TokenApi {
    @Headers("Accept: application/json")
    @POST("tokenization/card")
    fun postTemporaryToken(@Body tokenRequest: TokenRequest):Call<TokenResponse>
}