package com.pangea.raas.domain


internal interface RxBeaconOperations {
    //fun startBeacon()
    fun updateSessionToken(sessionId: String)
    fun logRequest(url:String)
    fun logSensitiveDeviceInfo()
    //fun rCookie():String
    fun removeLocationUpdates()
}