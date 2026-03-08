# Abastock

**El control de tu tienda en tu bolsillo.**

Abastock es una aplicación móvil Android diseñada para que tenderos y dueños de abarrotes gestionen su negocio de forma rápida y sencilla desde su celular. Desarrollada con tecnologías modernas de Android, ofrece una experiencia fluida y una interfaz limpia orientada al mercado latinoamericano.

## Características

- **Registro de cuenta** — Crea tu perfil con nombre de tienda, datos de contacto y credenciales de acceso, con validación en tiempo real.
- **Inicio de sesión** — Accede con correo/teléfono y contraseña, o mediante Google y Apple.
- **Interfaz moderna** — UI construida con Jetpack Compose y Material 3, con gradientes, animaciones y componentes reutilizables.

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Inyección de dependencias | Hilt |
| Base de datos local | Room |
| Networking | Retrofit |
| Carga de imágenes | Coil |
| Navegación | Navigation Compose |
| Procesamiento de anotaciones | KSP |
| Serialización | Kotlin Serialization |

## Arquitectura

El proyecto sigue una arquitectura basada en **features** con separación por capas de presentación:

```
app/src/main/java/com/softgenix/abastock/
├── core/
│   ├── shared/components/   # Componentes reutilizables (Button, Header, StyledInput, InputLabel)
│   └── ui/theme/            # Tema, colores y tipografía
├── features/
│   └── authentication/
│       └── presentation/
│           ├── screens/     # SignInScreen, SignUpScreen, SignUpSuccessScreen
│           └── components/  # Componentes específicos de autenticación
└── MainActivity.kt
```

## Requisitos previos

- **Android Studio** Ladybug o superior
- **JDK 21**
- **Android SDK** con compileSdk 36
- Dispositivo o emulador con **Android 8.0 (API 26)** como mínimo

## Configuración del proyecto

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Kevinjr2912/abastock-frontend.git
   ```

2. Abre el proyecto en Android Studio.

3. Crea un archivo `local.properties` en la raíz del proyecto (si no existe) y agrega la ruta de tu SDK:
   ```properties
   sdk.dir=/ruta/a/tu/Android/Sdk
   ```

4. Sincroniza Gradle y ejecuta la aplicación en un emulador o dispositivo físico.

## Compilar desde terminal

```bash
./gradlew assembleDebug
```

El APK generado se encontrará en `app/build/outputs/apk/debug/`.

## Equipo

Desarrollado por **SoftGenix**.

## Licencia

Este proyecto es de uso privado. Todos los derechos reservados.
