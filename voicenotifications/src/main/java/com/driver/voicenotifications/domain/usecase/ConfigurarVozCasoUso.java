package com.driver.voicenotifications.domain.usecase;

import androidx.annotation.NonNull;

import com.driver.voicenotifications.domain.model.ConfiguracionVoz;
import com.driver.voicenotifications.domain.repository.RepositorioNotificacionesVoz;

/**
 * Caso de uso para configurar el motor de voz
 * Encapsula la lógica de negocio para la configuración
 */
public class ConfigurarVozCasoUso {
    
    private final RepositorioNotificacionesVoz repositorio;

    public ConfigurarVozCasoUso(@NonNull RepositorioNotificacionesVoz repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Ejecuta el caso de uso
     * @param configuracion La configuración a aplicar
     */
    public void ejecutar(@NonNull ConfiguracionVoz configuracion) {
        repositorio.configurar(configuracion);
    }
}
