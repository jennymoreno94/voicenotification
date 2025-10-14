package com.driver.voicenotifications.domain.repository;

import androidx.annotation.NonNull;

import com.driver.voicenotifications.domain.model.VoiceConfig;
import com.driver.voicenotifications.domain.model.VoiceNotification;

/**
 * Repositorio para gestionar notificaciones de voz
 * Siguiendo el patrón Repository de Clean Architecture
 */
public interface VoiceNotificationRepository {
    
    /**
     * Reproduce una notificación de voz
     * @param notification La notificación a reproducir
     */
    void speak(@NonNull VoiceNotification notification);
    
    /**
     * Detiene la reproducción actual
     */
    void stop();
    
    /**
     * Verifica si el TTS está hablando actualmente
     * @return true si está reproduciendo
     */
    boolean isSpeaking();
    
    /**
     * Configura el motor de voz
     * @param config Configuración del motor
     */
    void configure(@NonNull VoiceConfig config);
    
    /**
     * Verifica si el TTS está disponible
     * @return true si está disponible
     */
    boolean isAvailable();
    
    /**
     * Libera recursos del TTS
     */
    void shutdown();
}
