package com.pangea.raas.data.models

import com.google.gson.annotations.Expose

internal data class TokenRequest(
    @Expose
    var encryptedCardNumber: String = "",
    @Expose
    var encryptedCvv: String = "",
    @Expose
    var partnerIdentifier: String = "",
    @Expose
    var requestId: String = ""
)



