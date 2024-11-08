import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:flutter/services.dart';

void main() {
  NotificationService.initialize;
  NotificationService._handleMethod;
  runApp(MyApp());
}

class NotificationService {
  static const MethodChannel _channel = MethodChannel("notification_listener");

  static Future<void> initialize() async {
        await _channel.invokeMethod('initialize');

    _channel.setMethodCallHandler(_handleMethod);
  }

  static Future<void> _handleMethod(MethodCall call) async {
    if (call.method == "onNotificationReceived") {
      final String message = call.arguments as String;
      print('Notification Received: $message');
    }
  }
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('Notification Reader'),
        ),
        body: Center(
          child: ElevatedButton(
            onPressed: () async {
              if (await Permission.notification.isDenied) {
                openAppSettings();
              } else {
                print("Access Given ");
              }
            },
            child: Text('Grant Notification Access'),
          ),
        ),
      ),
    );
  }
}
