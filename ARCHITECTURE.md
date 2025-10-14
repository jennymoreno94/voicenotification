# 🏗️ Arquitectura de la Librería

## Visión General

Esta librería implementa **Clean Architecture** con separación clara de responsabilidades y principios SOLID.

## 📐 Capas de la Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION                          │
│  (VoiceNotificationManager, Workers, UI Components)     │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                      DOMAIN                              │
│     (Use Cases, Models, Repository Interfaces)          │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                       DATA                               │
│  (Repository Implementations, TTS, Factories)            │
└─────────────────────────────────────────────────────────┘
```

## 🎯 Capa de Dominio (Domain Layer)

### Responsabilidad
Contiene la lógica de negocio pura, independiente de frameworks y tecnologías.

### Componentes

#### 1. Models
```java
// NotificationType.java
public enum NotificationType {
    SPEED_EXCESS,
    HARSH_BRAKING,
    HARSH_ACCELERATION,
    SHARP_TURN,
    CUSTOM
}
```

**Características:**
- Inmutables
- Sin dependencias de Android
- Validación en construcción
- Builder pattern para flexibilidad

#### 2. Repository Interfaces
```java
public interface VoiceNotificationRepository {
    void speak(VoiceNotification notification);
    void stop();
    boolean isSpeaking();
    void configure(VoiceConfig config);
    boolean isAvailable();
    void shutdown();
}
```

**Principios:**
- Dependency Inversion (SOLID)
- Abstracción de implementación
- Testeable con mocks

#### 3. Use Cases
```java
public class SpeakNotificationUseCase {
    private final VoiceNotificationRepository repository;
    
    public void execute(VoiceNotification notification) {
        // Lógica de negocio
        if (notification.getPriority().getLevel() >= HIGH) {
            repository.stop(); // Interrumpir si es alta prioridad
        }
        repository.speak(notification);
    }
}
```

**Ventajas:**
- Lógica de negocio encapsulada
- Reutilizable
- Fácil de testear
- Single Responsibility

## 💾 Capa de Datos (Data Layer)

### Responsabilidad
Implementa las interfaces del dominio usando tecnologías específicas.

### Componentes

#### 1. Repository Implementation
```java
public class VoiceNotificationRepositoryImpl 
    implements VoiceNotificationRepository {
    
    private TextToSpeech textToSpeech;
    
    @Override
    public void speak(VoiceNotification notification) {
        // Implementación con Android TTS
        textToSpeech.speak(
            notification.getMessage(),
            queueMode,
            params
        );
    }
}
```

**Características:**
- Implementa interfaces del dominio
- Maneja detalles técnicos (TTS)
- Gestión de recursos
- Callbacks y listeners

#### 2. Factories
```java
public class NotificationMessageFactory {
    public static String getMessage(
        NotificationType type, 
        Locale locale
    ) {
        // Lógica de creación de mensajes
    }
}
```

**Propósito:**
- Creación de objetos complejos
- Mensajes predefinidos
- Internacionalización

## 🎨 Capa de Presentación (Presentation Layer)

### Responsabilidad
Expone la funcionalidad a los consumidores de la librería.

### Componentes

#### 1. Manager (Facade Pattern)
```java
public class VoiceNotificationManager 
    implements DefaultLifecycleObserver {
    
    private final SpeakNotificationUseCase speakUseCase;
    private final MutableLiveData<NotificationEvent> eventLiveData;
    
    public void speak(VoiceNotification notification) {
        speakUseCase.execute(notification);
    }
    
    public LiveData<NotificationEvent> getEvents() {
        return eventLiveData;
    }
}
```

**Características:**
- Singleton thread-safe
- Lifecycle-aware
- API simple y clara
- Observable events (LiveData)

#### 2. Workers
```java
public class VoiceNotificationWorker extends Worker {
    @Override
    public Result doWork() {
        // Procesar notificaciones en background
        return Result.success();
    }
}
```

**Propósito:**
- Notificaciones en background
- Garantía de entrega
- Programación diferida

## 🔧 Capa de Utilidades (Utils)

### Componentes Auxiliares

#### 1. NotificationThrottler
```java
public class NotificationThrottler {
    private Map<NotificationType, Long> lastNotificationTime;
    
