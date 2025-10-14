package com.notificacionesvoz.dominio.casosuso;

import androidx.annotation.NonNull;

import com.notificacionesvoz.dominio.modelo.NotificacionVoz;
import com.notificacionesvoz.dominio.repositorio.RepositorioNotificacionesVoz;

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
