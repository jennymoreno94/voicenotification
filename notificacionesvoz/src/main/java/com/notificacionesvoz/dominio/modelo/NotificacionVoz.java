package com.notificacionesvoz.dominio.modelo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Modelo de dominio para una notificación de voz
 * Diseñado para ser completamente genérico y transversal a cualquier dominio de negocio
 */
public class NotificacionVoz {
    
    private final String mensaje;
    private final Prioridad prioridad;
    private final long marcaTiempo;
    @Nullable
    private final String categoria;
    @Nullable
    private final Object metadatos;

    private NotificacionVoz(Constructor constructor) {
        this.mensaje = constructor.mensaje;
        this.prioridad = constructor.prioridad;
        this.marcaTiempo = constructor.marcaTiempo;
        this.categoria = constructor.categoria;
        this.metadatos = constructor.metadatos;
    }

    @NonNull
    public String obtenerMensaje() {
        return mensaje;
    }

    @NonNull
    public Prioridad obtenerPrioridad() {
        return prioridad;
    }

    public long obtenerMarcaTiempo() {
        return marcaTiempo;
    }

    @Nullable
    public String obtenerCategoria() {
        return categoria;
    }

    @Nullable
    public Object obtenerMetadatos() {
        return metadatos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificacionVoz that = (NotificacionVoz) o;
        return marcaTiempo == that.marcaTiempo &&
                Objects.equals(mensaje, that.mensaje) &&
                prioridad == that.prioridad &&
                Objects.equals(categoria, that.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mensaje, prioridad, marcaTiempo, categoria);
    }

    @Override
    public String toString() {
        return "NotificacionVoz{" +
                "mensaje='" + mensaje + '\'' +
                ", prioridad=" + prioridad +
                ", categoria='" + categoria + '\'' +
                ", marcaTiempo=" + marcaTiempo +
                '}';
    }

    /**
     * Constructor para crear notificaciones de voz
     */
    public static class Constructor {
        private String mensaje;
        private Prioridad prioridad = Prioridad.NORMAL;
        private long marcaTiempo = System.currentTimeMillis();
        private String categoria;
        private Object metadatos;

        /**
         * Establece el mensaje que se reproducirá por voz
         * @param mensaje Texto a reproducir
         */
        public Constructor establecerMensaje(@NonNull String mensaje) {
            this.mensaje = mensaje;
            return this;
        }

        /**
         * Establece la prioridad de la notificación
         * @param prioridad Nivel de prioridad
         */
        public Constructor establecerPrioridad(@NonNull Prioridad prioridad) {
            this.prioridad = prioridad;
            return this;
        }

        /**
         * Establece el timestamp de la notificación
         * @param marcaTiempo Timestamp en milisegundos
         */
        public Constructor establecerMarcaTiempo(long marcaTiempo) {
            this.marcaTiempo = marcaTiempo;
            return this;
        }

        /**
         * Establece una categoría opcional para agrupar notificaciones
         * Útil para throttling o filtrado por tipo
         * @param categoria Identificador de categoría (ej: "alerta", "recordatorio", "info")
         */
        public Constructor establecerCategoria(@Nullable String categoria) {
            this.categoria = categoria;
            return this;
        }

        /**
         * Establece metadatos adicionales opcionales
         * @param metadatos Cualquier objeto con información adicional
         */
        public Constructor establecerMetadatos(@Nullable Object metadatos) {
            this.metadatos = metadatos;
            return this;
        }

        /**
         * Construye la notificación
         * @return NotificacionVoz configurada
         * @throws IllegalStateException si falta el mensaje
         */
        public NotificacionVoz construir() {
            if (mensaje == null || mensaje.trim().isEmpty()) {
                throw new IllegalStateException("El mensaje es requerido");
            }
            return new NotificacionVoz(this);
        }
    }

    /**
     * Niveles de prioridad para las notificaciones
     */
    public enum Prioridad {
        BAJA(0),
        NORMAL(1),
        ALTA(2),
        URGENTE(3);

        private final int nivel;

        Prioridad(int nivel) {
            this.nivel = nivel;
        }

        public int obtenerNivel() {
            return nivel;
        }
    }
}
