# 📘 Guía de Integración

Esta guía te ayudará a integrar la librería **Driver Voice Notifications** en tus proyectos existentes.

## 🎯 Escenarios de Integración

### Escenario 1: App de Rastreo GPS

```java
public class TrackingService extends Service {
    
    private VoiceNotificationManager voiceManager;
    private NotificationThrottler throttler;
    private DriverBehaviorAnalyzer analyzer;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        voiceManager = VoiceNotificationManager.getInstance(this);
        throttler = new NotificationThrottler(30000); // 30 segundos
        analyzer = new DriverBehaviorAnalyzer();
    }
    
    @Override
    public void onLocationChanged(Location location) {
        float speed = location.getSpeed() * 3.6f; // m/s a km/h
        int speedLimit = getSpeedLimitForLocation(location);
        
        if (analyzer.isSpeedExcess((int) speed, speedLimit)) {
            if (throttler.tryNotify(NotificationType.SPEED_EXCESS)) {
                voiceManager.speakSpeedExcess((int) speed, speedLimit);
            }
        }
    }
}
```

### Escenario 2: App con Acelerómetro

```java
public class AccelerometerMonitor implements SensorEventListener {
    
    private VoiceNotificationManager voiceManager;
    private DriverBehaviorAnalyzer analyzer;
    private NotificationThrottler throttler;
    
    private float lastVelocity = 0;
    private long lastTime = 0;
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            
            // Calcular aceleración
            long currentTime = System.currentTimeMillis();
            float deltaTime = (currentTime - lastTime) / 1000.0f;
            
            if (deltaTime > 0) {
                float acceleration = (z - lastVelocity) / deltaTime;
                float lateralAcceleration = Math.abs(x);
                
                // Analizar comportamiento
                NotificationType type = analyzer.analyzeAcceleration(
                    acceleration, 
                    lateralAcceleration
                );
                
                if (type != NotificationType.CUSTOM) {
                    if (throttler.tryNotify(type)) {
                        voiceManager.speak(type);
                    }
                }
                
                lastVelocity = z;
            }
            
            lastTime = currentTime;
        }
    }
}
```

### Escenario 3: Integración con Firebase/Backend

```java
public class FirebaseNotificationHandler {
    
    private VoiceNotificationManager voiceManager;
    
    public void handleRemoteNotification(RemoteMessage message) {
        String type = message.getData().get("notification_type");
        String customMessage = message.getData().get("message");
        String priority = message.getData().get("priority");
        
        NotificationType notificationType = NotificationType.valueOf(type);
        VoiceNotification.Priority notificationPriority = 
            VoiceNotification.Priority.valueOf(priority);
        
        VoiceNotification notification = new VoiceNotification.Builder()
                .setType(notificationType)
                .setMessage(customMessage)
                .setPriority(notificationPriority)
                .build();
        
        voiceManager.speak(notification);
    }
}
```

### Escenario 4: App Multi-módulo

```
project/
├── app/                    # Módulo principal
├── tracking/               # Módulo de rastreo
├── analytics/              # Módulo de análisis
└── voicenotifications/     # Librería de notificaciones
```

**settings.gradle**
```gradle
include ':app'
include ':tracking'
include ':analytics'
include ':voicenotifications'
```

**tracking/build.gradle**
```gradle
dependencies {
    implementation project(':voicenotifications')
    // Otras dependencias del módulo
}
```

## 🔧 Configuración por Tipo de App

### App de Flota de Vehículos

```java
public class FleetApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Configuración global
        VoiceNotificationManager manager = 
            VoiceNotificationManager.getInstance(this);
        
        // Configuración para conductores profesionales
        VoiceConfig config = VoiceConfig.builder()
                .setLocale(new Locale("es", "ES"))
                .setSpeechRate(0.95f)  // Ligeramente más lento
                .setPitch(1.0f)
                .setEnabled(true)
                .build();
        
        manager.configure(config);
        
        // Configurar umbrales más estrictos
        DriverBehaviorAnalyzer analyzer = new DriverBehaviorAnalyzer();
        analyzer.setSpeedThreshold(5);  // Solo 5 km/h de tolerancia
        analyzer.setHarshBrakingThreshold(-7.0f);
        
        // Guardar en singleton o DI
        AppConfig.setAnalyzer(analyzer);
    }
}
```

### App de Delivery

