package com.notificacionesvoz.utilidades;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Limitador de notificaciones para prevenir spam
 * Implementa un sistema de throttling por categoría de notificación
 * Completamente genérico y transversal a cualquier dominio de negocio
 */
public class LimitadorNotificaciones {
    
    private final long periodoEnfriamiento; // en milisegundos
    private final Map<String, Long> ultimasNotificaciones;
    private static final String CATEGORIA_PREDETERMINADA = "general";
    
    /**
     * Constructor
     * @param periodoEnfriamiento Tiempo mínimo entre notificaciones de la misma categoría (ms)
     */
    public LimitadorNotificaciones(long periodoEnfriamiento) {
        this.periodoEnfriamiento = periodoEnfriamiento;
        this.ultimasNotificaciones = new HashMap<>();
    }

    /**
     * Intenta notificar. Retorna true si se puede notificar, false si aún está en cooldown
     * @param categoria Categoría de la notificación (ej: "alerta", "recordatorio", "info")
     */
    public boolean intentarNotificar(@Nullable String categoria) {
        String clave = categoria != null ? categoria : CATEGORIA_PREDETERMINADA;
        long ahora = System.currentTimeMillis();
        Long ultimaNotificacion = ultimasNotificaciones.get(clave);
        
        if (ultimaNotificacion == null || (ahora - ultimaNotificacion) >= periodoEnfriamiento) {
            ultimasNotificaciones.put(clave, ahora);
            return true;
        }
        
        return false;
    }
    
    /**
     * Intenta notificar sin categoría (usa categoría predeterminada)
     */
    public boolean intentarNotificar() {
        return intentarNotificar(null);
    }

    /**
     * Obtiene el tiempo restante de enfriamiento para una categoría
     * @param categoria Categoría de la notificación
     * @return Milisegundos restantes, 0 si puede notificar
     */
    public long obtenerEnfriamientoRestante(@Nullable String categoria) {
        String clave = categoria != null ? categoria : CATEGORIA_PREDETERMINADA;
        Long ultimaNotificacion = ultimasNotificaciones.get(clave);
        if (ultimaNotificacion == null) {
            return 0;
        }
        
        long tiempoTranscurrido = System.currentTimeMillis() - ultimaNotificacion;
        long restante = periodoEnfriamiento - tiempoTranscurrido;
        return Math.max(0, restante);
    }
    
    /**
     * Obtiene el tiempo restante de enfriamiento para la categoría predeterminada
     */
    public long obtenerEnfriamientoRestante() {
        return obtenerEnfriamientoRestante(null);
    }
    
    /**
     * Reinicia el limitador para una categoría específica
     * @param categoria Categoría a reiniciar
     */
    public void reiniciar(@Nullable String categoria) {
        String clave = categoria != null ? categoria : CATEGORIA_PREDETERMINADA;
        ultimasNotificaciones.remove(clave);
    }
    
    /**
     * Reinicia todos los limitadores
     */
    public void reiniciarTodo() {
        ultimasNotificaciones.clear();
    }
    
    /**
     * Obtiene el período de enfriamiento configurado
     */
    public long obtenerPeriodoEnfriamiento() {
        return periodoEnfriamiento;
    }
}
