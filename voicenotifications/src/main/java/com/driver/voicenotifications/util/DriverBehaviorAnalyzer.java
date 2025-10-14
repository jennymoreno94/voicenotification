package com.driver.voicenotifications.util;

import androidx.annotation.NonNull;

import com.driver.voicenotifications.domain.model.NotificationType;

/**
 * Analizador de comportamiento del conductor
 * Proporciona lógica para determinar cuándo emitir notificaciones
 */
public class DriverBehaviorAnalyzer {
    
    private static final int DEFAULT_SPEED_THRESHOLD = 10; // km/h sobre el límite
    private static final float DEFAULT_HARSH_BRAKING_THRESHOLD = -8.0f; // m/s²
    private static final float DEFAULT_HARSH_ACCELERATION_THRESHOLD = 4.0f; // m/s²
    private static final float DEFAULT_SHARP_TURN_THRESHOLD = 5.0f; // m/s²
    
    private int speedThreshold = DEFAULT_SPEED_THRESHOLD;
    private float harshBrakingThreshold = DEFAULT_HARSH_BRAKING_THRESHOLD;
    private float harshAccelerationThreshold = DEFAULT_HARSH_ACCELERATION_THRESHOLD;
    private float sharpTurnThreshold = DEFAULT_SHARP_TURN_THRESHOLD;

    /**
     * Analiza si hay exceso de velocidad
     */
    public boolean isSpeedExcess(int currentSpeed, int speedLimit) {
        return currentSpeed > (speedLimit + speedThreshold);
    }

    /**
     * Analiza si hay frenada brusca
     * @param acceleration Aceleración en m/s² (negativa para frenado)
     */
    public boolean isHarshBraking(float acceleration) {
        return acceleration < harshBrakingThreshold;
    }

    /**
     * Analiza si hay aceleración brusca
     * @param acceleration Aceleración en m/s²
     */
    public boolean isHarshAcceleration(float acceleration) {
        return acceleration > harshAccelerationThreshold;
    }

    /**
     * Analiza si hay giro brusco
     * @param lateralAcceleration Aceleración lateral en m/s²
     */
    public boolean isSharpTurn(float lateralAcceleration) {
        return Math.abs(lateralAcceleration) > sharpTurnThreshold;
    }

    /**
     * Determina el tipo de notificación basado en los datos del sensor
     */
    @NonNull
    public NotificationType analyzeAcceleration(float acceleration, float lateralAcceleration) {
        if (isHarshBraking(acceleration)) {
            return NotificationType.HARSH_BRAKING;
        } else if (isHarshAcceleration(acceleration)) {
            return NotificationType.HARSH_ACCELERATION;
        } else if (isSharpTurn(lateralAcceleration)) {
            return NotificationType.SHARP_TURN;
        }
        return NotificationType.CUSTOM;
    }

    // Setters para personalizar umbrales
    public void setSpeedThreshold(int speedThreshold) {
        this.speedThreshold = speedThreshold;
    }

    public void setHarshBrakingThreshold(float harshBrakingThreshold) {
        this.harshBrakingThreshold = harshBrakingThreshold;
    }

    public void setHarshAccelerationThreshold(float harshAccelerationThreshold) {
        this.harshAccelerationThreshold = harshAccelerationThreshold;
    }

    public void setSharpTurnThreshold(float sharpTurnThreshold) {
        this.sharpTurnThreshold = sharpTurnThreshold;
    }

    // Getters
    public int getSpeedThreshold() {
        return speedThreshold;
    }

    public float getHarshBrakingThreshold() {
        return harshBrakingThreshold;
    }

    public float getHarshAccelerationThreshold() {
        return harshAccelerationThreshold;
    }

    public float getSharpTurnThreshold() {
        return sharpTurnThreshold;
    }
}
