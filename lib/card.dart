class CardInformation {
  final String publicKey;
  final String partnerIdentifier;
  final String cardNumber;
  final String cvv;

  const CardInformation(
      this.publicKey, this.partnerIdentifier, this.cardNumber, this.cvv);

  Map<String, String> toMap() {
    return {
      "publicKey": publicKey,
      "partnerIdentifier": partnerIdentifier,
      "cardNumber": cardNumber,
      "cvv": cvv
    };
  }
}
