# 🚗 Librería de Notificaciones de Voz para Conductores

> **Librería Android profesional con arquitectura limpia, Java 17 y stack tecnológico moderno**

## 📋 Resumen Ejecutivo

Esta librería proporciona un sistema completo de notificaciones de voz para aplicaciones de gestión de conductores. Implementa las mejores prácticas de desarrollo Android con arquitectura limpia, componentes modernos y total compatibilidad con Java 17.

### ✨ Características Principales

- ✅ **Arquitectura Limpia** - Separación de capas (Domain, Data, Presentation)
- ✅ **Java 17** - Totalmente compatible
- ✅ **AndroidX Lifecycle** - Gestión automática de recursos
- ✅ **LiveData** - Eventos reactivos y observables
- ✅ **WorkManager** - Notificaciones en background confiables
- ✅ **Text-to-Speech** - Motor nativo de Android
- ✅ **Anti-spam** - Sistema de throttling integrado
- ✅ **Multiidioma** - Español e Inglés incluidos
- ✅ **Tests Completos** - Cobertura de tests unitarios
- ✅ **Fácil Integración** - API simple y clara

## 🎯 Casos de Uso

### Para Aplicaciones de:
- 🚛 **Gestión de Flotas** - Monitoreo de conductores
- 🚕 **Transporte** - Uber, Cabify, apps similares
- 📦 **Delivery** - Rappi, Glovo, etc.
- 🚌 **Transporte Público** - Buses, taxis
- 🏍️ **Mensajería** - Motocicletas de entrega
- 📱 **Telemática** - Cualquier app de rastreo vehicular

## 🚀 Inicio Rápido (5 minutos)

### 1. Abrir en Android Studio

```
File → Open → C:\Users\jenny\CascadeProjects\driver-voice-notifications
```

### 2. Esperar Sincronización

Android Studio descargará automáticamente todas las dependencias.

### 3. Ejecutar App Demo

1. Conectar dispositivo o iniciar emulador
2. Click en **Run** (▶️)
3. Probar las notificaciones de voz

### 4. Integrar en tu Proyecto

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

**MainActivity.java**
```java
public class MainActivity extends AppCompatActivity {
    
    private VoiceNotificationManager voiceManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Inicializar
        voiceManager = VoiceNotificationManager.getInstance(this);
        getLifecycle().addObserver(voiceManager);
        
        // Usar
        voiceManager.speakSpeedExcess(120, 80);
    }
}
```

## 📱 Tipos de Notificaciones

### 1. Exceso de Velocidad
```java
// Con datos dinámicos
voiceManager.speakSpeedExcess(120, 80);
// Dice: "Atención, está conduciendo a 120 km/h. El límite es 80."

// Mensaje predefinido
voiceManager.speak(NotificationType.SPEED_EXCESS);
```

### 2. Frenada Brusca
```java
voiceManager.speak(NotificationType.HARSH_BRAKING);
// Dice: "Frenada brusca detectada. Conduzca con precaución."
```

### 3. Aceleración Brusca
```java
voiceManager.speak(NotificationType.HARSH_ACCELERATION);
// Dice: "Aceleración brusca detectada. Acelere gradualmente."
```

### 4. Giro Brusco
```java
voiceManager.speak(NotificationType.SHARP_TURN);
// Dice: "Giro brusco detectado. Reduzca la velocidad en las curvas."
```

### 5. Personalizada
```java
VoiceNotification notification = new VoiceNotification.Builder()
        .setType(NotificationType.CUSTOM)
        .setMessage("Tu mensaje personalizado aquí")
        .setPriority(VoiceNotification.Priority.HIGH)
        .build();

voiceManager.speak(notification);
```

## 🔧 Configuración

### Configurar Idioma y Voz

```java
VoiceConfig config = VoiceConfig.builder()
        .setLocale(new Locale("es", "MX"))  // Español de México
        .setSpeechRate(1.0f)                 // Velocidad normal
        .setPitch(1.0f)                      // Tono normal
        .setEnabled(true)                    // Habilitado
        .build();

voiceManager.configure(config);
```

### Prevenir Spam (Throttling)

```java
NotificationThrottler throttler = new NotificationThrottler(30000); // 30 segundos

if (throttler.tryNotify(NotificationType.SPEED_EXCESS)) {
    voiceManager.speak(NotificationType.SPEED_EXCESS);
} else {
    // Todavía en cooldown
    long remaining = throttler.getRemainingCooldown(NotificationType.SPEED_EXCESS);
    Log.d(TAG, "Esperar " + remaining + "ms");
}
```

### Observar Eventos

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

## 🏗️ Arquitectura

### Capas

```
┌─────────────────────────────────────┐
│      PRESENTATION LAYER             │
│  (Manager, Workers, UI)             │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│        DOMAIN LAYER                 │
│  (Use Cases, Models, Interfaces)    │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│         DATA LAYER                  │
│  (Repository Impl, TTS, Factory)    │
└─────────────────────────────────────┘
```

### Componentes Principales

- **VoiceNotificationManager** - Punto de entrada (Facade)
- **SpeakNotificationUseCase** - Lógica de negocio
- **VoiceNotificationRepository** - Abstracción de datos
- **NotificationThrottler** - Control de spam
- **DriverBehaviorAnalyzer** - Análisis de conducción
- **VoiceNotificationScheduler** - Programación de notificaciones

## 📊 Análisis de Comportamiento

