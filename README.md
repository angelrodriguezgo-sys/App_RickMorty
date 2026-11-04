# 🛸 Rick & Morty - Citadel App (Android Native)

Aplicación móvil nativa para Android desarrollada en Kotlin con Jetpack Compose, Retrofit y Firebase (Auth & Firestore). Permite explorar el multiverso de Rick y Morty, gestionar anomalías favoritas y administrar perfiles interdimensionales de usuario.

---

## 🎥 Demostración en Video (Máximo 2 minutos)

> 🎬 **[HAZ CLIC AQUÍ PARA VER EL VIDEO DE DEMOSTRACIÓN](PEGA_AQUI_TU_ENLACE_DE_YOUTUBE_O_DRIVE_O_LOOM)**  
> *(Reemplaza el enlace de arriba con tu enlace de YouTube, Loom o Google Drive donde grabaste el video de 2 minutos)*

---

## ✨ Características Principales

* 🌌 **Portal Explorer:** Búsqueda en tiempo real y filtrado avanzado (Estado, Género, Especie) conectándose a la API pública de Rick & Morty.
* 🌀 **Multiverso Infinito:** Exploración de todos los personajes del multiverso con scroll infinito paginado.
* ⭐ **Gestión de Favoritos:** Guardado de anomalías favoritas persistido por el UID de cada usuario.
* 👤 **Perfil e Identidad Citadel:** Personalización de nombre (alias), foto de perfil y generación dinámica de Coordenadas Dimensionales guardadas en Cloud Firestore.
* 🔐 **Autenticación Segura:** Inicio de sesión y registro de usuarios mediante Firebase Authentication.
* 🔒 **Política de Privacidad Integrada:** Enlace a la política de privacidad desde Bienvenida, Registro y Perfil.

---

## 🛠️ Tecnologías y Arquitectura

* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material 3)
* **Llamadas a API REST:** Retrofit 2 + Gson Converter (API: `https://rickandmortyapi.com/`)
* **Carga de Imágenes Async:** Coil Compose
* **Backend & Autenticación:** Firebase Auth & Cloud Firestore
* **Arquitectura:** MVVM (Model-View-ViewModel) + StateFlow & Coroutines
* **Persistencia Local:** SharedPreferences (AppPreferences aislados por UID)

---

## 📄 Política de Privacidad

La Política de Privacidad de la aplicación está publicada y accesible en:  
🔗 [Política de Privacidad en Google Sites](https://sites.google.com/view/poltica-de-privacidad-rick-and/p%C3%A1gina-principal)

---

## 📦 Compilación y Ejecución

1. Clonar este repositorio:
   ```bash
   git clone https://github.com/TU_USUARIO/TU_REPOSITORIO.git
   ```
2. Abrir el proyecto en **Android Studio**.
3. Asegurarse de mantener el archivo `app/google-services.json` configurado.
4. Compilar y ejecutar en un dispositivo físico o emulador Android (API 24+).
