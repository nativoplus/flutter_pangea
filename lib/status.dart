class PangeaResponse<T>{
  T data;
  bool _success = false;
  Exception _error;

  PangeaResponse.success(T data){
    this.data = data;
    _success = true;
    _error = null;
  }

  PangeaResponse.error(Exception error){
    this.data = null;
    _success = false;
    _error = error;
  }

  get success => _success;
  get error => _error;
}