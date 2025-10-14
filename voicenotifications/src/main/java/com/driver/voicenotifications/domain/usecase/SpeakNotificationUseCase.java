package com.driver.voicenotifications.domain.usecase;

import androidx.annotation.NonNull;

import com.driver.voicenotifications.domain.model.VoiceNotification;
import com.driver.voicenotifications.domain.repository.VoiceNotificationRepository;

/**
 * Caso de uso para reproducir notificaciones de voz
 * Implementa la lógica de negocio
 */
public class SpeakNotificationUseCase {
    
    private final VoiceNotificationRepository repository;

    public SpeakNotificationUseCase(@NonNull VoiceNotificationRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta el caso de uso
     * @param notification Notificación a reproducir
     */
    public void execute(@NonNull VoiceNotification notification) {
        if (!repository.isAvailable()) {
            throw new IllegalStateException("Voice notification service is not available");
        }
        
        // Si hay una notificación de alta prioridad, detener la actual
        if (notification.getPriority().getLevel() >= VoiceNotification.Priority.HIGH.getLevel()) {
            if (repository.isSpeaking()) {
                repository.stop();
            }
        }
        
        repository.speak(notification);
    }
}
