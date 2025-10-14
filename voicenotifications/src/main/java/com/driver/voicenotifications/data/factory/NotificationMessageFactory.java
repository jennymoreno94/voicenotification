package com.driver.voicenotifications.data.factory;

import androidx.annotation.NonNull;

import com.driver.voicenotifications.domain.model.NotificationType;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Factory para generar mensajes de notificación predefinidos
 */
public class NotificationMessageFactory {
    
    private static final Map<NotificationType, String> SPANISH_MESSAGES = new HashMap<>();
    private static final Map<NotificationType, String> ENGLISH_MESSAGES = new HashMap<>();
    
    static {
        // Mensajes en español
        SPANISH_MESSAGES.put(NotificationType.SPEED_EXCESS, 
            "Atención, exceso de velocidad detectado. Reduzca la velocidad.");
        SPANISH_MESSAGES.put(NotificationType.HARSH_BRAKING, 
            "Frenada brusca detectada. Conduzca con precaución.");
        SPANISH_MESSAGES.put(NotificationType.HARSH_ACCELERATION, 
            "Aceleración brusca detectada. Acelere gradualmente.");
        SPANISH_MESSAGES.put(NotificationType.SHARP_TURN, 
            "Giro brusco detectado. Reduzca la velocidad en las curvas.");
        
        // Mensajes en inglés
        ENGLISH_MESSAGES.put(NotificationType.SPEED_EXCESS, 
            "Attention, speed limit exceeded. Please reduce speed.");
        ENGLISH_MESSAGES.put(NotificationType.HARSH_BRAKING, 
            "Harsh braking detected. Drive carefully.");
        ENGLISH_MESSAGES.put(NotificationType.HARSH_ACCELERATION, 
            "Harsh acceleration detected. Accelerate gradually.");
        ENGLISH_MESSAGES.put(NotificationType.SHARP_TURN, 
            "Sharp turn detected. Reduce speed on curves.");
    }

    /**
     * Obtiene un mensaje predefinido para un tipo de notificación
     * @param type Tipo de notificación
     * @param locale Idioma del mensaje
     * @return Mensaje predefinido
     */
    @NonNull
    public static String getMessage(@NonNull NotificationType type, @NonNull Locale locale) {
        Map<NotificationType, String> messages = isSpanish(locale) 
            ? SPANISH_MESSAGES 
            : ENGLISH_MESSAGES;
        
        String message = messages.get(type);
        return message != null ? message : type.getDescription();
    }

    /**
     * Obtiene un mensaje predefinido en español
     */
    @NonNull
    public static String getSpanishMessage(@NonNull NotificationType type) {
        return getMessage(type, new Locale("es", "ES"));
    }

    /**
     * Obtiene un mensaje predefinido en inglés
     */
    @NonNull
    public static String getEnglishMessage(@NonNull NotificationType type) {
        return getMessage(type, Locale.ENGLISH);
    }

    /**
     * Genera un mensaje personalizado con datos dinámicos
     */
    @NonNull
    public static String getSpeedExcessMessage(int currentSpeed, int speedLimit) {
        return String.format(Locale.getDefault(),
            "Atención, está conduciendo a %d kilómetros por hora. El límite es %d.",
            currentSpeed, speedLimit);
    }

    private static boolean isSpanish(Locale locale) {
        return locale.getLanguage().equals("es");
    }
}
