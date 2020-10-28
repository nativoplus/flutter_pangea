package com.pangea.raas.data.models

data class CardInformation(
    var publicKey: String,
    var partnerIdentifier: String,
    var cardNumber: String,
    var cvv: String,
)