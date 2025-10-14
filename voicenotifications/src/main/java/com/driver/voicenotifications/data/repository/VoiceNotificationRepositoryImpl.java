package com.driver.voicenotifications.data.repository;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import androidx.annotation.NonNull;

import com.driver.voicenotifications.domain.model.VoiceConfig;
import com.driver.voicenotifications.domain.model.VoiceNotification;
import com.driver.voicenotifications.domain.repository.VoiceNotificationRepository;

import java.util.HashMap;
import java.util.Locale;

/**
 * Implementación del repositorio usando TextToSpeech de Android
 */
public class VoiceNotificationRepositoryImpl implements VoiceNotificationRepository {
    
    private static final String TAG = "VoiceNotificationRepo";
    
    private TextToSpeech textToSpeech;
    private VoiceConfig currentConfig;
    private boolean isInitialized = false;
    private final Context context;
    private VoiceNotificationListener listener;

    public VoiceNotificationRepositoryImpl(@NonNull Context context) {
        this.context = context.getApplicationContext();
        this.currentConfig = VoiceConfig.getDefault();
        initializeTTS();
    }

    private void initializeTTS() {
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(currentConfig.getLocale());
                
                if (result == TextToSpeech.LANG_MISSING_DATA || 
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "Language not supported: " + currentConfig.getLocale());
                    // Fallback to default locale
                    textToSpeech.setLanguage(Locale.getDefault());
                }
                
                textToSpeech.setPitch(currentConfig.getPitch());
                textToSpeech.setSpeechRate(currentConfig.getSpeechRate());
                
                textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        Log.d(TAG, "Started speaking: " + utteranceId);
                        if (listener != null) {
                            listener.onStart(utteranceId);
                        }
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        Log.d(TAG, "Finished speaking: " + utteranceId);
                        if (listener != null) {
                            listener.onDone(utteranceId);
                        }
                    }

                    @Override
                    public void onError(String utteranceId) {
                        Log.e(TAG, "Error speaking: " + utteranceId);
                        if (listener != null) {
                            listener.onError(utteranceId);
                        }
                    }
                });
                
                isInitialized = true;
                Log.i(TAG, "TextToSpeech initialized successfully");
            } else {
                Log.e(TAG, "TextToSpeech initialization failed");
                isInitialized = false;
            }
        });
    }

    @Override
    public void speak(@NonNull VoiceNotification notification) {
        if (!isInitialized || textToSpeech == null) {
            Log.w(TAG, "TTS not initialized, cannot speak");
            return;
        }

        if (!currentConfig.isEnabled()) {
            Log.d(TAG, "Voice notifications are disabled");
            return;
        }

        String utteranceId = notification.getType().name() + "_" + notification.getTimestamp();
        
        HashMap<String, String> params = new HashMap<>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
        
        int queueMode = notification.getPriority().getLevel() >= VoiceNotification.Priority.HIGH.getLevel() 
            ? TextToSpeech.QUEUE_FLUSH 
            : currentConfig.getQueueMode();
        
        textToSpeech.speak(notification.getMessage(), queueMode, params);
        
        Log.d(TAG, "Speaking notification: " + notification.getMessage());
    }

    @Override
    public void stop() {
        if (textToSpeech != null && isInitialized) {
            textToSpeech.stop();
            Log.d(TAG, "Stopped speaking");
        }
    }

    @Override
    public boolean isSpeaking() {
        return textToSpeech != null && isInitialized && textToSpeech.isSpeaking();
    }

    @Override
    public void configure(@NonNull VoiceConfig config) {
        this.currentConfig = config;
        
        if (textToSpeech != null && isInitialized) {
            textToSpeech.setLanguage(config.getLocale());
            textToSpeech.setPitch(config.getPitch());
            textToSpeech.setSpeechRate(config.getSpeechRate());
            Log.i(TAG, "Voice configuration updated");
        }
    }

    @Override
    public boolean isAvailable() {
        return isInitialized && textToSpeech != null;
    }

    @Override
    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            isInitialized = false;
            Log.i(TAG, "TextToSpeech shutdown");
        }
    }

    /**
     * Establece un listener para eventos de TTS
     */
    public void setListener(VoiceNotificationListener listener) {
        this.listener = listener;
    }

    /**
     * Interface para escuchar eventos de las notificaciones de voz
     */
    public interface VoiceNotificationListener {
        void onStart(String utteranceId);
        void onDone(String utteranceId);
        void onError(String utteranceId);
    }
}
