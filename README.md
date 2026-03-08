# Abastock

**El control de tu tienda en tu bolsillo.**

Abastock es una aplicacion movil Android disenada para que tenderos y duenos de abarrotes gestionen su negocio de forma rapida y sencilla desde su celular. Desarrollada con tecnologias modernas de Android, ofrece una experiencia fluida y una interfaz limpia orientada al mercado latinoamericano.

## Caracteristicas

- **Registro de cuenta** - Crea tu perfil con nombre de tienda, datos de contacto y credenciales de acceso, con validacion en tiempo real.
- **Inicio de sesion** - Accede con correo/telefono y contrasena, o mediante Google y Apple.
- **Interfaz moderna** - UI construida con Jetpack Compose y Material 3, con gradientes, animaciones y componentes reutilizables.

## Stack tecnologico

| Capa | Tecnologia |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Inyeccion de dependencias | Hilt |
| Base de datos local | Room |
| Networking | Retrofit |
| Carga de imagenes | Coil |
| Navegacion | Navigation Compose |
| Procesamiento de anotaciones | KSP |
| Serializacion | Kotlin Serialization |

## Arquitectura

El proyecto sigue una arquitectura basada en **features** con separacion por capas de presentacion:

```
app/src/main/java/com/softgenix/abastock/
├── core/
│   ├── shared/components/   # Componentes reutilizables (Button, Header, StyledInput, InputLabel)
│   └── ui/theme/            # Tema, colores y tipografia
├── features/
│   └── authentication/
│       └── presentation/
│           ├── screens/     # SignInScreen, SignUpScreen, SignUpSuccessScreen
│           └── components/  # Componentes especificos de autenticacion
└── MainActivity.kt
```

## Requisitos previos

- **Android Studio** Ladybug o superior
- **JDK 21**
- **Android SDK** con compileSdk 36
- Dispositivo o emulador con **Android 8.0 (API 26)** como minimo

## Configuracion del proyecto

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Kevinjr2912/abastock-frontend.git
   ```

2. Abre el proyecto en Android Studio.

3. Crea un archivo `local.properties` en la raiz del proyecto (si no existe) y agrega la ruta de tu SDK:
   ```properties
   sdk.dir=/ruta/a/tu/Android/Sdk
   ```

4. Sincroniza Gradle y ejecuta la aplicacion en un emulador o dispositivo fisico.

## Compilar desde terminal

```bash
./gradlew assembleDebug
```

El APK generado se encontrara en `app/build/outputs/apk/debug/`.

## Equipo

Desarrollado por **SoftGenix**.

## Licencia

Este proyecto es de uso privado. Todos los derechos reservados.
