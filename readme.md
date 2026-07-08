# Filmoteca - Google Sign In y Firebase Cloud Messaging

## 1. Descripción del proyecto

Esta práctica consiste en ampliar una aplicación Android llamada **Filmoteca**.

La aplicación permite:

- Mostrar un listado de películas.
- Consultar los datos de una película.
- Añadir películas desde la app.
- Ver una pantalla de **Acerca de...**.
- Iniciar sesión con Google antes de entrar en la app.
- Mostrar el usuario conectado en la pantalla **Acerca de...**.
- Cerrar sesión.
- Desconectar la cuenta de Google.
- Recibir mensajes de Firebase Cloud Messaging para dar de alta o de baja películas.

La práctica está desarrollada en **Kotlin** usando Android Studio.

---

## 2. Tecnologías utilizadas

- Android Studio
- Kotlin
- XML Layouts
- Jetpack Compose en algunas pantallas
- Firebase Authentication
- Google Sign In
- Firebase Cloud Messaging
- Logcat
- Emulador Android con servicios de Google

---

## 3. Funcionalidades implementadas

### 3.1. Login con Google

Se ha añadido una pantalla inicial de login.

El usuario debe iniciar sesión con una cuenta de Google antes de entrar al listado de películas.

Si el usuario ya está autenticado, la aplicación entra directamente a la pantalla principal.

### 3.2. Usuario conectado en About

En la pantalla **Acerca de...** se muestra el usuario autenticado:

```text
Usuario conectado:
Nombre del usuario
Correo electrónico
```

Los datos se obtienen desde Firebase Authentication.

### 3.3. Cerrar sesión

En el menú principal se ha añadido la opción:

```text
Cerrar sesión
```

Esta opción cierra la sesión actual de Firebase y Google, y vuelve a la pantalla de login.

### 3.4. Desconectar cuenta

En el menú principal también se ha añadido la opción:

```text
Desconectar cuenta
```

Esta opción revoca el acceso de Google a la aplicación y vuelve a la pantalla de login.

---

## 4. Firebase Cloud Messaging

Se ha añadido Firebase Cloud Messaging para recibir mensajes desde Firebase Console.

La app recibe mensajes de datos con una operación:

```text
operacion = alta
```

o:

```text
operacion = baja
```

Según la operación recibida, la app añade, actualiza o elimina una película.

---

## 5. Alta de película por FCM

Para dar de alta una película desde Firebase, se envía un mensaje de prueba con estos datos personalizados:

| Clave | Valor |
|---|---|
| `operacion` | `alta` |
| `title` | `Soul` |
| `director` | `Pete Docter` |
| `year` | `2020` |
| `genre` | `Animación` |
| `format` | `Digital` |
| `imdbUrl` | `https://www.imdb.com/` |
| `notes` | `Recibida por Firebase` |

Si la película no existe, se añade al listado.

Si la película ya existe, se actualiza.

En Logcat aparece:

```text
FCM_MESSAGE: Mensaje recibido
FCM_MESSAGE: Película añadida: Soul
```

o:

```text
FCM_MESSAGE: Película actualizada: Soul
```

---

## 6. Baja de película por FCM

Para eliminar una película desde Firebase, se envía un mensaje de prueba con estos datos personalizados:

| Clave | Valor |
|---|---|
| `operacion` | `baja` |
| `title` | `Soul` |

Si la película existe, se elimina del listado.

En Logcat aparece:

```text
FCM_MESSAGE: Mensaje recibido
FCM_MESSAGE: Película eliminada: Soul
```

Si la película no existe, aparece:

```text
FCM_MESSAGE: Película no encontrada: Soul
```

---

## 7. Cómo probar la aplicación

### 7.1. Ejecutar la app

1. Abrir el proyecto en Android Studio.
2. Ejecutar la aplicación en un emulador Android con servicios de Google.
3. La app muestra primero la pantalla de login.
4. Iniciar sesión con una cuenta Google.
5. Entrar al listado de películas.

### 7.2. Probar About

1. Entrar en la app con Google.
2. Pulsar **ABOUT...**.
3. Comprobar que aparece el nombre y el correo del usuario conectado.

### 7.3. Probar cerrar sesión

1. Abrir el menú principal.
2. Pulsar **Cerrar sesión**.
3. La app vuelve a la pantalla de login.

### 7.4. Probar desconectar cuenta

1. Iniciar sesión de nuevo.
2. Abrir el menú principal.
3. Pulsar **Desconectar cuenta**.
4. La app vuelve a la pantalla de login.

---

## 8. Cómo probar Firebase Cloud Messaging

### 8.1. Obtener el token FCM

1. Ejecutar la app.
2. Entrar al listado de películas.
3. Abrir Logcat.
4. Buscar:

```text
FCM_TOKEN
```

5. Copiar el token que aparece después de:

```text
Token actual:
```

### 8.2. Enviar mensaje de prueba

En Firebase Console:

1. Entrar en el proyecto Firebase.
2. Ir a **Mensajería**.
3. Crear una campaña de **Mensajes de notificación de Firebase**.
4. Rellenar título y texto.
5. Añadir los datos personalizados.
6. Pulsar **Enviar mensaje de prueba**.
7. Pegar el token FCM del emulador.
8. Pulsar **Prueba**.
9. Comprobar el resultado en Logcat buscando:

```text
FCM_MESSAGE
```

---

## 9. Demostración recomendada en vídeo

Para demostrar la práctica en vídeo:

1. Mostrar la pantalla de login.
2. Iniciar sesión con Google.
3. Entrar al listado de películas.
4. Abrir **ABOUT...** y mostrar el usuario conectado.
5. Mostrar Logcat con el token FCM.
6. Enviar desde Firebase un mensaje de alta de `Soul`.
7. Mostrar en Logcat `Película añadida: Soul`.
8. Mostrar que `Soul` aparece en el listado.
9. Enviar desde Firebase un mensaje de baja de `Soul`.
10. Mostrar en Logcat `Película eliminada: Soul`.
11. Mostrar que `Soul` desaparece del listado.

---

## 10. Archivos principales modificados

| Archivo | Función |
|---|---|
| `LoginActivity.kt` | Pantalla de login con Google Sign In |
| `FilmListActivity.kt` | Listado de películas, menú, cierre de sesión, token FCM |
| `AboutActivity.kt` | Muestra información del usuario conectado |
| `FilmDataActivity.kt` | Muestra los datos reales de cada película |
| `FilmDataSource.kt` | Gestiona la lista de películas y procesa altas/bajas |
| `MyFirebaseMessagingService.kt` | Recibe y procesa mensajes FCM |
| `AndroidManifest.xml` | Declara actividades, permisos y servicio FCM |
| `menu_film_list.xml` | Menú con añadir, About, cerrar sesión y desconectar cuenta |

---

## 11. Avisos

- El emulador debe tener servicios de Google.
- Firebase Analytics no es necesario para esta práctica.
- Los avisos deprecados de Google Sign In no impiden el funcionamiento de la app.
- El token FCM puede cambiar si se reinstala la app o se cambia de emulador.

---

## 12. Estado final

La práctica queda completada con:

- Login con Google.
- Acceso a la app solo después del login.
- Visualización del usuario conectado en About.
- Cierre de sesión.
- Desconexión de cuenta.
- Firebase Cloud Messaging configurado.
- Alta de película mediante FCM.
- Baja de película mediante FCM.
- Comprobación mediante Logcat y emulador.

## Evidencias
Video demostrativo con emulador y FireBase para ver los token en la app.
- SignIn_Firebase.mp4
