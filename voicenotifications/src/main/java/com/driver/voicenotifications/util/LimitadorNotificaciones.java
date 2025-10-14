package com.driver.voicenotifications.util;

import androidx.annotation.NonNull;

import com.driver.voicenotifications.domain.model.TipoNotificacion;

import java.util.HashMap;
import java.util.Map;

/**
 * Limitador para evitar spam de notificaciones
 * Implementa cooldown entre notificaciones del mismo tipo
 */
public class LimitadorNotificaciones {
    
    private static final long TIEMPO_ESPERA_PREDETERMINADO_MS = 30000; // 30 segundos
    
    private final Map<TipoNotificacion, Long> ultimaNotificacion;
    private long tiempoEsperaMs;

    public LimitadorNotificaciones() {
        this(TIEMPO_ESPERA_PREDETERMINADO_MS);
    }

    public LimitadorNotificaciones(long tiempoEsperaMs) {
        this.tiempoEsperaMs = tiempoEsperaMs;
        this.ultimaNotificacion = new HashMap<>();
    }

    /**
     * Verifica si se puede enviar una notificación
     * @param tipo Tipo de notificación
     * @return true si ha pasado el tiempo de espera
     */
    public boolean puedeNotificar(@NonNull TipoNotificacion tipo) {
        Long ultimaVez = ultimaNotificacion.get(tipo);
        if (ultimaVez == null) {
            return true;
        }
        
        long tiempoActual = System.currentTimeMillis();
        return (tiempoActual - ultimaVez) >= tiempoEsperaMs;
    }

    /**
     * Registra que se ha enviado una notificación
     */
    public void registrarNotificacion(@NonNull TipoNotificacion tipo) {
        ultimaNotificacion.put(tipo, System.currentTimeMillis());
    }

    /**
     * Intenta notificar y registra si es exitoso
     * @return true si se permitió la notificación
     */
    public boolean intentarNotificar(@NonNull TipoNotificacion tipo) {
        if (puedeNotificar(tipo)) {
            registrarNotificacion(tipo);
            return true;
        }
        return false;
    }

    /**
     * Resetea el cooldown para un tipo específico
     */
    public void reiniciar(@NonNull TipoNotificacion tipo) {
        ultimaNotificacion.remove(tipo);
    }

    /**
     * Resetea todos los cooldowns
     */
    public void reiniciarTodo() {
        ultimaNotificacion.clear();
    }

    /**
     * Configura el tiempo de espera
     */
    public void establecerTiempoEsperaMs(long tiempoEsperaMs) {
        this.tiempoEsperaMs = tiempoEsperaMs;
    }

    public long obtenerTiempoEsperaMs() {
        return tiempoEsperaMs;
    }

    /**
     * Obtiene el tiempo restante de espera para un tipo
     * @return milisegundos restantes, 0 si ya puede notificar
     */
    public long obtenerTiempoRestante(@NonNull TipoNotificacion tipo) {
        Long ultimaVez = ultimaNotificacion.get(tipo);
        if (ultimaVez == null) {
            return 0;
        }
        
        long tiempoTranscurrido = System.currentTimeMillis() - ultimaVez;
        long tiempoRestante = tiempoEsperaMs - tiempoTranscurrido;
        return Math.max(0, tiempoRestante);
    }
}
