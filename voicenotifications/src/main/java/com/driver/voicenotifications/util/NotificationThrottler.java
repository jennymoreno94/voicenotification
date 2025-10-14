package com.driver.voicenotifications.util;

import androidx.annotation.NonNull;

import com.driver.voicenotifications.domain.model.NotificationType;

import java.util.HashMap;
import java.util.Map;

/**
 * Throttler para evitar spam de notificaciones
 * Implementa cooldown entre notificaciones del mismo tipo
 */
public class NotificationThrottler {
    
    private static final long DEFAULT_COOLDOWN_MS = 30000; // 30 segundos
    
    private final Map<NotificationType, Long> lastNotificationTime;
    private long cooldownMillis;

    public NotificationThrottler() {
        this(DEFAULT_COOLDOWN_MS);
    }

    public NotificationThrottler(long cooldownMillis) {
        this.cooldownMillis = cooldownMillis;
        this.lastNotificationTime = new HashMap<>();
    }

    /**
     * Verifica si se puede enviar una notificación
     * @param type Tipo de notificación
     * @return true si ha pasado el cooldown
     */
    public boolean canNotify(@NonNull NotificationType type) {
        Long lastTime = lastNotificationTime.get(type);
        if (lastTime == null) {
            return true;
        }
        
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastTime) >= cooldownMillis;
    }

    /**
     * Registra que se ha enviado una notificación
     */
    public void recordNotification(@NonNull NotificationType type) {
        lastNotificationTime.put(type, System.currentTimeMillis());
    }

    /**
     * Intenta notificar y registra si es exitoso
     * @return true si se permitió la notificación
     */
    public boolean tryNotify(@NonNull NotificationType type) {
        if (canNotify(type)) {
            recordNotification(type);
            return true;
        }
        return false;
    }

    /**
     * Resetea el cooldown para un tipo específico
     */
    public void reset(@NonNull NotificationType type) {
        lastNotificationTime.remove(type);
    }

    /**
     * Resetea todos los cooldowns
     */
    public void resetAll() {
        lastNotificationTime.clear();
    }

    /**
     * Configura el tiempo de cooldown
     */
    public void setCooldownMillis(long cooldownMillis) {
        this.cooldownMillis = cooldownMillis;
    }

    public long getCooldownMillis() {
        return cooldownMillis;
    }

    /**
     * Obtiene el tiempo restante de cooldown para un tipo
     * @return milisegundos restantes, 0 si ya puede notificar
     */
    public long getRemainingCooldown(@NonNull NotificationType type) {
        Long lastTime = lastNotificationTime.get(type);
        if (lastTime == null) {
            return 0;
        }
        
        long elapsed = System.currentTimeMillis() - lastTime;
        long remaining = cooldownMillis - elapsed;
        return Math.max(0, remaining);
    }
}
