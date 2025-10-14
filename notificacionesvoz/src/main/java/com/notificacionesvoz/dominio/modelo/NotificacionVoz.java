package com.notificacionesvoz.dominio.modelo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Modelo de dominio para una notificación de voz
 */
public class NotificacionVoz {
    
    private final TipoNotificacion tipo;
    private final String mensaje;
    private final Prioridad prioridad;
    private final long marcaTiempo;
    @Nullable
    private final Object metadatos;

    private NotificacionVoz(Constructor constructor) {
        this.tipo = constructor.tipo;
        this.mensaje = constructor.mensaje;
        this.prioridad = constructor.prioridad;
        this.marcaTiempo = constructor.marcaTiempo;
        this.metadatos = constructor.metadatos;
    }

    @NonNull
    public TipoNotificacion obtenerTipo() {
        return tipo;
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
    public Object obtenerMetadatos() {
        return metadatos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificacionVoz that = (NotificacionVoz) o;
        return marcaTiempo == that.marcaTiempo &&
                tipo == that.tipo &&
                Objects.equals(mensaje, that.mensaje) &&
                prioridad == that.prioridad;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, mensaje, prioridad, marcaTiempo);
    }

    @Override
    public String toString() {
        return "NotificacionVoz{" +
                "tipo=" + tipo +
                ", mensaje='" + mensaje + '\'' +
                ", prioridad=" + prioridad +
                ", marcaTiempo=" + marcaTiempo +
                '}';
    }

    /**
     * Constructor para crear notificaciones de voz
     */
    public static class Constructor {
        private TipoNotificacion tipo;
        private String mensaje;
        private Prioridad prioridad = Prioridad.NORMAL;
        private long marcaTiempo = System.currentTimeMillis();
        private Object metadatos;

        public Constructor establecerTipo(@NonNull TipoNotificacion tipo) {
            this.tipo = tipo;
            return this;
        }

        public Constructor establecerMensaje(@NonNull String mensaje) {
            this.mensaje = mensaje;
            return this;
        }

        public Constructor establecerPrioridad(@NonNull Prioridad prioridad) {
            this.prioridad = prioridad;
            return this;
        }

        public Constructor establecerMarcaTiempo(long marcaTiempo) {
            this.marcaTiempo = marcaTiempo;
            return this;
        }

        public Constructor establecerMetadatos(@Nullable Object metadatos) {
            this.metadatos = metadatos;
            return this;
        }

        public NotificacionVoz construir() {
            if (tipo == null) {
                throw new IllegalStateException("El tipo de notificación es requerido");
            }
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
