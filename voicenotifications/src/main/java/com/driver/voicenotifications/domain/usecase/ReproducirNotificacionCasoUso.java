package com.driver.voicenotifications.domain.usecase;

import androidx.annotation.NonNull;

import com.driver.voicenotifications.domain.model.NotificacionVoz;
import com.driver.voicenotifications.domain.repository.RepositorioNotificacionesVoz;

/**
 * Caso de uso para reproducir notificaciones de voz
 * Encapsula la lógica de negocio para la reproducción
 */
public class ReproducirNotificacionCasoUso {
    
    private final RepositorioNotificacionesVoz repositorio;

    public ReproducirNotificacionCasoUso(@NonNull RepositorioNotificacionesVoz repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Ejecuta el caso de uso
     * @param notificacion La notificación a reproducir
     */
    public void ejecutar(@NonNull NotificacionVoz notificacion) {
        // Si la prioridad es alta o urgente, interrumpir reproducción actual
        if (notificacion.obtenerPrioridad().obtenerNivel() >= NotificacionVoz.Prioridad.ALTA.obtenerNivel()) {
            repositorio.detener();
        }
        
        repositorio.reproducir(notificacion);
    }
}
