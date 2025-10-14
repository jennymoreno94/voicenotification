# 🔄 Uso Transversal de la Librería

Esta guía explica cómo usar la librería como componente transversal en múltiples aplicaciones.

## 🎯 Objetivo

Usar la misma librería de notificaciones de voz en **todas tus aplicaciones de gestión de conductores** sin duplicar código.

## 📦 Métodos de Distribución

### Método 1: Copiar Módulo (Más Control)

**Ventajas:**
- ✅ Control total del código
- ✅ Puedes modificar si es necesario
- ✅ No requiere configuración adicional

**Pasos:**

1. **Copiar módulo a tu proyecto:**
   ```
   Copiar: driver-voice-notifications\voicenotifications\
   Pegar en: tu-proyecto\voicenotifications\
   ```

2. **Configurar settings.gradle:**
   ```gradle
   include ':app'
   include ':voicenotifications'
   ```

3. **Agregar dependencia en app/build.gradle:**
   ```gradle
   dependencies {
       implementation project(':voicenotifications')
   }
   ```

4. **Sync y listo**

### Método 2: AAR File (Más Simple)

**Ventajas:**
- ✅ Un solo archivo
- ✅ Fácil de distribuir
- ✅ No se ve el código fuente

**Pasos:**

1. **Generar AAR (solo una vez):**
   ```bash
   cd C:\Users\jenny\CascadeProjects\driver-voice-notifications
   .\gradlew :voicenotifications:assembleRelease
   ```
   
   **Ubicación del AAR:**
   ```
   voicenotifications\build\outputs\aar\voicenotifications-release.aar
   ```

2. **En cada proyecto:**
   
   a. Crear carpeta `libs`:
   ```
   tu-proyecto\app\libs\
   ```
   
   b. Copiar AAR:
   ```
   Copiar: voicenotifications-release.aar
   Pegar en: tu-proyecto\app\libs\
   ```
   
   c. Configurar app/build.gradle:
   ```gradle
   dependencies {
       implementation files('libs/voicenotifications-release.aar')
       
       // Dependencias requeridas
       implementation 'androidx.core:core:1.12.0'
       implementation 'androidx.appcompat:appcompat:1.6.1'
       implementation 'androidx.lifecycle:lifecycle-runtime:2.6.2'
       implementation 'androidx.lifecycle:lifecycle-livedata:2.6.2'
       implementation 'androidx.lifecycle:lifecycle-viewmodel:2.6.2'
       implementation 'androidx.work:work-runtime:2.9.0'
   }
   ```

### Método 3: Maven Local (Para Desarrollo)

**Ventajas:**
- ✅ Versionado automático
- ✅ Fácil actualización
- ✅ Profesional

**Pasos:**

1. **Publicar en Maven Local (solo una vez):**
   ```bash
   cd C:\Users\jenny\CascadeProjects\driver-voice-notifications
   .\gradlew :voicenotifications:publishToMavenLocal
   ```

2. **En cada proyecto:**
   
   a. Configurar build.gradle (raíz):
   ```gradle
   allprojects {
       repositories {
           google()
           mavenCentral()
           mavenLocal()  // ← Agregar
       }
   }
   ```
   
   b. Agregar dependencia en app/build.gradle:
   ```gradle
   dependencies {
       implementation 'com.driver:voicenotifications:1.0.0'
   }
   ```

## 💼 Casos de Uso por Tipo de App

### App de Gestión de Flotas

**Características:**
- Monitoreo en tiempo real
- Alertas de velocidad
- Análisis de comportamiento

**Implementación:**
```java
public class FleetTrackingService extends Service {
    private VoiceNotificationManager voiceManager;
    private NotificationThrottler throttler;
    private DriverBehaviorAnalyzer analyzer;
    
    @Override
    public void onCreate() {
        super.onCreate();
        voiceManager = VoiceNotificationManager.getInstance(this);
        throttler = new NotificationThrottler(30000);
        analyzer = new DriverBehaviorAnalyzer();
        
        // Configuración estricta para flotas
        analyzer.setSpeedThreshold(5);
        analyzer.setHarshBrakingThreshold(-7.0f);
    }
    
    public void onGPSUpdate(Location location) {
        float speed = location.getSpeed() * 3.6f;
        int speedLimit = getSpeedLimit(location);
        
        if (analyzer.isSpeedExcess((int) speed, speedLimit)) {
            if (throttler.tryNotify(NotificationType.SPEED_EXCESS)) {
                voiceManager.speakSpeedExcess((int) speed, speedLimit);
            }
        }
    }
}
```

