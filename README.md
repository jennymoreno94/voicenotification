
# 🚗 Driver Voice Notifications Library

Librería Android profesional para notificaciones de voz dirigidas a conductores. Implementa arquitectura limpia, patrones modernos y está optimizada para Java 17.

## 📋 Características

- ✅ **Arquitectura Limpia** (Clean Architecture)
- ✅ **Java 17** compatible
- ✅ **AndroidX Lifecycle** aware
- ✅ **LiveData** para eventos reactivos
- ✅ **WorkManager** para notificaciones en background
- ✅ **Text-to-Speech** nativo de Android
- ✅ **Throttling** automático para evitar spam
- ✅ **Análisis de comportamiento** del conductor
- ✅ **Fácil integración** como librería
- ✅ **Thread-safe** y optimizada

## 🎯 Tipos de Notificaciones

1. **Exceso de Velocidad** - Alerta cuando se supera el límite
2. **Frenada Brusca** - Detecta frenados agresivos
3. **Aceleración Brusca** - Detecta aceleraciones agresivas
4. **Giro Brusco** - Detecta giros peligrosos
5. **Personalizada** - Mensajes custom

## 🚀 Instalación

### Opción 1: Módulo Local

```gradle
// settings.gradle
include ':voicenotifications'

// app/build.gradle
dependencies {
    implementation project(':voicenotifications')
}
```

### Opción 2: AAR File

```gradle
dependencies {
    implementation files('libs/voicenotifications-1.0.0.aar')
}
```

### Opción 3: Maven Local

```gradle
repositories {
    mavenLocal()
}

dependencies {
    implementation 'com.driver:voicenotifications:1.0.0'
}
```

## 📱 Uso Básico

### 1. Inicialización

```java
public class MainActivity extends AppCompatActivity {
    
    private VoiceNotificationManager voiceManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Obtener instancia
        voiceManager = VoiceNotificationManager.getInstance(this);
        
        // Registrar lifecycle observer (importante)
        getLifecycle().addObserver(voiceManager);
        
        // Configurar (opcional)
        VoiceConfig config = VoiceConfig.builder()
                .setLocale(new Locale("es", "ES"))
                .setSpeechRate(1.0f)
                .setPitch(1.0f)
                .setEnabled(true)
                .build();
        voiceManager.configure(config);
    }
}
```

### 2. Notificaciones Simples

```java
// Usar mensajes predefinidos
voiceManager.speak(NotificationType.HARSH_BRAKING);
voiceManager.speak(NotificationType.HARSH_ACCELERATION);

// Exceso de velocidad con datos
voiceManager.speakSpeedExcess(120, 80); // velocidad actual, límite
```

### 3. Notificaciones Personalizadas

```java
VoiceNotification notification = new VoiceNotification.Builder()
        .setType(NotificationType.CUSTOM)
        .setMessage("Atención, zona escolar adelante")
        .setPriority(VoiceNotification.Priority.HIGH)
        .build();

voiceManager.speak(notification);
```

### 4. Observar Eventos

```java
voiceManager.getEvents().observe(this, event -> {
    switch (event.getType()) {
        case STARTED:
            Log.d(TAG, "Notificación iniciada");
            break;
        case COMPLETED:
            Log.d(TAG, "Notificación completada");
            break;
        case ERROR:
            Log.e(TAG, "Error: " + event.getMessage());
            break;
    }
});
```

## 🔧 Características Avanzadas

### Throttling (Anti-spam)

```java
NotificationThrottler throttler = new NotificationThrottler(30000); // 30 segundos

if (throttler.tryNotify(NotificationType.SPEED_EXCESS)) {
    voiceManager.speak(NotificationType.SPEED_EXCESS);
} else {
    long remaining = throttler.getRemainingCooldown(NotificationType.SPEED_EXCESS);
    Log.d(TAG, "Esperar " + remaining + "ms");
}
```

### Análisis de Comportamiento

```java
DriverBehaviorAnalyzer analyzer = new DriverBehaviorAnalyzer();

// Configurar umbrales personalizados
analyzer.setSpeedThreshold(15); // km/h sobre el límite
analyzer.setHarshBrakingThreshold(-8.0f); // m/s²

// Analizar datos
if (analyzer.isSpeedExcess(currentSpeed, speedLimit)) {
    voiceManager.speakSpeedExcess(currentSpeed, speedLimit);
}

if (analyzer.isHarshBraking(acceleration)) {
    voiceManager.speak(NotificationType.HARSH_BRAKING);
}

// Análisis automático de aceleración
NotificationType type = analyzer.analyzeAcceleration(
    acceleration, 
    lateralAcceleration
);
if (type != NotificationType.CUSTOM) {
    voiceManager.speak(type);
}
```

### Notificaciones Programadas

```java
VoiceNotificationScheduler scheduler = new VoiceNotificationScheduler(context);

VoiceNotification notification = new VoiceNotification.Builder()
        .setType(NotificationType.CUSTOM)
        .setMessage("Recuerde tomar un descanso")
        .build();

// Programar para dentro de 2 horas
scheduler.scheduleNotification(notification, TimeUnit.HOURS.toMillis(2));

// Cancelar notificaciones programadas
scheduler.cancelNotifications(NotificationType.CUSTOM);
```

### Mensajes Multiidioma

