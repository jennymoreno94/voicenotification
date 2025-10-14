package com.driver.voicenotifications.domain.repository;

import androidx.annotation.NonNull;

import com.driver.voicenotifications.domain.model.ConfiguracionVoz;
import com.driver.voicenotifications.domain.model.NotificacionVoz;

/**
 * Interfaz del repositorio para gestionar notificaciones de voz
 * Define el contrato para la capa de datos
 */
public interface RepositorioNotificacionesVoz {
    
    /**
     * Reproduce una notificación de voz
     * @param notificacion La notificación a reproducir
     */
    void reproducir(@NonNull NotificacionVoz notificacion);
    
    /**
     * Detiene la reproducción actual
     */
    void detener();
    
    /**
     * Verifica si está reproduciendo actualmente
     * @return true si está reproduciendo
     */
    boolean estaReproduciendo();
    
    /**
     * Configura el motor de voz
     * @param configuracion La configuración a aplicar
     */
    void configurar(@NonNull ConfiguracionVoz configuracion);
    
    /**
     * Verifica si el servicio de voz está disponible
     * @return true si está disponible
     */
    boolean estaDisponible();
    
    /**
     * Finaliza y libera recursos del motor de voz
     */
    void finalizar();
}
