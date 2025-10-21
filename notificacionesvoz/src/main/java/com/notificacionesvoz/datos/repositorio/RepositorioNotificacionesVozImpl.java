package com.notificacionesvoz.datos.repositorio;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import androidx.annotation.NonNull;

import com.notificacionesvoz.dominio.modelo.ConfiguracionVoz;
import com.notificacionesvoz.dominio.modelo.NotificacionVoz;
import com.notificacionesvoz.dominio.repositorio.RepositorioNotificacionesVoz;

import java.util.HashMap;
import java.util.Locale;

/**
 * Implementación del repositorio usando TextToSpeech de Android
 * Gestiona la reproducción de notificaciones de voz
 */
public class RepositorioNotificacionesVozImpl implements RepositorioNotificacionesVoz {
    
    private static final String ETIQUETA = "RepoNotificacionesVoz";
    
    private TextToSpeech motorVoz;
    private ConfiguracionVoz configuracionActual;
    private boolean estaInicializado = false;
    private final Context contexto;
    private EscuchadorNotificacionesVoz escuchador;

    public RepositorioNotificacionesVozImpl(@NonNull Context contexto) {
        this.contexto = contexto.getApplicationContext();
        this.configuracionActual = ConfiguracionVoz.obtenerPredeterminada();
        inicializarTTS();
    }

    /**
     * Inicializa el motor de Text-to-Speech
     */
    private void inicializarTTS() {
        motorVoz = new TextToSpeech(contexto, estado -> {
            if (estado == TextToSpeech.SUCCESS) {
                int resultado = motorVoz.setLanguage(configuracionActual.obtenerIdioma());
                
                if (resultado == TextToSpeech.LANG_MISSING_DATA || 
                    resultado == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(ETIQUETA, "Idioma no soportado: " + configuracionActual.obtenerIdioma());
                    // Fallback al idioma por defecto
                    motorVoz.setLanguage(Locale.getDefault());
                }
                
                motorVoz.setPitch(configuracionActual.obtenerTonoVoz());
                motorVoz.setSpeechRate(configuracionActual.obtenerVelocidadVoz());
                
                motorVoz.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String idExpresion) {
                        Log.d(ETIQUETA, "Iniciando reproducción: " + idExpresion);
                        if (escuchador != null) {
                            escuchador.alIniciar(idExpresion);
                        }
                    }

                    @Override
                    public void onDone(String idExpresion) {
                        Log.d(ETIQUETA, "Reproducción finalizada: " + idExpresion);
                        if (escuchador != null) {
                            escuchador.alCompletar(idExpresion);
                        }
                    }

                    @Override
                    public void onError(String idExpresion) {
                        Log.e(ETIQUETA, "Error en reproducción: " + idExpresion);
                        if (escuchador != null) {
                            escuchador.alOcurrirError(idExpresion);
                        }
                    }
                });
                
                estaInicializado = true;
                Log.i(ETIQUETA, "TextToSpeech inicializado exitosamente");
            } else {
                Log.e(ETIQUETA, "Falló la inicialización de TextToSpeech");
                estaInicializado = false;
            }
        });
    }

    @Override
    public void reproducir(@NonNull NotificacionVoz notificacion) {
        if (!estaInicializado || motorVoz == null) {
            Log.w(ETIQUETA, "TTS no inicializado, no se puede reproducir");
            return;
        }

        if (!configuracionActual.estaHabilitado()) {
            Log.d(ETIQUETA, "Notificaciones de voz deshabilitadas");
            return;
        }

        // Generar ID único usando categoría (si existe) o timestamp
        String categoria = notificacion.obtenerCategoria() != null 
            ? notificacion.obtenerCategoria() 
            : "notificacion";
        String idExpresion = categoria + "_" + notificacion.obtenerMarcaTiempo();
        
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, idExpresion);
        
        int modoCola = notificacion.obtenerPrioridad().obtenerNivel() >= NotificacionVoz.Prioridad.ALTA.obtenerNivel() 
            ? TextToSpeech.QUEUE_FLUSH 
            : configuracionActual.obtenerModoCola();
        
        motorVoz.speak(notificacion.obtenerMensaje(), modoCola, parametros);
        
        Log.d(ETIQUETA, "Reproduciendo notificación: " + notificacion.obtenerMensaje());
    }

    @Override
    public void detener() {
        if (motorVoz != null && estaInicializado) {
            motorVoz.stop();
            Log.d(ETIQUETA, "Reproducción detenida");
        }
    }

    @Override
    public boolean estaReproduciendo() {
        return motorVoz != null && estaInicializado && motorVoz.isSpeaking();
    }

    @Override
    public void configurar(@NonNull ConfiguracionVoz configuracion) {
        this.configuracionActual = configuracion;
        
        if (motorVoz != null && estaInicializado) {
            motorVoz.setLanguage(configuracion.obtenerIdioma());
            motorVoz.setPitch(configuracion.obtenerTonoVoz());
            motorVoz.setSpeechRate(configuracion.obtenerVelocidadVoz());
            Log.i(ETIQUETA, "Configuración de voz actualizada");
        }
    }

    @Override
    public boolean estaDisponible() {
        return estaInicializado && motorVoz != null;
    }

    @Override
    public void finalizar() {
        if (motorVoz != null) {
            motorVoz.stop();
            motorVoz.shutdown();
            estaInicializado = false;
            Log.i(ETIQUETA, "TextToSpeech finalizado");
        }
    }

    /**
     * Establece un escuchador para eventos de TTS
     * @param escuchador El escuchador de eventos
     */
    public void establecerEscuchador(EscuchadorNotificacionesVoz escuchador) {
        this.escuchador = escuchador;
    }

    /**
     * Interface para escuchar eventos de las notificaciones de voz
     */
    public interface EscuchadorNotificacionesVoz {
        void alIniciar(String idExpresion);
        void alCompletar(String idExpresion);
        void alOcurrirError(String idExpresion);
    }
}
