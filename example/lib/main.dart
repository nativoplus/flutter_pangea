import 'package:flutter/material.dart';
import 'package:flutter_pangea/card.dart';
import 'package:flutter_pangea/flutter_pangea.dart';
import 'package:flutter_pangea/status.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  FlutterPangea _flutterPangea;
  String _publicKey, _partnerIdentifier, _cardNumber, _cvv;
  String _response = "";
  Color _responseColor = failureColor;

  @override
  void initState() {
    super.initState();
    _flutterPangea = FlutterPangea.instance;
    _flutterPangea.init(true, "239842384", Environment.DEV).then(
        (value) => {
              //"239842384" is sessionId
              if (value.success)
                setState(() {
                  _platformVersion = value.data.sessionId;
                })
            }, onError: (e) {
      setState(() {
        _response = "Cannot initialize Pangea ${e.toString()}";
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: TextField(
                onChanged: (text) {
                  _publicKey = text;
                },
                decoration: InputDecoration(
                    hintText: "Public Key",
                    hintStyle: TextStyle(color: Color(0xFF9E9E9E))),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 8.0, right: 8),
              child: TextField(
                onChanged: (text) {
                  _partnerIdentifier = text;
                },
                decoration: InputDecoration(
                    hintText: "Partner Identifier",
                    hintStyle: TextStyle(color: Color(0xFF9E9E9E))),
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: TextField(
                onChanged: (text) {
                  _cardNumber = text;
                },
                keyboardType: TextInputType.number,
                maxLength: 20,
                decoration: InputDecoration(
                    hintText: "Card Number",
                    hintStyle: TextStyle(color: Color(0xFF9E9E9E))),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 8.0, right: 8),
              child: TextField(
                onChanged: (text) {
                  _cvv = text;
                },
                keyboardType: TextInputType.number,
                maxLength: 3,
                decoration: InputDecoration(
                    hintText: "CVV",
                    hintStyle: TextStyle(color: Color(0xFF9E9E9E))),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 8),
              child: Text(
                "Response :",
                style: TextStyle(fontSize: 18, color: Color(0xFF38C0FF)),
              ),
            ),
            Expanded(
              child: SingleChildScrollView(
                child: Padding(
                  padding: const EdgeInsets.only(top: 8),
                  child: Text(
                    _response,
                    style: TextStyle(fontSize: 15, color: _responseColor),
                  ),
                ),
              ),
            ),
            FlatButton(
              onPressed: () {
                _flutterPangea
                    .createToken(CardInformation(
                        _publicKey, _partnerIdentifier, _cardNumber, _cvv))
                    .then((value) => _handleResponse(value), onError: (e) {
                  setState(() {
                    _responseColor = failureColor;
                    _response = e.toString();
                  });
                });
              },
              color: Color(0xFF38C0FF),
              child: Text(
                "Get Response",
                style: TextStyle(fontSize: 15, color: Color(0xFFFFFFFF)),
              ),
            )
          ],
        ),
      ),
    );
  }

  _handleResponse(PangeaResponse<String> value) {
    setState(() {
      _responseColor = value.success ? successColor : failureColor;
      _response = value.data;
      // for (var i = 0; i <= 10; i++) _response += _response;
    });
  }
}

const successColor = Color(0xFF00D612);
const failureColor = Color(0xFFF41100);
