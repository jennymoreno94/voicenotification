package com.driver.voicenotifications.domain.model;

/**
 * Tipos de notificaciones para conductores
 */
public enum NotificationType {
    SPEED_EXCESS("Exceso de velocidad"),
    HARSH_BRAKING("Frenada brusca"),
    HARSH_ACCELERATION("Aceleración brusca"),
    SHARP_TURN("Giro brusco"),
    CUSTOM("Notificación personalizada");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
