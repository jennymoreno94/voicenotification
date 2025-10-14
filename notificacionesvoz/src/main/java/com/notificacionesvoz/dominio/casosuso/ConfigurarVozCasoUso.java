package com.notificacionesvoz.dominio.casosuso;

import androidx.annotation.NonNull;

import com.notificacionesvoz.dominio.modelo.ConfiguracionVoz;
import com.notificacionesvoz.dominio.repositorio.RepositorioNotificacionesVoz;

/**
 * Caso de uso para configurar el motor de voz
 * Encapsula la lógica de negocio para la configuración
 */
public class ConfigurarVozCasoUso {
    
    private final RepositorioNotificacionesVoz repositorio;

    public ConfigurarVozCasoUso(@NonNull RepositorioNotificacionesVoz repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Ejecuta el caso de uso
     * @param configuracion La configuración a aplicar
     */
    public void ejecutar(@NonNull ConfiguracionVoz configuracion) {
        repositorio.configurar(configuracion);
    }
}
