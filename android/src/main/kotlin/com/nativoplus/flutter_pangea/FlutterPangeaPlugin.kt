package com.nativoplus.flutter_pangea

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import com.pangea.raas.data.models.CardInformation
import com.pangea.raas.data.models.TokenResponse
import com.pangea.raas.domain.CallBack
import com.pangea.raas.domain.Pangea
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** FlutterPangeaPlugin */
class FlutterPangeaPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var applicationContext: Context? = null

    private lateinit var mPangea: Pangea

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        applicationContext = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_pangea")
        channel.setMethodCallHandler(this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "init") {
            val args = call.arguments as HashMap<String, Any>
            mPangea = Pangea.createSession(
                    applicationContext!!,
                    args["debugInfo"] as Boolean,
                    args["pangeaSessionId"] as String,
                    getEnvironment(args["environment"] as String))
            result.success("Success ${mPangea.getSessionId()}")
        } else if (call.method == "createToken") {
            val args = call.arguments as HashMap<String, String>
            mPangea.createToken(CardInformation(
                    args["publicKey"]!!,
                    args["partnerIdentifier"]!!,
                    args["cardNumber"]!!,
                    args["cvv"]!!), object : CallBack {
                override fun onFailure(tokenResponse: TokenResponse, throwable: Throwable?) {
                    Handler(Looper.getMainLooper()).post {
                        result.error("FAILED", "Token creation failed", tokenResponse.token)
                    }
                }

                override fun onResponse(tokenResponse: TokenResponse) {
                    Handler(Looper.getMainLooper()).post {
                        result.success("token response ${tokenResponse.token}")
                    }
                }
            })
        } else {
            result.notImplemented()
        }
    }

    private fun getEnvironment(environment: String?): Pangea.Companion.Environment {
        return when (environment) {
            "production" -> Pangea.Companion.Environment.PRODUCTION
            "integration" -> Pangea.Companion.Environment.INTEGRATION
            else -> Pangea.Companion.Environment.DEV
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        applicationContext = null
        channel.setMethodCallHandler(null)
    }
}
