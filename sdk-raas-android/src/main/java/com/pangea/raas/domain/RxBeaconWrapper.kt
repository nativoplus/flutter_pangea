package com.pangea.raas.domain

import android.content.Context
import android.util.Log
import com.riskified.android_sdk.RiskifiedBeaconMain
import com.riskified.android_sdk.RiskifiedBeaconMainInterface


internal class RxBeaconWrapper(
    context: Context,
    sessionId: String,
    debugInfo: Boolean,
    ) : RxBeaconOperations {
    companion object {
        private const val SHOP_NAME = "gopangea.com"
        private const val TAG = "RxBeaconWrapper"
    }

    private val rxBeacon: RiskifiedBeaconMainInterface
    
    init {
        rxBeacon = RiskifiedBeaconMain()
        rxBeacon.startBeacon(SHOP_NAME,sessionId,debugInfo,context)
    }

    override fun updateSessionToken(sessionId: String) {
        rxBeacon.updateSessionToken(sessionId)
    }

    override fun logRequest(url: String) {
        rxBeacon.logRequest(url)
    }

    override fun logSensitiveDeviceInfo() {
        rxBeacon.logSensitiveDeviceInfo()
    }

    //version 1.3.0 of riskified doesn't have this method only 1.3.1 and above
    //override fun rCookie(): String = rxBeacon.rCookie()
    //override fun rCookie(): String = ""

    override fun removeLocationUpdates() {
        rxBeacon.removeLocationUpdates()
    }


}
