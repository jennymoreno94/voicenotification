package com.notificacionesvoz.datos.fabrica;

import androidx.annotation.NonNull;

import com.notificacionesvoz.dominio.modelo.TipoNotificacion;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Fábrica para generar mensajes de notificación predefinidos
 */
public class FabricaMensajesNotificacion {
    
    private static final Map<TipoNotificacion, String> MENSAJES_ESPANOL = new HashMap<>();
    private static final Map<TipoNotificacion, String> MENSAJES_INGLES = new HashMap<>();
    
    static {
        // Mensajes en español
        MENSAJES_ESPANOL.put(TipoNotificacion.EXCESO_VELOCIDAD, 
            "Atención, exceso de velocidad detectado. Reduzca la velocidad.");
        MENSAJES_ESPANOL.put(TipoNotificacion.FRENADA_BRUSCA, 
            "Frenada brusca detectada. Conduzca con precaución.");
        MENSAJES_ESPANOL.put(TipoNotificacion.ACELERACION_BRUSCA, 
            "Aceleración brusca detectada. Acelere gradualmente.");
        MENSAJES_ESPANOL.put(TipoNotificacion.GIRO_BRUSCO, 
            "Giro brusco detectado. Reduzca la velocidad en las curvas.");
        
        // Mensajes en inglés
        MENSAJES_INGLES.put(TipoNotificacion.EXCESO_VELOCIDAD, 
            "Attention, speed limit exceeded. Please reduce speed.");
        MENSAJES_INGLES.put(TipoNotificacion.FRENADA_BRUSCA, 
            "Harsh braking detected. Drive carefully.");
        MENSAJES_INGLES.put(TipoNotificacion.ACELERACION_BRUSCA, 
            "Harsh acceleration detected. Accelerate gradually.");
        MENSAJES_INGLES.put(TipoNotificacion.GIRO_BRUSCO, 
            "Sharp turn detected. Reduce speed on curves.");
    }

    /**
     * Obtiene un mensaje predefinido para un tipo de notificación
     * @param tipo Tipo de notificación
     * @param idioma Idioma del mensaje
     * @return Mensaje predefinido
     */
    @NonNull
    public static String obtenerMensaje(@NonNull TipoNotificacion tipo, @NonNull Locale idioma) {
        Map<TipoNotificacion, String> mensajes = esEspanol(idioma) 
            ? MENSAJES_ESPANOL 
            : MENSAJES_INGLES;
        
        String mensaje = mensajes.get(tipo);
        return mensaje != null ? mensaje : tipo.obtenerDescripcion();
    }

    /**
     * Obtiene un mensaje predefinido en español
     */
    @NonNull
    public static String obtenerMensajeEspanol(@NonNull TipoNotificacion tipo) {
        return obtenerMensaje(tipo, new Locale("es", "ES"));
    }

    /**
     * Obtiene un mensaje predefinido en inglés
     */
    @NonNull
    public static String obtenerMensajeIngles(@NonNull TipoNotificacion tipo) {
        return obtenerMensaje(tipo, Locale.ENGLISH);
    }

    /**
     * Genera un mensaje personalizado con datos dinámicos para exceso de velocidad
     */
    @NonNull
    public static String obtenerMensajeExcesoVelocidad(int velocidadActual, int limiteVelocidad) {
        return String.format(Locale.getDefault(),
            "Atención, está conduciendo a %d kilómetros por hora. El límite es %d.",
            velocidadActual, limiteVelocidad);
    }

    private static boolean esEspanol(Locale idioma) {
        return idioma.getLanguage().equals("es");
    }
}