### App de Delivery

**Características:**
- Recordatorios de descanso
- Alertas de conducción
- Notificaciones de entrega

**Implementación:**
```java
public class DeliveryActivity extends AppCompatActivity {
    private VoiceNotificationManager voiceManager;
    private VoiceNotificationScheduler scheduler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        voiceManager = VoiceNotificationManager.getInstance(this);
        scheduler = new VoiceNotificationScheduler(this);
        getLifecycle().addObserver(voiceManager);
        
        // Programar recordatorios
        scheduleBreakReminders();
    }
    
    private void scheduleBreakReminders() {
        VoiceNotification reminder = new VoiceNotification.Builder()
                .setType(NotificationType.CUSTOM)
                .setMessage("Recuerde tomar un descanso")
                .build();
        
        scheduler.scheduleNotification(reminder, 
            TimeUnit.HOURS.toMillis(2));
    }
    
    private void onDeliveryComplete() {
        VoiceNotification notification = new VoiceNotification.Builder()
                .setType(NotificationType.CUSTOM)
                .setMessage("Entrega completada exitosamente")
                .setPriority(VoiceNotification.Priority.LOW)
                .build();
        
        voiceManager.speak(notification);
    }
}
```

### App de Transporte Público

**Características:**
- Anuncios de paradas
- Alertas de velocidad moderadas
- Notificaciones de ruta

**Implementación:**
```java
public class BusDriverAssistant {
    private VoiceNotificationManager voiceManager;
    private NotificationThrottler throttler;
    
    public BusDriverAssistant(Context context) {
        voiceManager = VoiceNotificationManager.getInstance(context);
        throttler = new NotificationThrottler(60000); // 1 minuto
        
        // Configuración para buses
        VoiceConfig config = VoiceConfig.builder()
                .setSpeechRate(0.9f)  // Más lento
                .setPitch(1.0f)
                .build();
        voiceManager.configure(config);
    }
    
    public void announceStop(String stopName) {
        VoiceNotification announcement = new VoiceNotification.Builder()
                .setType(NotificationType.CUSTOM)
                .setMessage("Próxima parada: " + stopName)
                .setPriority(VoiceNotification.Priority.LOW)
                .build();
        
        voiceManager.speak(announcement);
    }
    
    public void checkSpeed(int currentSpeed, int speedLimit) {
        if (currentSpeed > speedLimit + 10) {
            if (throttler.tryNotify(NotificationType.SPEED_EXCESS)) {
                voiceManager.speakSpeedExcess(currentSpeed, speedLimit);
            }
        }
    }
}
```

## 🔧 Configuración por Proyecto

### Configuración Global en Application

```java
public class MyApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Configurar una vez para toda la app
        VoiceNotificationManager manager = 
            VoiceNotificationManager.getInstance(this);
        
        VoiceConfig config = VoiceConfig.builder()
                .setLocale(getAppLocale())
                .setSpeechRate(getPreferredSpeechRate())
                .setEnabled(isVoiceNotificationsEnabled())
                .build();
        
        manager.configure(config);
    }
    
    private Locale getAppLocale() {
        // Obtener de preferencias del usuario
        return new Locale("es", "ES");
    }
    
    private float getPreferredSpeechRate() {
        // Obtener de preferencias del usuario
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        return prefs.getFloat("speech_rate", 1.0f);
    }
    
    private boolean isVoiceNotificationsEnabled() {
        // Obtener de preferencias del usuario
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        return prefs.getBoolean("voice_enabled", true);
    }
}
```

## 🎨 Personalización por App

### App 1: Voz Femenina, Rápida
```java
VoiceConfig config = VoiceConfig.builder()
        .setSpeechRate(1.2f)
        .setPitch(1.3f)  // Tono más alto
        .build();
```

