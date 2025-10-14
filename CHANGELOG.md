# Changelog

Todos los cambios notables de este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/lang/es/).

## [1.0.0] - 2025-10-04

### ✨ Agregado
- Implementación inicial de la librería
- Arquitectura limpia (Clean Architecture) con capas Domain, Data y Presentation
- Soporte para Java 17
- Integración con AndroidX Lifecycle
- LiveData para eventos reactivos
- WorkManager para notificaciones en background
- Text-to-Speech nativo de Android
- Tipos de notificaciones predefinidos:
  - Exceso de velocidad
  - Frenada brusca
  - Aceleración brusca
  - Giro brusco
  - Notificaciones personalizadas
- `VoiceNotificationManager` como punto de entrada principal
- `NotificationThrottler` para prevenir spam
- `DriverBehaviorAnalyzer` para análisis de conducción
- `VoiceNotificationScheduler` para notificaciones programadas
- `NotificationMessageFactory` con mensajes en español e inglés
- Sistema de prioridades (LOW, NORMAL, HIGH, URGENT)
- Configuración personalizable de voz (velocidad, tono, idioma)
- Tests unitarios completos
- Aplicación de demostración
- Documentación completa (README, INTEGRATION_GUIDE, ARCHITECTURE)
- Soporte para ProGuard/R8

### 🔧 Técnico
- Min SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Gradle: 8.2.0
- AndroidX Core: 1.12.0
- Lifecycle: 2.6.2
- WorkManager: 2.9.0
- Material Components: 1.11.0

### 📚 Documentación
- README.md con guía de uso completa
- INTEGRATION_GUIDE.md con ejemplos de integración
- ARCHITECTURE.md con detalles de arquitectura
- JavaDoc completo en todas las clases públicas

### 🧪 Testing
- Tests unitarios para modelos de dominio
- Tests para NotificationThrottler
- Tests para DriverBehaviorAnalyzer
- Configuración de testing con JUnit 4.13.2 y Mockito 5.5.0

---

## Tipos de Cambios

- `✨ Agregado` - Para nuevas funcionalidades
- `🔧 Cambiado` - Para cambios en funcionalidad existente
- `⚠️ Deprecado` - Para funcionalidades que serán removidas
- `🗑️ Removido` - Para funcionalidades removidas
- `🐛 Corregido` - Para corrección de bugs
- `🔒 Seguridad` - Para vulnerabilidades de seguridad
