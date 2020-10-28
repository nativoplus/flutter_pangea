class PangeaNotInitializedException implements Exception {
  String _message;

  PangeaNotInitializedException() {
    _message = "Pangea is not initialized. Please call 'init()' method first.";
  }

  @override
  String toString() => _message;
}

class PangeaMalformedException implements Exception {
  String _message;

  PangeaMalformedException.message(this._message);

  PangeaMalformedException() {
    _message = "Something went wrong";
  }

  @override
  String toString() => _message;
}

class PangeaParamNullException implements Exception {
  String _message;
  String _methodName;
  String _paramName;
  String _nestedParam;

  PangeaParamNullException(this._methodName, this._paramName) {
    _message = "'$_paramName' is null into method '$_methodName'";
  }

  PangeaParamNullException.nestedParam(this._paramName, this._nestedParam) {
    _message = "'$_paramName.$_nestedParam' is null";
  }

  @override
  String toString() => _message;
}