    public boolean tryNotify(NotificationType type) {
        // Lógica de cooldown
    }
}
```

**Propósito:** Prevenir spam de notificaciones

#### 2. DriverBehaviorAnalyzer
```java
public class DriverBehaviorAnalyzer {
    public boolean isSpeedExcess(int current, int limit);
    public boolean isHarshBraking(float acceleration);
    public NotificationType analyzeAcceleration(...);
}
```

**Propósito:** Análisis de datos de conducción

#### 3. VoiceNotificationScheduler
```java
public class VoiceNotificationScheduler {
    private WorkManager workManager;
    
    public void scheduleNotification(
        VoiceNotification notification, 
        long delayMillis
    );
}
```

**Propósito:** Programación de notificaciones

## 🔄 Flujo de Datos

### Flujo Normal
```
Usuario → Manager → UseCase → Repository → TTS
                                    ↓
                              LiveData Event
                                    ↓
                              Observer (UI)
```

### Flujo con Throttling
```
Sensor Data → Analyzer → Throttler → Manager → UseCase
                            ↓
                        (blocked)
                            ↓
                        No Action
```

### Flujo Programado
```
Scheduler → WorkManager → Worker → Manager → UseCase
                                                ↓
                                          Repository
```

## 🎯 Patrones de Diseño Implementados

### 1. Repository Pattern
**Ubicación:** `domain/repository/` y `data/repository/`

**Beneficio:** Abstracción de la fuente de datos

### 2. Use Case Pattern
**Ubicación:** `domain/usecase/`

**Beneficio:** Encapsulación de lógica de negocio

### 3. Builder Pattern
**Ubicación:** `VoiceNotification`, `VoiceConfig`

**Beneficio:** Construcción flexible de objetos

### 4. Singleton Pattern
**Ubicación:** `VoiceNotificationManager`

**Beneficio:** Instancia única, thread-safe

### 5. Observer Pattern
**Ubicación:** `LiveData` en Manager

**Beneficio:** Notificación reactiva de eventos

### 6. Factory Pattern
**Ubicación:** `NotificationMessageFactory`

**Beneficio:** Creación centralizada de mensajes

### 7. Facade Pattern
**Ubicación:** `VoiceNotificationManager`

**Beneficio:** API simple sobre sistema complejo

### 8. Strategy Pattern
**Ubicación:** `DriverBehaviorAnalyzer`

**Beneficio:** Algoritmos intercambiables

## 📋 Principios SOLID

### Single Responsibility
- Cada clase tiene una única responsabilidad
- `SpeakNotificationUseCase` solo maneja la lógica de hablar
- `NotificationThrottler` solo maneja cooldowns

### Open/Closed
- Extensible mediante herencia e interfaces
- Cerrado a modificación de código existente
- Nuevos tipos de notificaciones sin cambiar código

### Liskov Substitution
- Implementaciones de `VoiceNotificationRepository` son intercambiables
- Mocks en tests sustituyen implementaciones reales

### Interface Segregation
- Interfaces específicas y pequeñas
- `VoiceNotificationRepository` solo métodos necesarios
- No interfaces "gordas"

### Dependency Inversion
- Dependencias apuntan hacia abstracciones
- Use Cases dependen de interfaces, no implementaciones
- Inyección de dependencias en constructores

## 🧪 Testabilidad

### Unit Tests
```java
@Test
public void testSpeakUseCase() {
    // Mock del repository
    VoiceNotificationRepository mockRepo = mock(...);
    
    // Use case con mock
    SpeakNotificationUseCase useCase = 
        new SpeakNotificationUseCase(mockRepo);
    
    // Test
    useCase.execute(notification);
    verify(mockRepo).speak(notification);
}
```

### Integration Tests
```java
@Test
public void testManagerIntegration() {
    VoiceNotificationManager manager = 
        VoiceNotificationManager.getInstance(context);
    
    manager.speak(NotificationType.SPEED_EXCESS);
    assertTrue(manager.isSpeaking());
}
```

## 🔐 Thread Safety

### Singleton Thread-Safe
```java
public static VoiceNotificationManager getInstance(Context context) {
    if (instance == null) {
        synchronized (VoiceNotificationManager.class) {
            if (instance == null) {
                instance = new VoiceNotificationManager(context);
            }
        }
    }
    return instance;
}
```

**Patrón:** Double-checked locking

### LiveData Thread-Safe
```java
eventLiveData.postValue(event); // Thread-safe
```

**Beneficio:** Actualización desde cualquier thread

## 📊 Gestión de Recursos

### Lifecycle Management
```java
@Override
public void onDestroy(LifecycleOwner owner) {
    repository.shutdown();
    owner.getLifecycle().removeObserver(this);
}
```

**Previene:** Memory leaks

### TTS Resource Management
```java
@Override
public void shutdown() {
    if (textToSpeech != null) {
        textToSpeech.stop();
        textToSpeech.shutdown();
    }
}
```

**Libera:** Recursos del sistema

## 🚀 Extensibilidad

### Agregar Nuevo Tipo de Notificación
```java
// 1. Agregar en enum
public enum NotificationType {
    // ...
    DROWSINESS_DETECTED  // Nuevo
}

