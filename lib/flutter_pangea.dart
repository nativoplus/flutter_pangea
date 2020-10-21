
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterPangea {
  static const MethodChannel _channel =
      const MethodChannel('flutter_pangea');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
