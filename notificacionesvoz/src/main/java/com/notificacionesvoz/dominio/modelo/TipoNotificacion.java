package com.notificacionesvoz.dominio.modelo;

/**
 * Tipos de notificaciones para conductores
 */
public enum TipoNotificacion {
    EXCESO_VELOCIDAD("Exceso de velocidad"),
    FRENADA_BRUSCA("Frenada brusca"),
    ACELERACION_BRUSCA("Aceleración brusca"),
    GIRO_BRUSCO("Giro brusco"),
    PERSONALIZADA("Notificación personalizada");

    private final String descripcion;

    TipoNotificacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String obtenerDescripcion() {
        return descripcion;
    }
}
