package com.driver.voicenotifications.util;

import androidx.annotation.NonNull;

import com.driver.voicenotifications.domain.model.TipoNotificacion;

/**
 * Analizador de comportamiento del conductor
 * Proporciona lógica para determinar cuándo emitir notificaciones
 */
public class AnalizadorComportamientoConductor {
    
    private static final int UMBRAL_VELOCIDAD_PREDETERMINADO = 10; // km/h sobre el límite
    private static final float UMBRAL_FRENADA_BRUSCA_PREDETERMINADO = -8.0f; // m/s²
    private static final float UMBRAL_ACELERACION_BRUSCA_PREDETERMINADO = 4.0f; // m/s²
    private static final float UMBRAL_GIRO_BRUSCO_PREDETERMINADO = 5.0f; // m/s²
    
    private int umbralVelocidad = UMBRAL_VELOCIDAD_PREDETERMINADO;
    private float umbralFrenadaBrusca = UMBRAL_FRENADA_BRUSCA_PREDETERMINADO;
    private float umbralAceleracionBrusca = UMBRAL_ACELERACION_BRUSCA_PREDETERMINADO;
    private float umbralGiroBrusco = UMBRAL_GIRO_BRUSCO_PREDETERMINADO;

    /**
     * Analiza si hay exceso de velocidad
     */
    public boolean esExcesoVelocidad(int velocidadActual, int limiteVelocidad) {
        return velocidadActual > (limiteVelocidad + umbralVelocidad);
    }

    /**
     * Analiza si hay frenada brusca
     * @param aceleracion Aceleración en m/s² (negativa para frenado)
     */
    public boolean esFrenadaBrusca(float aceleracion) {
        return aceleracion < umbralFrenadaBrusca;
    }

    /**
     * Analiza si hay aceleración brusca
     * @param aceleracion Aceleración en m/s²
     */
    public boolean esAceleracionBrusca(float aceleracion) {
        return aceleracion > umbralAceleracionBrusca;
    }

    /**
     * Analiza si hay giro brusco
     * @param aceleracionLateral Aceleración lateral en m/s²
     */
    public boolean esGiroBrusco(float aceleracionLateral) {
        return Math.abs(aceleracionLateral) > umbralGiroBrusco;
    }

    /**
     * Determina el tipo de notificación basado en los datos del sensor
     */
    @NonNull
    public TipoNotificacion analizarAceleracion(float aceleracion, float aceleracionLateral) {
        if (esFrenadaBrusca(aceleracion)) {
            return TipoNotificacion.FRENADA_BRUSCA;
        } else if (esAceleracionBrusca(aceleracion)) {
            return TipoNotificacion.ACELERACION_BRUSCA;
        } else if (esGiroBrusco(aceleracionLateral)) {
            return TipoNotificacion.GIRO_BRUSCO;
        }
        return TipoNotificacion.PERSONALIZADA;
    }

    // Setters para personalizar umbrales
    public void establecerUmbralVelocidad(int umbralVelocidad) {
        this.umbralVelocidad = umbralVelocidad;
    }

    public void establecerUmbralFrenadaBrusca(float umbralFrenadaBrusca) {
        this.umbralFrenadaBrusca = umbralFrenadaBrusca;
    }

    public void establecerUmbralAceleracionBrusca(float umbralAceleracionBrusca) {
        this.umbralAceleracionBrusca = umbralAceleracionBrusca;
    }

    public void establecerUmbralGiroBrusco(float umbralGiroBrusco) {
        this.umbralGiroBrusco = umbralGiroBrusco;
    }

    // Getters
    public int obtenerUmbralVelocidad() {
        return umbralVelocidad;
    }

    public float obtenerUmbralFrenadaBrusca() {
        return umbralFrenadaBrusca;
    }

    public float obtenerUmbralAceleracionBrusca() {
        return umbralAceleracionBrusca;
    }

    public float obtenerUmbralGiroBrusco() {
        return umbralGiroBrusco;
    }
}