```java
DriverBehaviorAnalyzer analyzer = new DriverBehaviorAnalyzer();

// Configurar umbrales personalizados
analyzer.setSpeedThreshold(10);           // km/h sobre el límite
analyzer.setHarshBrakingThreshold(-8.0f); // m/s²

// Analizar velocidad
if (analyzer.isSpeedExcess(currentSpeed, speedLimit)) {
    voiceManager.speakSpeedExcess(currentSpeed, speedLimit);
}

// Analizar aceleración
if (analyzer.isHarshBraking(acceleration)) {
    voiceManager.speak(NotificationType.HARSH_BRAKING);
}

// Análisis automático
NotificationType type = analyzer.analyzeAcceleration(
    acceleration, 
    lateralAcceleration
);
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

## 🧪 Testing

### Ejecutar Tests

```bash
# Todos los tests
.\gradlew :voicenotifications:test

# Ver reporte
voicenotifications\build\reports\tests\testDebugUnitTest\index.html
```

### Tests Incluidos

- ✅ `VoiceNotificationTest` - Modelos de dominio
- ✅ `NotificationThrottlerTest` - Lógica de throttling
- ✅ `DriverBehaviorAnalyzerTest` - Análisis de comportamiento

## 📦 Generar Librería (AAR)

```bash
# Generar AAR release
.\gradlew :voicenotifications:assembleRelease

# Ubicación
voicenotifications\build\outputs\aar\voicenotifications-release.aar
```

### Publicar en Maven Local

```bash
.\gradlew :voicenotifications:publishToMavenLocal
```

**Usar en otros proyectos:**
```gradle
repositories {
    mavenLocal()
}

dependencies {
    implementation 'com.driver:voicenotifications:1.0.0'
}
```

## 📚 Documentación

- **[SETUP.md](SETUP.md)** - Configuración inicial del proyecto
- **[QUICK_START.md](QUICK_START.md)** - Inicio rápido en 5 minutos
- **[INTEGRATION_GUIDE.md](INTEGRATION_GUIDE.md)** - Guía de integración detallada
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - Detalles de arquitectura
- **[BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md)** - Compilación y publicación
- **[CHANGELOG.md](CHANGELOG.md)** - Historial de cambios

## 🔒 Requisitos

### Software
- **JDK 17** o superior
- **Android Studio** Hedgehog (2023.1.1) o superior
- **Gradle** 8.2 (incluido)

### Android
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Compile SDK:** 34

### Permisos
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
*Solo para descargar datos de TTS si es necesario*

## 💡 Mejores Prácticas

### ✅ Hacer

```java
// Usar Application Context
VoiceNotificationManager.getInstance(getApplicationContext());

// Registrar Lifecycle
getLifecycle().addObserver(voiceManager);

// Implementar Throttling
if (throttler.tryNotify(type)) {
    voiceManager.speak(type);
}

// Usar prioridades apropiadas
.setPriority(VoiceNotification.Priority.HIGH)
```

### ❌ Evitar

```java
// NO usar Activity Context directamente (memory leak)
VoiceNotificationManager.getInstance(this);

// NO olvidar registrar lifecycle
// Puede causar memory leaks

// NO enviar notificaciones sin throttling
// Puede causar spam molesto
```

## 🎓 Ejemplos de Integración

### Con GPS/Location

```java
@Override
public void onLocationChanged(Location location) {
    float speed = location.getSpeed() * 3.6f; // m/s a km/h
    
    if (analyzer.isSpeedExcess((int) speed, speedLimit)) {
        if (throttler.tryNotify(NotificationType.SPEED_EXCESS)) {
            voiceManager.speakSpeedExcess((int) speed, speedLimit);
        }
    }
}
```

### Con Acelerómetro

```java
@Override
public void onSensorChanged(SensorEvent event) {
    float acceleration = calculateAcceleration(event);
    
    NotificationType type = analyzer.analyzeAcceleration(
        acceleration, 
        lateralAcceleration
    );
    
    if (type != NotificationType.CUSTOM) {
        if (throttler.tryNotify(type)) {
            voiceManager.speak(type);
        }
    }
}
```

### Con Firebase/Backend

```java
public void onMessageReceived(RemoteMessage message) {
    String type = message.getData().get("notification_type");
    String customMessage = message.getData().get("message");
    
    VoiceNotification notification = new VoiceNotification.Builder()
            .setType(NotificationType.valueOf(type))
            .setMessage(customMessage)
            .setPriority(VoiceNotification.Priority.HIGH)
            .build();
    
    voiceManager.speak(notification);
}
```

## 🌟 Ventajas Competitivas

### vs Implementación Manual
- ✅ Arquitectura probada y escalable
- ✅ Gestión automática de recursos
- ✅ Anti-spam incluido
- ✅ Tests unitarios
- ✅ Documentación completa

### vs Otras Librerías
- ✅ Específica para conductores
- ✅ Análisis de comportamiento incluido
- ✅ Java 17 compatible
- ✅ Sin dependencias externas pesadas
- ✅ Código limpio y mantenible

## 🤝 Contribuir

Esta librería es de código abierto. Para contribuir:

1. Fork el repositorio
2. Crea una rama: `git checkout -b feature/nueva-funcionalidad`
3. Commit: `git commit -m 'Agregar nueva funcionalidad'`
4. Push: `git push origin feature/nueva-funcionalidad`
5. Abre un Pull Request

## 📄 Licencia

MIT License - Ver [LICENSE](LICENSE) para más detalles.

## 📞 Soporte

Para reportar bugs o solicitar features:
- Abre un issue en el repositorio
- Consulta la documentación en `/docs`
- Revisa los ejemplos en la app demo

## 🎯 Roadmap Futuro

- [ ] Soporte para más idiomas (Portugués, Francés)
- [ ] Integración con Google Cloud TTS
- [ ] Análisis de fatiga del conductor
- [ ] Dashboard de estadísticas
- [ ] Modo offline con cache de mensajes
- [ ] Personalización de voces (masculina/femenina)

---

**Desarrollado con ❤️ para mejorar la seguridad vial**

*Versión 1.0.0 - Octubre 2025*
