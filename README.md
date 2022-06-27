# Vuela V Aplicación Móvl

> Desarrollado por Grupo 1 (Caso 1)
---

## Descripción
- Versión mínima de Android soportada: 7.1.1
- SDK mínimo 25
- Target SDK 32
- Desarrollado con Java 1.8
- Gestor de dependencias: Gradle

## Dependencias
#### Material Android (Material 3)
- com.google.android.material:material:1.6.1

#### Okhttp, Retrofit (Api client)
- com.squareup.okhttp3:logging-interceptor:4.6.0
- com.squareup.retrofit2:retrofit:2.9.0
- com.squareup.retrofit2:converter-gson:2.9.0

#### Zxing android (Generar QR)
- com.journeyapps:zxing-android-embedded:4.3.0
- com.google.firebase:firebase-messaging:23.0.5

### Plugins
- com.android.application
- com.google.gms.google-services

---
## Firebase Messaging Service: Configuración Notificaciones Push
Para configurar el servicio de notificaciones con FCM, es necesario crear unproyecto en la [consola de Firebase](https://console.firebase.google.com/).
Una vez generado el proyecto, se debe agregar una aplicación, para lo que es necesario obtener el **applicationId** de nuestra app, que se encuentra en el archivo **build.gradle**, a nivel de app, dentro de la carpeta src/

FCM requiere un archivo de configuración con el nombre **google-services.json**, este archivo lo podemos obtener en la [consola de Firebase](https://console.firebase.google.com/), más información [aquí](https://support.google.com/firebase/answer/7015592?hl=es#zippy=%2Cen-este-art%C3%ADculo).

El archivo debe colocarse dentro de la carpeta /app/src/.

[Aquí](https://firebase.google.com/docs/cloud-messaging/android/client) puede visitar la documentación oficial de Android para configurar las notificaciones push con Firebase.


## SQLite 
Esta aplicación usa SQLite para guardar datos del usuario, el nombre de la base es **vuelav.db**

[Aquí](https://developer.android.com/training/data-storage/sqlite?hl=es-419) se puede vistar la documentación oficial de Android para la implementación de SQLite.