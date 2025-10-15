package com.notificacionesvoz.utilidades;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Analizador de comportamiento del conductor
 * 
 * NOTA: Esta es una clase de utilidad OPCIONAL específica para aplicaciones
 * de gestión de conductores. Si tu aplicación no maneja conductores,
 * puedes ignorar esta clase completamente.
 * 
 * Proporciona lógica para analizar datos de sensores y determinar
 * cuándo emitir notificaciones relacionadas con la conducción.
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
     * Determina la categoría de evento basado en los datos del sensor
     * @param aceleracion Aceleración en m/s²
     * @param aceleracionLateral Aceleración lateral en m/s²
     * @return Categoría del evento detectado, o null si no hay evento
     */
    @Nullable
    public String analizarAceleracion(float aceleracion, float aceleracionLateral) {
        if (esFrenadaBrusca(aceleracion)) {
            return "frenada_brusca";
        } else if (esAceleracionBrusca(aceleracion)) {
            return "aceleracion_brusca";
        } else if (esGiroBrusco(aceleracionLateral)) {
            return "giro_brusco";
        }
        return null;
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
