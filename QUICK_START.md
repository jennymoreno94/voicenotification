# 🚀 Quick Start Guide

Guía rápida para comenzar a usar la librería en **5 minutos**.

## ⚡ Instalación Rápida

### 1. Agregar la Librería

**settings.gradle**
```gradle
include ':voicenotifications'
```

**app/build.gradle**
```gradle
dependencies {
    implementation project(':voicenotifications')
}
```

### 2. Sync Gradle
```bash
./gradlew sync
```

## 📱 Código Mínimo

### En tu Activity/Fragment

```java
public class MainActivity extends AppCompatActivity {
    
    private VoiceNotificationManager voiceManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 1. Obtener instancia
        voiceManager = VoiceNotificationManager.getInstance(this);
        
        // 2. Registrar lifecycle (IMPORTANTE)
        getLifecycle().addObserver(voiceManager);
        
        // 3. Usar
        voiceManager.speak(NotificationType.SPEED_EXCESS);
    }
}
```

## 🎯 Casos de Uso Comunes

### Exceso de Velocidad

```java
// Con datos dinámicos
voiceManager.speakSpeedExcess(120, 80); // velocidad actual, límite

// Mensaje predefinido
voiceManager.speak(NotificationType.SPEED_EXCESS);
```

### Frenada Brusca

```java
voiceManager.speak(NotificationType.HARSH_BRAKING);
```

### Aceleración Brusca

```java
voiceManager.speak(NotificationType.HARSH_ACCELERATION);
```

### Mensaje Personalizado

```java
VoiceNotification notification = new VoiceNotification.Builder()
        .setType(NotificationType.CUSTOM)
        .setMessage("Tu mensaje aquí")
        .setPriority(VoiceNotification.Priority.NORMAL)
        .build();

voiceManager.speak(notification);
```

## 🔧 Configuración Básica

```java
VoiceConfig config = VoiceConfig.builder()
        .setLocale(new Locale("es", "ES"))  // Español
        .setSpeechRate(1.0f)                 // Velocidad normal
        .setPitch(1.0f)                      // Tono normal
        .setEnabled(true)                    // Habilitado
        .build();

voiceManager.configure(config);
```

## 🛡️ Prevenir Spam (Throttling)

```java
NotificationThrottler throttler = new NotificationThrottler(30000); // 30 seg

if (throttler.tryNotify(NotificationType.SPEED_EXCESS)) {
    voiceManager.speak(NotificationType.SPEED_EXCESS);
}
```

## 📊 Observar Eventos

```java
voiceManager.getEvents().observe(this, event -> {
    switch (event.getType()) {
        case STARTED:
            // Notificación iniciada
            break;
        case COMPLETED:
            // Notificación completada
            break;
        case ERROR:
            // Error
            Toast.makeText(this, "Error: " + event.getMessage(), 
                Toast.LENGTH_SHORT).show();
            break;
    }
});
```

## 🚗 Integración con Sensores

```java
public class DrivingMonitor {
    
    private VoiceNotificationManager voiceManager;
    private DriverBehaviorAnalyzer analyzer;
    private NotificationThrottler throttler;
    
    public DrivingMonitor(Context context) {
        voiceManager = VoiceNotificationManager.getInstance(context);
        analyzer = new DriverBehaviorAnalyzer();
        throttler = new NotificationThrottler();
    }
    
    public void onSpeedChanged(int currentSpeed, int speedLimit) {
        if (analyzer.isSpeedExcess(currentSpeed, speedLimit)) {
            if (throttler.tryNotify(NotificationType.SPEED_EXCESS)) {
                voiceManager.speakSpeedExcess(currentSpeed, speedLimit);
            }
        }
    }
    
    public void onAccelerationChanged(float acceleration) {
        if (analyzer.isHarshBraking(acceleration)) {
            if (throttler.tryNotify(NotificationType.HARSH_BRAKING)) {
                voiceManager.speak(NotificationType.HARSH_BRAKING);
            }
        }
    }
}
```

## ⏱️ Notificaciones Programadas

```java
VoiceNotificationScheduler scheduler = 
    new VoiceNotificationScheduler(context);

VoiceNotification notification = new VoiceNotification.Builder()
        .setType(NotificationType.CUSTOM)
        .setMessage("Recuerde tomar un descanso")
        .build();

// Programar para dentro de 2 horas
scheduler.scheduleNotification(
    notification, 
    TimeUnit.HOURS.toMillis(2)
);
```

## 🎮 Probar con la App Demo

1. Abre el proyecto en Android Studio
2. Ejecuta el módulo `app`
3. Prueba los diferentes tipos de notificaciones
4. Observa el comportamiento del throttling

## 📋 Checklist de Integración

- [ ] Agregar dependencia en `build.gradle`
- [ ] Sync Gradle
- [ ] Obtener instancia de `VoiceNotificationManager`
- [ ] Registrar `LifecycleObserver`
- [ ] Configurar voz (opcional)
- [ ] Implementar throttling (recomendado)
- [ ] Observar eventos (opcional)
- [ ] Probar en dispositivo real

## 🆘 Problemas Comunes

### TTS no funciona
```java
if (!voiceManager.isAvailable()) {
    // TTS no disponible, instalar datos
    Intent intent = new Intent();
    intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
    startActivity(intent);
}
```

### Memory Leak
```java
// ✅ SIEMPRE registrar lifecycle
getLifecycle().addObserver(voiceManager);

// ✅ Usar Application Context
VoiceNotificationManager.getInstance(getApplicationContext());
```

### Notificaciones se cortan
```java
// Usar prioridades más altas
VoiceNotification notification = new VoiceNotification.Builder()
        .setType(NotificationType.SPEED_EXCESS)
        .setMessage("Mensaje importante")
        .setPriority(VoiceNotification.Priority.HIGH) // ← Aquí
        .build();
```

## 📚 Siguiente Paso

Lee la documentación completa:
- [README.md](README.md) - Documentación completa
- [INTEGRATION_GUIDE.md](INTEGRATION_GUIDE.md) - Guía de integración avanzada
- [ARCHITECTURE.md](ARCHITECTURE.md) - Detalles de arquitectura

## 💡 Tips

1. **Siempre** registra el lifecycle observer
2. **Usa** throttling para evitar spam
3. **Configura** el idioma según preferencias del usuario
4. **Prueba** en dispositivos reales (TTS varía)
5. **Observa** eventos para debugging

---

¿Listo? ¡Comienza a usar la librería! 🚀
