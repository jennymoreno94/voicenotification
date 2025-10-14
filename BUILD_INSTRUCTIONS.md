# 🔨 Instrucciones de Compilación

Guía completa para compilar, probar y publicar la librería.

## 📋 Requisitos Previos

### Software Necesario

- **JDK 17** o superior
- **Android Studio** Hedgehog (2023.1.1) o superior
- **Android SDK** con:
  - Android 14 (API 34)
  - Build Tools 34.0.0
  - Android SDK Platform-Tools
- **Gradle** 8.2 (incluido en el wrapper)

### Verificar Instalación

```bash
# Verificar Java
java -version
# Debe mostrar: java version "17.x.x"

# Verificar Android SDK
echo $ANDROID_HOME
# Debe mostrar la ruta del SDK
```

## 🚀 Compilación

### 1. Clonar/Abrir Proyecto

```bash
cd C:\Users\jenny\CascadeProjects\driver-voice-notifications
```

### 2. Compilar Librería

```bash
# Windows
.\gradlew :voicenotifications:build

# Linux/Mac
./gradlew :voicenotifications:build
```

**Salida esperada:**
```
BUILD SUCCESSFUL in 30s
45 actionable tasks: 45 executed
```

### 3. Compilar App de Demo

```bash
.\gradlew :app:build
```

## 🧪 Testing

### Tests Unitarios

```bash
# Ejecutar todos los tests
.\gradlew :voicenotifications:test

# Solo tests de una clase
.\gradlew :voicenotifications:test --tests VoiceNotificationTest

# Con reporte HTML
.\gradlew :voicenotifications:testDebugUnitTest
# Reporte en: voicenotifications/build/reports/tests/testDebugUnitTest/index.html
```

### Tests de Instrumentación

```bash
# Conectar dispositivo o iniciar emulador
.\gradlew :voicenotifications:connectedAndroidTest
```

### Coverage

```bash
.\gradlew :voicenotifications:testDebugUnitTestCoverage
# Reporte en: voicenotifications/build/reports/coverage/
```

## 📦 Generar AAR

### Release AAR

```bash
.\gradlew :voicenotifications:assembleRelease
```

**Ubicación:** `voicenotifications/build/outputs/aar/voicenotifications-release.aar`

### Debug AAR

```bash
.\gradlew :voicenotifications:assembleDebug
```

**Ubicación:** `voicenotifications/build/outputs/aar/voicenotifications-debug.aar`

## 📱 Instalar App Demo

### En Dispositivo Conectado

```bash
.\gradlew :app:installDebug
```

### Generar APK

```bash
.\gradlew :app:assembleDebug
# APK en: app/build/outputs/apk/debug/app-debug.apk

.\gradlew :app:assembleRelease
# APK en: app/build/outputs/apk/release/app-release.apk
```

## 📚 Publicar Librería

### Opción 1: Maven Local

```bash
.\gradlew :voicenotifications:publishToMavenLocal
```

**Ubicación:** `~/.m2/repository/com/driver/voicenotifications/1.0.0/`

**Uso en otros proyectos:**
```gradle
repositories {
    mavenLocal()
}

dependencies {
    implementation 'com.driver:voicenotifications:1.0.0'
}
```

### Opción 2: Repositorio Privado

**Configurar en `voicenotifications/build.gradle`:**
```gradle
publishing {
    publications {
        release(MavenPublication) {
            from components.release
            groupId = 'com.driver'
            artifactId = 'voicenotifications'
            version = '1.0.0'
        }
    }
    repositories {
        maven {
            url = "https://tu-repositorio.com/maven"
            credentials {
                username = project.findProperty("repoUser")
                password = project.findProperty("repoPassword")
            }
        }
    }
}
```

**Publicar:**
```bash
.\gradlew :voicenotifications:publish
```

### Opción 3: JitPack

1. Subir a GitHub
2. Crear release/tag
3. Usar en proyectos:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.usuario:driver-voice-notifications:1.0.0'
}
```

## 🔍 Análisis de Código

### Lint

```bash
.\gradlew :voicenotifications:lint
# Reporte en: voicenotifications/build/reports/lint-results.html
```

### Checkstyle (Opcional)

Agregar en `voicenotifications/build.gradle`:
```gradle
apply plugin: 'checkstyle'

checkstyle {
    toolVersion = '10.12.0'
}
```

Ejecutar:
```bash
.\gradlew :voicenotifications:checkstyle
```

## 🧹 Limpieza

```bash
# Limpiar builds
.\gradlew clean

# Limpiar cache de Gradle
.\gradlew cleanBuildCache

# Limpiar todo (incluyendo wrapper)
rm -rf .gradle build */build
```

## 🐛 Troubleshooting

### Error: Java Version

```
Error: Unsupported class file major version 61
```

**Solución:**
```bash
# Verificar versión de Java
java -version

# Configurar JAVA_HOME
set JAVA_HOME=C:\Program Files\Java\jdk-17
```

### Error: SDK Location

```
Error: SDK location not found
```

**Solución:**
Crear `local.properties`:
```properties
sdk.dir=C\:\\Users\\usuario\\AppData\\Local\\Android\\Sdk
```

### Error: Out of Memory

```
Error: OutOfMemoryError: Java heap space
```

**Solución:**
Editar `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m
```

### Error: Dependency Resolution

```bash
# Limpiar y reconstruir
.\gradlew clean build --refresh-dependencies
```

## 📊 Tamaños de Build

### Librería (AAR)

- **Debug:** ~150 KB
- **Release:** ~80 KB (con ProGuard)

### App Demo (APK)

- **Debug:** ~3 MB
- **Release:** ~2 MB (con ProGuard)

## ⚡ Optimizaciones

### Build Paralelo

**gradle.properties:**
```properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
```

### Daemon

```bash
# Iniciar daemon
.\gradlew --daemon

# Detener daemon
.\gradlew --stop
```

## 🔐 Firma de Release

### Generar Keystore

```bash
keytool -genkey -v -keystore driver-voice-notifications.keystore \
  -alias release -keyalg RSA -keysize 2048 -validity 10000
```

### Configurar Firma

**app/build.gradle:**
```gradle
android {
    signingConfigs {
        release {
            storeFile file("../driver-voice-notifications.keystore")
            storePassword "tu_password"
            keyAlias "release"
            keyPassword "tu_password"
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
        }
    }
}
```

## 📈 CI/CD

### GitHub Actions

**.github/workflows/build.yml:**
```yaml
name: Build

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Build with Gradle
        run: ./gradlew build
      
      - name: Run Tests
        run: ./gradlew test
      
      - name: Upload AAR
        uses: actions/upload-artifact@v3
        with:
          name: voicenotifications-aar
          path: voicenotifications/build/outputs/aar/*.aar
```

## 📝 Checklist de Release

- [ ] Actualizar versión en `build.gradle`
- [ ] Actualizar `CHANGELOG.md`
- [ ] Ejecutar todos los tests
- [ ] Ejecutar lint
- [ ] Generar AAR release
- [ ] Probar AAR en proyecto demo
- [ ] Actualizar documentación
- [ ] Crear tag en Git
- [ ] Publicar en repositorio
- [ ] Actualizar README con nueva versión

## 🆘 Soporte

Si encuentras problemas durante la compilación:

1. Verifica requisitos previos
2. Limpia el proyecto: `.\gradlew clean`
3. Invalida caché en Android Studio: `File > Invalidate Caches > Restart`
4. Verifica logs en `build/outputs/logs/`
5. Abre un issue en el repositorio

---

**¡Listo para compilar!** 🚀