// 2. Agregar mensaje en factory
SPANISH_MESSAGES.put(
    NotificationType.DROWSINESS_DETECTED,
    "Detectada somnolencia. Tome un descanso."
);

// 3. Usar normalmente
voiceManager.speak(NotificationType.DROWSINESS_DETECTED);
```

### Agregar Nueva Fuente de Datos
```java
// 1. Implementar interface
public class CloudTTSRepository 
    implements VoiceNotificationRepository {
    // Implementación con Google Cloud TTS
}

// 2. Inyectar en use case
SpeakNotificationUseCase useCase = 
    new SpeakNotificationUseCase(new CloudTTSRepository());
```

## 📈 Escalabilidad

### Horizontal
- Múltiples instancias en diferentes procesos
- WorkManager distribuye carga

### Vertical
- Optimizado para bajo consumo de recursos
- Lazy initialization
- Resource pooling

## 🔍 Monitoreo y Debugging

### Logging Strategy
```java
private static final String TAG = "VoiceNotificationRepo";
Log.d(TAG, "Speaking notification: " + message);
```

### Event Tracking
```java
voiceManager.getEvents().observe(this, event -> {
    analytics.logEvent("voice_notification", bundle);
});
```

## 💡 Mejores Prácticas Implementadas

1. ✅ **Inmutabilidad** - Modelos inmutables
2. ✅ **Null Safety** - Anotaciones `@NonNull`/`@Nullable`
3. ✅ **Resource Management** - Lifecycle-aware
4. ✅ **Error Handling** - Try-catch y callbacks
5. ✅ **Documentation** - JavaDoc completo
6. ✅ **Testing** - Unit tests incluidos
7. ✅ **Performance** - Lazy loading, caching
8. ✅ **Security** - No permisos peligrosos

## 🎓 Referencias

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Android Architecture Components](https://developer.android.com/topic/architecture)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
- [Design Patterns](https://refactoring.guru/design-patterns)

---

Esta arquitectura garantiza:
- ✅ Mantenibilidad a largo plazo
- ✅ Testabilidad completa
- ✅ Escalabilidad horizontal y vertical
- ✅ Bajo acoplamiento, alta cohesión
- ✅ Fácil integración en proyectos existentes
