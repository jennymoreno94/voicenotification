package com.notificacionesvoz.presentacion;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.notificacionesvoz.dominio.modelo.NotificacionVoz;

/**
 * Trabajador para procesar notificaciones de voz en background
 * Útil para notificaciones programadas o diferidas
 */
public class TrabajadorNotificacionesVoz extends Worker {
    
    public static final String CLAVE_CATEGORIA = "categoria";
    public static final String CLAVE_MENSAJE = "mensaje";
    public static final String CLAVE_PRIORIDAD = "prioridad";

    public TrabajadorNotificacionesVoz(@NonNull Context contexto, @NonNull WorkerParameters parametros) {
        super(contexto, parametros);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            String categoria = getInputData().getString(CLAVE_CATEGORIA);
            String mensaje = getInputData().getString(CLAVE_MENSAJE);
            String prioridadTexto = getInputData().getString(CLAVE_PRIORIDAD);
            
            if (mensaje == null) {
                return Result.failure();
            }
            
            NotificacionVoz.Prioridad prioridad = prioridadTexto != null 
                ? NotificacionVoz.Prioridad.valueOf(prioridadTexto)
                : NotificacionVoz.Prioridad.NORMAL;
            
            NotificacionVoz notificacion = new NotificacionVoz.Constructor()
                    .establecerMensaje(mensaje)
                    .establecerCategoria(categoria)
                    .establecerPrioridad(prioridad)
                    .construir();
            
            GestorNotificacionesVoz gestor = GestorNotificacionesVoz.obtenerInstancia(getApplicationContext());
            gestor.reproducir(notificacion);
            
            return Result.success();
        } catch (Exception excepcion) {
            return Result.failure();
        }
    }
}
