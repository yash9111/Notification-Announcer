package io.flutter.notificationreader;

import android.app.Notification;
import android.content.Context;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import androidx.annotation.NonNull;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodChannel;
import java.util.Locale;

public class NotificationListener extends NotificationListenerService {
    private static final String CHANNEL = "notification_listener";
    private MethodChannel methodChannel;

    @Override
    public void onCreate() {
        FlutterEngine flutterEngine = new FlutterEngine(this);
        flutterEngine.getDartExecutor().executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        );
        methodChannel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL);
        super.onCreate();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn == null || sbn.getNotification() == null) return;

        Notification notification = sbn.getNotification();
        CharSequence title = notification.extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence text = notification.extras.getCharSequence(Notification.EXTRA_TEXT);

        if (title != null && text != null) {
            String message = title + " says " + text;
            // System.out.println(message);

            // Send notification data to Dart
            methodChannel.invokeMethod("onNotificationReceived", message);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
