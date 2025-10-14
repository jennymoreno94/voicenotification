package com.notificacionesvoz.dominio.modelo;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import java.util.Locale;

/**
 * Configuración para el motor de Text-to-Speech
 */
public class ConfiguracionVoz {
    
    private final float velocidadVoz;
    private final float tonoVoz;
    private final Locale idioma;
    private final boolean habilitado;
    private final int modoCola;

    private ConfiguracionVoz(Constructor constructor) {
        this.velocidadVoz = constructor.velocidadVoz;
        this.tonoVoz = constructor.tonoVoz;
        this.idioma = constructor.idioma;
        this.habilitado = constructor.habilitado;
        this.modoCola = constructor.modoCola;
    }

    @FloatRange(from = 0.1, to = 3.0)
    public float obtenerVelocidadVoz() {
        return velocidadVoz;
    }

    @FloatRange(from = 0.1, to = 2.0)
    public float obtenerTonoVoz() {
        return tonoVoz;
    }

    @NonNull
    public Locale obtenerIdioma() {
        return idioma;
    }

    public boolean estaHabilitado() {
        return habilitado;
    }

    public int obtenerModoCola() {
        return modoCola;
    }

    /**
     * Constructor para crear configuraciones de voz
     */
    public static class Constructor {
        private float velocidadVoz = 1.0f;
        private float tonoVoz = 1.0f;
        private Locale idioma = new Locale("es", "ES");
        private boolean habilitado = true;
        private int modoCola = 0; // QUEUE_FLUSH

        public Constructor establecerVelocidadVoz(@FloatRange(from = 0.1, to = 3.0) float velocidadVoz) {
            if (velocidadVoz < 0.1f || velocidadVoz > 3.0f) {
                throw new IllegalArgumentException("La velocidad de voz debe estar entre 0.1 y 3.0");
            }
            this.velocidadVoz = velocidadVoz;
            return this;
        }

        public Constructor establecerTonoVoz(@FloatRange(from = 0.1, to = 2.0) float tonoVoz) {
            if (tonoVoz < 0.1f || tonoVoz > 2.0f) {
                throw new IllegalArgumentException("El tono de voz debe estar entre 0.1 y 2.0");
            }
            this.tonoVoz = tonoVoz;
            return this;
        }

        public Constructor establecerIdioma(@NonNull Locale idioma) {
            this.idioma = idioma;
            return this;
        }

        public Constructor establecerHabilitado(boolean habilitado) {
            this.habilitado = habilitado;
            return this;
        }

        public Constructor establecerModoCola(int modoCola) {
            this.modoCola = modoCola;
            return this;
        }

        public ConfiguracionVoz construir() {
            return new ConfiguracionVoz(this);
        }
    }

    public static Constructor constructor() {
        return new Constructor();
    }

    public static ConfiguracionVoz obtenerPredeterminada() {
        return new Constructor().construir();
    }
}