```java
public class DeliveryDriverActivity extends AppCompatActivity {
    
    private VoiceNotificationManager voiceManager;
    private VoiceNotificationScheduler scheduler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        voiceManager = VoiceNotificationManager.getInstance(this);
        scheduler = new VoiceNotificationScheduler(this);
        getLifecycle().addObserver(voiceManager);
        
        // Programar recordatorios periódicos
        scheduleBreakReminders();
    }
    
    private void scheduleBreakReminders() {
        VoiceNotification breakReminder = new VoiceNotification.Builder()
                .setType(NotificationType.CUSTOM)
                .setMessage("Recuerde tomar un descanso cada 2 horas")
                .setPriority(VoiceNotification.Priority.NORMAL)
                .build();
        
        // Cada 2 horas
        scheduler.scheduleNotification(
            breakReminder, 
            TimeUnit.HOURS.toMillis(2)
        );
    }
    
    private void onDeliveryCompleted() {
        VoiceNotification congrats = new VoiceNotification.Builder()
                .setType(NotificationType.CUSTOM)
                .setMessage("Entrega completada exitosamente")
                .setPriority(VoiceNotification.Priority.LOW)
                .build();
        
        voiceManager.speak(congrats);
    }
}
```

### App de Transporte Público

```java
public class BusDriverAssistant {
    
    private VoiceNotificationManager voiceManager;
    private NotificationThrottler throttler;
    
    public BusDriverAssistant(Context context) {
        voiceManager = VoiceNotificationManager.getInstance(context);
        // Cooldown más largo para transporte público
        throttler = new NotificationThrottler(60000); // 1 minuto
    }
    
    public void checkSpeed(int currentSpeed, int speedLimit) {
        // Tolerancia mayor para buses
        if (currentSpeed > speedLimit + 5) {
            if (throttler.tryNotify(NotificationType.SPEED_EXCESS)) {
                String message = String.format(
                    "Velocidad actual %d kilómetros por hora. " +
                    "Límite de velocidad %d kilómetros por hora.",
                    currentSpeed, speedLimit
                );
                
                VoiceNotification notification = new VoiceNotification.Builder()
                        .setType(NotificationType.SPEED_EXCESS)
                        .setMessage(message)
                        .setPriority(VoiceNotification.Priority.NORMAL)
                        .build();
                
                voiceManager.speak(notification);
            }
        }
    }
    
    public void announceStop(String stopName) {
        VoiceNotification announcement = new VoiceNotification.Builder()
                .setType(NotificationType.CUSTOM)
                .setMessage("Próxima parada: " + stopName)
                .setPriority(VoiceNotification.Priority.LOW)
                .build();
        
        voiceManager.speak(announcement);
    }
}
```

## 🔌 Integración con Arquitecturas Existentes

### Con MVVM

```java
public class DrivingViewModel extends ViewModel {
    
    private final VoiceNotificationManager voiceManager;
    private final MutableLiveData<DrivingState> drivingState;
    
    public DrivingViewModel(Application application) {
        voiceManager = VoiceNotificationManager.getInstance(application);
        drivingState = new MutableLiveData<>();
    }
    
    public void onSpeedChanged(int speed, int limit) {
        if (speed > limit + 10) {
            voiceManager.speakSpeedExcess(speed, limit);
            drivingState.setValue(new DrivingState(true, "Exceso de velocidad"));
        }
    }
    
    public LiveData<NotificationEvent> getVoiceEvents() {
        return voiceManager.getEvents();
    }
}
```

### Con MVP

```java
public class DrivingPresenter {
    
    private DrivingView view;
    private VoiceNotificationManager voiceManager;
    
    public DrivingPresenter(DrivingView view, Context context) {
        this.view = view;
        this.voiceManager = VoiceNotificationManager.getInstance(context);
        
        // Observar eventos
        voiceManager.getEvents().observeForever(event -> {
            if (event.getType() == NotificationEvent.Type.ERROR) {
                view.showError(event.getMessage());
            }
        });
    }
    
    public void handleHarshBraking() {
        voiceManager.speak(NotificationType.HARSH_BRAKING);
        view.showWarning("Frenada brusca detectada");
    }
}
```

### Con Clean Architecture + Dagger/Hilt

