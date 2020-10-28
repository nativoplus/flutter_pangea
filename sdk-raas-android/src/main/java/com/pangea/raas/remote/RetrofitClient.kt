package com.pangea.raas.remote

import android.util.Log
import com.pangea.raas.domain.Pangea.Companion.Environment
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


internal object RetrofitClient {
    private const val TAG = "RetrofitClient"
    private var environmentMap: EnumMap<Environment,String> = EnumMap(Environment::class.java)
    private var debugInfo:Boolean = false
    private var retrofit: Retrofit? = null

    fun initRetrofitInstance(environment: Environment, debugInfo:Boolean):RetrofitClient {
        this.debugInfo = debugInfo
        val clientHttp = OkHttpClient.Builder()
        if (debugInfo){
            val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            clientHttp.addInterceptor(httpLoggingInterceptor)
        }
        retrofit = Retrofit.Builder()
            .baseUrl(environmentMap[environment] ?: "urlNotFound")
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientHttp.build())
            .build()
        return this
    }

    fun<T> buildService(service: Class<T>): T?{
        retrofit?.let {
            return it.create(service)
        }
        if (debugInfo){
            Log.w(TAG, "buildService: the retrofit client is null, use RetrofitClient.initRetrofitInstance(debugInfo:Boolean) to get it first", )
        }
        return null
    }

    init {
        environmentMap.clear()
        environmentMap[Environment.PRODUCTION] = "https://api.pangea-raas.com/raas/v1/"
        environmentMap[Environment.DEV] = "https://api.pangea-raas-dev.com/raas/v1"
        environmentMap[Environment.INTEGRATION] = "https://api.pangea-raas-integration.com/raas/v1/"
    }


}

