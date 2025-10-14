package com.notificacionesvoz.utilidades;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.notificacionesvoz.dominio.modelo.TipoNotificacion;
import com.notificacionesvoz.dominio.modelo.NotificacionVoz;
import com.notificacionesvoz.presentacion.TrabajadorNotificacionesVoz;

import java.util.concurrent.TimeUnit;

/**
 * Utilidad para programar notificaciones de voz diferidas
 * Usa WorkManager para garantizar la entrega
 */
public class ProgramadorNotificacionesVoz {
    
    private final WorkManager gestorTrabajos;

    public ProgramadorNotificacionesVoz(@NonNull Context contexto) {
        this.gestorTrabajos = WorkManager.getInstance(contexto.getApplicationContext());
    }

    /**
     * Programa una notificación para ser reproducida después de un retraso
     */
    public void programarNotificacion(@NonNull NotificacionVoz notificacion, long retrasoMs) {
        Data datosEntrada = new Data.Builder()
                .putString(TrabajadorNotificacionesVoz.CLAVE_TIPO_NOTIFICACION, notificacion.obtenerTipo().name())
                .putString(TrabajadorNotificacionesVoz.CLAVE_MENSAJE, notificacion.obtenerMensaje())
                .putString(TrabajadorNotificacionesVoz.CLAVE_PRIORIDAD, notificacion.obtenerPrioridad().name())
                .build();

        OneTimeWorkRequest solicitudTrabajo = new OneTimeWorkRequest.Builder(TrabajadorNotificacionesVoz.class)
                .setInputData(datosEntrada)
                .setInitialDelay(retrasoMs, TimeUnit.MILLISECONDS)
                .addTag(notificacion.obtenerTipo().name())
                .build();

        gestorTrabajos.enqueue(solicitudTrabajo);
    }

    /**
     * Cancela todas las notificaciones programadas de un tipo específico
     */
    public void cancelarNotificaciones(@NonNull TipoNotificacion tipo) {
        gestorTrabajos.cancelAllWorkByTag(tipo.name());
    }

    /**
     * Cancela todas las notificaciones programadas
     */
    public void cancelarTodasLasNotificaciones() {
        gestorTrabajos.cancelAllWork();
    }
}