### App 2: Voz Masculina, Normal
```java
VoiceConfig config = VoiceConfig.builder()
        .setSpeechRate(1.0f)
        .setPitch(0.8f)  // Tono más bajo
        .build();
```

### App 3: Voz Lenta para Mayor Claridad
```java
VoiceConfig config = VoiceConfig.builder()
        .setSpeechRate(0.85f)
        .setPitch(1.0f)
        .build();
```

## 📊 Análisis Personalizado por App

### App de Flotas: Estricto
```java
DriverBehaviorAnalyzer analyzer = new DriverBehaviorAnalyzer();
analyzer.setSpeedThreshold(5);           // Solo 5 km/h de tolerancia
analyzer.setHarshBrakingThreshold(-7.0f);
analyzer.setHarshAccelerationThreshold(3.5f);
```

### App de Delivery: Moderado
```java
DriverBehaviorAnalyzer analyzer = new DriverBehaviorAnalyzer();
analyzer.setSpeedThreshold(10);          // 10 km/h de tolerancia
analyzer.setHarshBrakingThreshold(-8.0f);
analyzer.setHarshAccelerationThreshold(4.0f);
```

### App de Transporte: Relajado
```java
DriverBehaviorAnalyzer analyzer = new DriverBehaviorAnalyzer();
analyzer.setSpeedThreshold(15);          // 15 km/h de tolerancia
analyzer.setHarshBrakingThreshold(-9.0f);
analyzer.setHarshAccelerationThreshold(5.0f);
```

## 🔄 Actualización de la Librería

### Si usas Módulo:
1. Actualiza el código en el proyecto fuente
2. Copia el módulo actualizado a cada proyecto
3. Sync Gradle

### Si usas AAR:
1. Genera nuevo AAR con nueva versión
2. Reemplaza en carpeta `libs` de cada proyecto
3. Sync Gradle

### Si usas Maven Local:
1. Actualiza versión en `voicenotifications/build.gradle`:
   ```gradle
   version = '1.1.0'  // Nueva versión
   ```
2. Publica: `.\gradlew publishToMavenLocal`
3. Actualiza en proyectos consumidores:
   ```gradle
   implementation 'com.driver:voicenotifications:1.1.0'
   ```

## 🧪 Testing en Múltiples Apps

Cada app puede tener sus propios tests:

```java
@Test
public void testFleetNotifications() {
    VoiceNotificationManager manager = 
        VoiceNotificationManager.getInstance(context);
    
    // Test específico para app de flotas
    manager.speakSpeedExcess(100, 80);
    assertTrue(manager.isSpeaking());
}
```

## 📝 Checklist de Integración

Para cada nueva app:

- [ ] Copiar módulo o AAR
- [ ] Configurar `settings.gradle` (si es módulo)
- [ ] Agregar dependencia en `build.gradle`
- [ ] Agregar dependencias requeridas (si es AAR)
- [ ] Configurar en `Application.onCreate()`
- [ ] Implementar en Activities/Services
- [ ] Configurar throttling según necesidad
- [ ] Configurar analyzer según tipo de app
- [ ] Testear en dispositivo real
- [ ] Documentar configuración específica

## 💡 Mejores Prácticas

1. **Mantén una versión fuente:** Un proyecto con el código original
2. **Versionado:** Usa versionado semántico (1.0.0, 1.1.0, etc.)
3. **Documentación:** Documenta cambios en CHANGELOG.md
4. **Testing:** Prueba en al menos una app antes de distribuir
5. **Configuración:** Usa configuración global en Application
6. **Personalización:** Personaliza por app según necesidades

## 🎯 Ventajas de Este Enfoque

✅ **Reutilización:** Mismo código en todas las apps
✅ **Mantenibilidad:** Un solo punto de actualización
✅ **Consistencia:** Comportamiento uniforme
✅ **Eficiencia:** No duplicar esfuerzo
✅ **Calidad:** Tests compartidos
✅ **Escalabilidad:** Fácil agregar nuevas apps

---

**La librería está lista para ser tu componente transversal en todos tus proyectos de gestión de conductores.**