```java
@Module
@InstallIn(SingletonComponent.class)
public class VoiceNotificationModule {
    
    @Provides
    @Singleton
    public VoiceNotificationManager provideVoiceManager(
            @ApplicationContext Context context
    ) {
        return VoiceNotificationManager.getInstance(context);
    }
    
    @Provides
    @Singleton
    public NotificationThrottler provideThrottler() {
        return new NotificationThrottler(30000);
    }
    
    @Provides
    @Singleton
    public DriverBehaviorAnalyzer provideAnalyzer() {
        return new DriverBehaviorAnalyzer();
    }
}

// Uso en ViewModel
@HiltViewModel
public class TrackingViewModel extends ViewModel {
    
    private final VoiceNotificationManager voiceManager;
    private final NotificationThrottler throttler;
    
    @Inject
    public TrackingViewModel(
            VoiceNotificationManager voiceManager,
            NotificationThrottler throttler
    ) {
        this.voiceManager = voiceManager;
        this.throttler = throttler;
    }
}
```

## 📱 Integración con Servicios

### Foreground Service

```java
public class DrivingForegroundService extends Service {
    
    private VoiceNotificationManager voiceManager;
    
    @Override
    public void onCreate() {
        super.onCreate();
        voiceManager = VoiceNotificationManager.getInstance(this);
        startForeground(1, createNotification());
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Procesar datos de sensores
        processSensorData();
        return START_STICKY;
    }
    
    private void processSensorData() {
        // Tu lógica aquí
        // Cuando detectes un evento:
        voiceManager.speak(NotificationType.HARSH_BRAKING);
    }
}
```

## 🌐 Integración con APIs REST

```java
public class ApiNotificationHandler {
    
    private VoiceNotificationManager voiceManager;
    
    public void handleApiResponse(NotificationResponse response) {
        NotificationType type = NotificationType.valueOf(
            response.getType()
        );
        
        VoiceNotification notification = new VoiceNotification.Builder()
                .setType(type)
                .setMessage(response.getMessage())
                .setPriority(mapPriority(response.getPriority()))
                .build();
        
        voiceManager.speak(notification);
    }
    
    private VoiceNotification.Priority mapPriority(String priority) {
        switch (priority.toUpperCase()) {
            case "LOW": return VoiceNotification.Priority.LOW;
            case "HIGH": return VoiceNotification.Priority.HIGH;
            case "URGENT": return VoiceNotification.Priority.URGENT;
            default: return VoiceNotification.Priority.NORMAL;
        }
    }
}
```

## 🧪 Testing en Integración

```java
@RunWith(AndroidJUnit4.class)
public class IntegrationTest {
    
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = 
        new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void testVoiceNotificationIntegration() {
        activityRule.getScenario().onActivity(activity -> {
            VoiceNotificationManager manager = 
                VoiceNotificationManager.getInstance(activity);
            
            assertTrue(manager.isAvailable());
            
            manager.speak(NotificationType.SPEED_EXCESS);
            
            // Verificar que está hablando
            SystemClock.sleep(500);
            assertTrue(manager.isSpeaking());
        });
    }
}
```

## 🔐 Consideraciones de Seguridad

### Permisos en Runtime

```java
// La librería NO requiere permisos peligrosos
// Solo INTERNET (para TTS data si es necesario)
```

### Ofuscación (ProGuard/R8)

```proguard
# Ya incluido en consumer-rules.pro
-keep public class com.driver.voicenotifications.** { public *; }
```

## 📊 Monitoreo y Analytics

```java
public class AnalyticsIntegration {
    
    private VoiceNotificationManager voiceManager;
    private FirebaseAnalytics analytics;
    
    public void setupAnalytics() {
        voiceManager.getEvents().observeForever(event -> {
            Bundle bundle = new Bundle();
            bundle.putString("event_type", event.getType().name());
            bundle.putString("message", event.getMessage());
            
            analytics.logEvent("voice_notification", bundle);
        });
    }
}
```

## 💡 Tips de Integración

1. **Inicializa temprano**: En `Application.onCreate()`
2. **Usa Application Context**: Evita memory leaks
3. **Registra Lifecycle**: Siempre en Activities/Fragments
4. **Implementa Throttling**: Evita spam de notificaciones
5. **Maneja Errores**: Observa eventos de error
6. **Testea en Dispositivos Reales**: TTS varía por dispositivo
7. **Configura Idioma**: Según preferencias del usuario
8. **Usa Prioridades**: Para control de interrupciones

---

¿Necesitas ayuda con tu integración específica? Abre un issue en el repositorio.
