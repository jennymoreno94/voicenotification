package com.driver.voicenotifications.domain.usecase;

import androidx.annotation.NonNull;

import com.driver.voicenotifications.domain.model.VoiceConfig;
import com.driver.voicenotifications.domain.repository.VoiceNotificationRepository;

/**
 * Caso de uso para configurar el motor de voz
 */
public class ConfigureVoiceUseCase {
    
    private final VoiceNotificationRepository repository;

    public ConfigureVoiceUseCase(@NonNull VoiceNotificationRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta el caso de uso
     * @param config Configuración a aplicar
     */
    public void execute(@NonNull VoiceConfig config) {
        repository.configure(config);
    }
}
