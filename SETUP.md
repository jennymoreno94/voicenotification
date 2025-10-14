# 🔧 Setup del Proyecto

## ✅ Pasos para Ejecutar

### 1. Abrir en Android Studio

1. **Abrir Android Studio**
2. **File → Open**
3. Navegar a: `C:\Users\jenny\CascadeProjects\driver-voice-notifications`
4. Seleccionar la carpeta y hacer clic en **OK**

### 2. Esperar Sincronización

Android Studio automáticamente:
- ✅ Descargará Gradle wrapper
- ✅ Descargará dependencias
- ✅ Sincronizará el proyecto
- ✅ Indexará archivos

**Tiempo estimado:** 2-5 minutos (primera vez)

### 3. Verificar Configuración

**Verificar JDK:**
- `File → Project Structure → SDK Location`
- Debe estar configurado **JDK 17** o superior

**Verificar Android SDK:**
- `File → Settings → Appearance & Behavior → System Settings → Android SDK`
- Debe tener instalado **Android 14 (API 34)**

### 4. Compilar el Proyecto

**Opción A: Desde Android Studio**
- `Build → Make Project` (Ctrl+F9)

**Opción B: Desde Terminal en Android Studio**
```bash
# Windows
.\gradlew build

# Verificar solo la librería
.\gradlew :voicenotifications:build
```

### 5. Ejecutar la App Demo

1. **Conectar un dispositivo Android** o **iniciar un emulador**
2. Seleccionar configuración **app** en la barra superior
3. Click en el botón **Run** (▶️) o presionar **Shift+F10**

### 6. Probar la Librería

Una vez que la app se ejecute:
1. Presiona los botones de notificaciones
2. Escucha las notificaciones de voz
3. Prueba el botón "Simular Conducción"
4. Observa el throttling (cooldown de 10 segundos)

## 🧪 Ejecutar Tests

### Desde Android Studio

1. Click derecho en `voicenotifications/src/test/java`
2. Seleccionar **Run 'Tests in voicenotifications'**

### Desde Terminal

```bash
.\gradlew :voicenotifications:test
```

**Ver reporte:**
```
voicenotifications\build\reports\tests\testDebugUnitTest\index.html
```

## 📦 Generar AAR (Librería)

```bash
# Release AAR
.\gradlew :voicenotifications:assembleRelease

# Ubicación
voicenotifications\build\outputs\aar\voicenotifications-release.aar
```

## 🔍 Verificar Estructura

El proyecto debe verse así en Android Studio:

```
driver-voice-notifications
├── app                          # Módulo de demostración
│   ├── src/main/java/...       # Código de la app demo
│   └── build.gradle            # Configuración del módulo
├── voicenotifications          # Módulo de la librería
│   ├── src/main/java/...       # Código de la librería
│   ├── src/test/java/...       # Tests unitarios
│   └── build.gradle            # Configuración de la librería
├── build.gradle                # Configuración raíz
├── settings.gradle             # Módulos del proyecto
└── gradle.properties           # Propiedades de Gradle
```

## ⚠️ Problemas Comunes

### Error: "SDK location not found"

**Solución:** Crear archivo `local.properties` en la raíz:
```properties
sdk.dir=C\:\\Users\\jenny\\AppData\\Local\\Android\\Sdk
```

### Error: "Gradle sync failed"

**Solución:**
1. `File → Invalidate Caches → Invalidate and Restart`
2. Esperar a que reinicie
3. `File → Sync Project with Gradle Files`

### Error: "Java version"

**Solución:**
1. `File → Project Structure → SDK Location`
2. Cambiar JDK a versión 17 o superior
3. `File → Sync Project with Gradle Files`

### Error: "Unable to find method"

**Solución:**
1. Verificar que estés usando Android Studio Hedgehog o superior
2. Actualizar Android Studio si es necesario

## 🎯 Siguiente Paso: Integración

Una vez que el proyecto compile y ejecute correctamente:

1. **Copiar el módulo** `voicenotifications` a tu proyecto
2. **Agregar en settings.gradle:**
   ```gradle
   include ':voicenotifications'
   ```
3. **Agregar dependencia en tu app:**
   ```gradle
   dependencies {
       implementation project(':voicenotifications')
   }
   ```
4. **Seguir** la guía en `QUICK_START.md`

## 📞 Soporte

Si encuentras problemas:
1. Revisa los logs en `Build` tab en Android Studio
2. Verifica que todas las dependencias se descargaron
3. Consulta `BUILD_INSTRUCTIONS.md` para más detalles

---

**¡Listo para empezar!** 🚀

Una vez que Android Studio termine de sincronizar, el proyecto estará listo para usar.
