package com.driver.voicenotifications.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.driver.voicenotifications.data.factory.NotificationMessageFactory;
import com.driver.voicenotifications.data.repository.VoiceNotificationRepositoryImpl;
import com.driver.voicenotifications.domain.model.NotificationType;
import com.driver.voicenotifications.domain.model.VoiceConfig;
import com.driver.voicenotifications.domain.model.VoiceNotification;
import com.driver.voicenotifications.domain.repository.VoiceNotificationRepository;
import com.driver.voicenotifications.domain.usecase.ConfigureVoiceUseCase;
import com.driver.voicenotifications.domain.usecase.SpeakNotificationUseCase;

/**
 * Punto de entrada principal de la librería
 * Gestiona el ciclo de vida y proporciona API simple para los consumidores
 */
public class VoiceNotificationManager implements DefaultLifecycleObserver {
    
    private static volatile VoiceNotificationManager instance;
    
    private final VoiceNotificationRepository repository;
    private final SpeakNotificationUseCase speakUseCase;
    private final ConfigureVoiceUseCase configureUseCase;
    private final MutableLiveData<NotificationEvent> eventLiveData;
    
    private VoiceNotificationManager(@NonNull Context context) {
        VoiceNotificationRepositoryImpl repoImpl = new VoiceNotificationRepositoryImpl(context);
        this.repository = repoImpl;
        this.speakUseCase = new SpeakNotificationUseCase(repository);
        this.configureUseCase = new ConfigureVoiceUseCase(repository);
        this.eventLiveData = new MutableLiveData<>();
        
        // Configurar listener para eventos
        repoImpl.setListener(new VoiceNotificationRepositoryImpl.VoiceNotificationListener() {
            @Override
            public void onStart(String utteranceId) {
                eventLiveData.postValue(new NotificationEvent(NotificationEvent.Type.STARTED, utteranceId));
            }

            @Override
            public void onDone(String utteranceId) {
                eventLiveData.postValue(new NotificationEvent(NotificationEvent.Type.COMPLETED, utteranceId));
            }

            @Override
            public void onError(String utteranceId) {
                eventLiveData.postValue(new NotificationEvent(NotificationEvent.Type.ERROR, utteranceId));
            }
        });
    }

    /**
     * Obtiene la instancia singleton (thread-safe)
     */
    @NonNull
    public static VoiceNotificationManager getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (VoiceNotificationManager.class) {
                if (instance == null) {
                    instance = new VoiceNotificationManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * Reproduce una notificación de voz
     */
    public void speak(@NonNull VoiceNotification notification) {
        try {
            speakUseCase.execute(notification);
        } catch (Exception e) {
            eventLiveData.postValue(new NotificationEvent(NotificationEvent.Type.ERROR, e.getMessage()));
        }
    }

    /**
     * Reproduce una notificación con mensaje predefinido
     */
    public void speak(@NonNull NotificationType type) {
        String message = NotificationMessageFactory.getSpanishMessage(type);
        VoiceNotification notification = new VoiceNotification.Builder()
                .setType(type)
                .setMessage(message)
                .setPriority(VoiceNotification.Priority.NORMAL)
                .build();
        speak(notification);
    }

    /**
     * Reproduce notificación de exceso de velocidad con datos
     */
    public void speakSpeedExcess(int currentSpeed, int speedLimit) {
        String message = NotificationMessageFactory.getSpeedExcessMessage(currentSpeed, speedLimit);
        VoiceNotification notification = new VoiceNotification.Builder()
                .setType(NotificationType.SPEED_EXCESS)
                .setMessage(message)
                .setPriority(VoiceNotification.Priority.HIGH)
                .build();
        speak(notification);
    }

    /**
     * Configura el motor de voz
     */
    public void configure(@NonNull VoiceConfig config) {
        configureUseCase.execute(config);
    }

    /**
     * Detiene la reproducción actual
     */
    public void stop() {
        repository.stop();
    }

    /**
     * Verifica si está reproduciendo
     */
    public boolean isSpeaking() {
        return repository.isSpeaking();
    }

    /**
     * Verifica si el servicio está disponible
     */
    public boolean isAvailable() {
        return repository.isAvailable();
    }

    /**
     * Obtiene LiveData para observar eventos
     */
    @NonNull
    public LiveData<NotificationEvent> getEvents() {
        return eventLiveData;
    }

    // Lifecycle callbacks
    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        // Pausar notificaciones cuando la app va a background
        stop();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        // Liberar recursos
        repository.shutdown();
        owner.getLifecycle().removeObserver(this);
    }

    /**
     * Clase para eventos de notificaciones
     */
    public static class NotificationEvent {
        public enum Type {
            STARTED, COMPLETED, ERROR
        }

        private final Type type;
        private final String message;

        public NotificationEvent(Type type, String message) {
            this.type = type;
            this.message = message;
        }

        public Type getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }
    }
}
