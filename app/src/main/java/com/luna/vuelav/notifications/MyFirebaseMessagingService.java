package com.luna.vuelav.notifications;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Looper.prepare();
        new Handler().postDelayed(() -> {
            if (message.getNotification() != null) {
                Toast.makeText(getBaseContext(), Objects.requireNonNull(message.getNotification()).getTitle(), Toast.LENGTH_LONG).show();
            }
        }, 2000);
        Looper.loop();
    }
}