```java
// Español (por defecto)
String message = NotificationMessageFactory.getSpanishMessage(
    NotificationType.SPEED_EXCESS
);

// Inglés
String message = NotificationMessageFactory.getEnglishMessage(
    NotificationType.SPEED_EXCESS
);

// Según locale
String message = NotificationMessageFactory.getMessage(
    NotificationType.SPEED_EXCESS,
    new Locale("es", "ES")
);

// Mensaje dinámico
String message = NotificationMessageFactory.getSpeedExcessMessage(120, 80);
```

## 🏗️ Arquitectura

```
voicenotifications/
├── domain/                 # Capa de dominio (lógica de negocio)
│   ├── model/             # Modelos de dominio
│   │   ├── NotificationType.java
│   │   ├── VoiceNotification.java
│   │   └── VoiceConfig.java
│   ├── repository/        # Interfaces de repositorio
│   │   └── VoiceNotificationRepository.java
│   └── usecase/           # Casos de uso
│       ├── SpeakNotificationUseCase.java
│       └── ConfigureVoiceUseCase.java
├── data/                  # Capa de datos
│   ├── repository/        # Implementaciones
│   │   └── VoiceNotificationRepositoryImpl.java
│   └── factory/           # Factories
│       └── NotificationMessageFactory.java
├── presentation/          # Capa de presentación
│   ├── VoiceNotificationManager.java
│   └── VoiceNotificationWorker.java
└── util/                  # Utilidades
    ├── NotificationThrottler.java
    ├── DriverBehaviorAnalyzer.java
    └── VoiceNotificationScheduler.java
```

## 🎨 Patrones y Principios

- **Clean Architecture** - Separación de capas
- **SOLID** - Principios de diseño
- **Repository Pattern** - Abstracción de datos
- **Use Case Pattern** - Lógica de negocio encapsulada
- **Builder Pattern** - Construcción de objetos
- **Singleton Pattern** - Instancia única del manager
- **Observer Pattern** - LiveData para eventos
- **Factory Pattern** - Creación de mensajes

## ⚙️ Configuración Avanzada

### Personalizar Voz

```java
VoiceConfig config = VoiceConfig.builder()
        .setLocale(new Locale("es", "MX"))  // Español de México
        .setSpeechRate(0.9f)                 // Más lento
        .setPitch(1.1f)                      // Tono más alto
        .setEnabled(true)
        .setQueueMode(TextToSpeech.QUEUE_ADD) // Encolar en vez de reemplazar
        .build();

voiceManager.configure(config);
```

### Control Manual

```java
// Verificar disponibilidad
if (voiceManager.isAvailable()) {
    voiceManager.speak(notification);
}

// Verificar si está hablando
if (voiceManager.isSpeaking()) {
    voiceManager.stop();
}

// Detener reproducción
voiceManager.stop();
```

## 📊 Testing

La librería incluye tests unitarios completos:

```bash
# Ejecutar tests
./gradlew :voicenotifications:test

# Con coverage
./gradlew :voicenotifications:testDebugUnitTest
```

Clases testeadas:
- `VoiceNotificationTest` - Modelos de dominio
- `NotificationThrottlerTest` - Lógica de throttling
- `DriverBehaviorAnalyzerTest` - Análisis de comportamiento

## 🔒 Permisos

La librería solo requiere:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

No requiere permisos peligrosos.

## 📦 Dependencias

- AndroidX Core 1.12.0
- AndroidX Lifecycle 2.6.2
- AndroidX WorkManager 2.9.0
- Material Components 1.11.0

## 🔄 Compatibilidad

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Java**: 17
- **Gradle**: 8.2.0

## 💡 Mejores Prácticas

### 1. Lifecycle Management

```java
// ✅ CORRECTO - Registrar lifecycle
getLifecycle().addObserver(voiceManager);

// ❌ INCORRECTO - No registrar lifecycle
// Puede causar memory leaks
```

### 2. Throttling

```java
// ✅ CORRECTO - Usar throttler
if (throttler.tryNotify(type)) {
    voiceManager.speak(type);
}

// ❌ INCORRECTO - Sin throttler
// Puede causar spam de notificaciones
```

### 3. Prioridades

```java
// ✅ CORRECTO - Usar prioridades apropiadas
VoiceNotification urgent = new VoiceNotification.Builder()
        .setType(NotificationType.SPEED_EXCESS)
        .setMessage("Peligro inminente")
        .setPriority(VoiceNotification.Priority.URGENT) // Interrumpe otras
        .build();

// ℹ️ NORMAL - Para notificaciones regulares
// ⚠️ HIGH - Para situaciones importantes
// 🚨 URGENT - Solo para emergencias
```

### 4. Context

```java
// ✅ CORRECTO - Usar Application Context
VoiceNotificationManager.getInstance(getApplicationContext());

// ⚠️ EVITAR - Activity Context
// Puede causar memory leaks si la activity se destruye
```

## 🐛 Troubleshooting

### TTS no inicializa

```java
if (!voiceManager.isAvailable()) {
    // Verificar que el dispositivo tenga TTS instalado
    Intent installIntent = new Intent();
    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
    startActivity(installIntent);
}
```

### Idioma no disponible

```java
VoiceConfig config = VoiceConfig.builder()
        .setLocale(Locale.getDefault()) // Usar idioma del sistema
        .build();
```

## 📄 Licencia

Esta librería es de código abierto y puede ser utilizada libremente en proyectos comerciales y no comerciales.

## 👥 Contribuir

Para contribuir al proyecto:
1. Fork el repositorio
2. Crea una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Crea un Pull Request

## 📞 Soporte

Para reportar bugs o solicitar features, abre un issue en el repositorio.

---

**Desarrollado con ❤️ para mejorar la seguridad vial**
