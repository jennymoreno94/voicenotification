package com.driver.voicenotifications.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.driver.voicenotifications.domain.model.NotificationType;
import com.driver.voicenotifications.domain.model.VoiceNotification;

/**
 * Worker para procesar notificaciones de voz en background
 * Útil para notificaciones programadas o diferidas
 */
public class VoiceNotificationWorker extends Worker {
    
    public static final String KEY_NOTIFICATION_TYPE = "notification_type";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_PRIORITY = "priority";

    public VoiceNotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            String typeStr = getInputData().getString(KEY_NOTIFICATION_TYPE);
            String message = getInputData().getString(KEY_MESSAGE);
            String priorityStr = getInputData().getString(KEY_PRIORITY);
            
            if (typeStr == null || message == null) {
                return Result.failure();
            }
            
            NotificationType type = NotificationType.valueOf(typeStr);
            VoiceNotification.Priority priority = priorityStr != null 
                ? VoiceNotification.Priority.valueOf(priorityStr)
                : VoiceNotification.Priority.NORMAL;
            
            VoiceNotification notification = new VoiceNotification.Builder()
                    .setType(type)
                    .setMessage(message)
                    .setPriority(priority)
                    .build();
            
            VoiceNotificationManager manager = VoiceNotificationManager.getInstance(getApplicationContext());
            manager.speak(notification);
            
            return Result.success();
        } catch (Exception e) {
            return Result.failure();
        }
    }
}
