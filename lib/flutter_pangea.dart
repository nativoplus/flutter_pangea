import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_pangea/card.dart';
import 'package:flutter_pangea/exceptions.dart';
import 'package:flutter_pangea/status.dart';

class FlutterPangea {
  static const MethodChannel _channel = const MethodChannel('flutter_pangea');
  _Pangea _pangea;
  bool _initialized;

  get initialized => _initialized;

  FlutterPangea._create();

  static final FlutterPangea instance = FlutterPangea._create();

  Future<PangeaResponse<_Pangea>> init(
      bool debugInfo, String pangeaSessionId, Environment environment) async {
    var methodName = "init";
    try {
      if (debugInfo != null && pangeaSessionId != null && environment != null) {
        _pangea = _Pangea(
            false, pangeaSessionId, environment.toString().toLowerCase());
        String sessionId =
            await _channel.invokeMethod(methodName, _pangea.toMap());
        _pangea.sessionId = sessionId;
        _initialized = true;
        return PangeaResponse.success(_pangea);
      } else if (debugInfo == null)
        throw PangeaParamNullException(methodName, "debugInfo");
      else if (pangeaSessionId == null)
        throw PangeaParamNullException(methodName, "pangeaSessionId");
      else if (environment == null)
        throw PangeaParamNullException(methodName, "environment");
      else
        throw PangeaMalformedException();
    } catch (e) {
      throw e;
    }
  }

  Future<PangeaResponse<String>> createToken(CardInformation card) async {
    var methodName = "createToken";
    if (_pangea != null) {
      if (card != null &&
          card.cardNumber != null &&
          card.publicKey != null &&
          card.partnerIdentifier != null &&
          card.cvv != null) {
        String result = await _channel.invokeMethod(methodName, card.toMap());
        return PangeaResponse.success(result);
      } else if (card == null)
        throw PangeaParamNullException(methodName, "card");
      else if (card.cardNumber == null)
        throw PangeaParamNullException("card", "cardNumber");
      else if (card.publicKey == null)
        throw PangeaParamNullException("card", "publicKey");
      else if (card.partnerIdentifier == null)
        throw PangeaParamNullException("card", "partnerIdentifier");
      else if (card.cvv == null)
        throw PangeaParamNullException("card", "cvv");
      else
        throw PangeaMalformedException();
    } else
      _initialized = false;
    throw PangeaNotInitializedException();
  }

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}

class _Pangea {
  final bool _debugInfo;
  final String _pangeaSessionId;
  final String _environment;
  String _sessionId;

  _Pangea(this._debugInfo, this._pangeaSessionId, this._environment);

  String get sessionId => _sessionId;

  set sessionId(String value) {
    _sessionId = value;
  }

  Map<String, dynamic> toMap() => {
        if (_debugInfo != null) 'debugInfo': _debugInfo,
        if (_pangeaSessionId != null) 'pangeaSessionId': _pangeaSessionId,
        if (_environment != null) 'environment': _environment
      };
}

enum Environment { PRODUCTION, DEV, INTEGRATION }

typedef CreateTokenCallback = void Function(PangeaResponse<String> response);
