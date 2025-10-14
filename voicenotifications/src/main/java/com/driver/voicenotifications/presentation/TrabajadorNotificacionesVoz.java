package com.driver.voicenotifications.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.driver.voicenotifications.domain.model.TipoNotificacion;
import com.driver.voicenotifications.domain.model.NotificacionVoz;

/**
 * Trabajador para procesar notificaciones de voz en background
 * Útil para notificaciones programadas o diferidas
 */
public class TrabajadorNotificacionesVoz extends Worker {
    
    public static final String CLAVE_TIPO_NOTIFICACION = "tipo_notificacion";
    public static final String CLAVE_MENSAJE = "mensaje";
    public static final String CLAVE_PRIORIDAD = "prioridad";

    public TrabajadorNotificacionesVoz(@NonNull Context contexto, @NonNull WorkerParameters parametros) {
        super(contexto, parametros);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            String tipoTexto = getInputData().getString(CLAVE_TIPO_NOTIFICACION);
            String mensaje = getInputData().getString(CLAVE_MENSAJE);
            String prioridadTexto = getInputData().getString(CLAVE_PRIORIDAD);
            
            if (tipoTexto == null || mensaje == null) {
                return Result.failure();
            }
            
            TipoNotificacion tipo = TipoNotificacion.valueOf(tipoTexto);
            NotificacionVoz.Prioridad prioridad = prioridadTexto != null 
                ? NotificacionVoz.Prioridad.valueOf(prioridadTexto)
                : NotificacionVoz.Prioridad.NORMAL;
            
            NotificacionVoz notificacion = new NotificacionVoz.Constructor()
                    .establecerTipo(tipo)
                    .establecerMensaje(mensaje)
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
