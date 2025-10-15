package com.notificacionesvoz.utilidades;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

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
        String categoria = notificacion.obtenerCategoria() != null 
            ? notificacion.obtenerCategoria() 
            : "notificacion";
        
        Data datosEntrada = new Data.Builder()
                .putString(TrabajadorNotificacionesVoz.CLAVE_CATEGORIA, categoria)
                .putString(TrabajadorNotificacionesVoz.CLAVE_MENSAJE, notificacion.obtenerMensaje())
                .putString(TrabajadorNotificacionesVoz.CLAVE_PRIORIDAD, notificacion.obtenerPrioridad().name())
                .build();

        OneTimeWorkRequest solicitudTrabajo = new OneTimeWorkRequest.Builder(TrabajadorNotificacionesVoz.class)
                .setInputData(datosEntrada)
                .setInitialDelay(retrasoMs, TimeUnit.MILLISECONDS)
                .addTag(categoria)
                .build();

        gestorTrabajos.enqueue(solicitudTrabajo);
    }

    /**
     * Cancela todas las notificaciones programadas de una categoría específica
     * @param categoria Categoría de las notificaciones a cancelar
     */
    public void cancelarNotificaciones(@NonNull String categoria) {
        gestorTrabajos.cancelAllWorkByTag(categoria);
    }

    /**
     * Cancela todas las notificaciones programadas
     */
    public void cancelarTodasLasNotificaciones() {
        gestorTrabajos.cancelAllWork();
    }
}
