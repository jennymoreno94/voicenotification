package com.driver.voicenotifications.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.driver.voicenotifications.domain.model.NotificationType;
import com.driver.voicenotifications.domain.model.VoiceNotification;
import com.driver.voicenotifications.presentation.VoiceNotificationWorker;

import java.util.concurrent.TimeUnit;

/**
 * Utilidad para programar notificaciones de voz diferidas
 * Usa WorkManager para garantizar la entrega
 */
public class VoiceNotificationScheduler {
    
    private final WorkManager workManager;

    public VoiceNotificationScheduler(@NonNull Context context) {
        this.workManager = WorkManager.getInstance(context.getApplicationContext());
    }

    /**
     * Programa una notificación para ser reproducida después de un delay
     */
    public void scheduleNotification(@NonNull VoiceNotification notification, long delayMillis) {
        Data inputData = new Data.Builder()
                .putString(VoiceNotificationWorker.KEY_NOTIFICATION_TYPE, notification.getType().name())
                .putString(VoiceNotificationWorker.KEY_MESSAGE, notification.getMessage())
                .putString(VoiceNotificationWorker.KEY_PRIORITY, notification.getPriority().name())
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(VoiceNotificationWorker.class)
                .setInputData(inputData)
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .addTag(notification.getType().name())
                .build();

        workManager.enqueue(workRequest);
    }

    /**
     * Cancela todas las notificaciones programadas de un tipo específico
     */
    public void cancelNotifications(@NonNull NotificationType type) {
        workManager.cancelAllWorkByTag(type.name());
    }

    /**
     * Cancela todas las notificaciones programadas
     */
    public void cancelAllNotifications() {
        workManager.cancelAllWork();
    }
}
