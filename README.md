Herramientas utilizadas:
Android Studio: entorno de desarrollo para crear y ejecutar la aplicación.
Java: lenguaje utilizado para la programación.
XML: utilizado para diseñar las interfaces de la aplicación.
Firebase Firestore: base de datos en la nube para guardar y consultar incidencias.
OpenStreetMap: servicio de mapas utilizado en la aplicación.
GitHub: plataforma utilizada para almacenar y compartir el código fuente.
Se utilizó ChatGPT, como herramienta de apoyo durante el desarrollo de Geo.
la use para resolver dudas de programación en Java y Android Studio, comprender errores, recibir orientación sobre la conexión con Firebase Firestore y mejorar el funcionamiento de las pantallas de la aplicación.

Version:
Android Studio Quail 3 | 2026.1.3
Runtime version: 25.0.2+-15348964-b329.117 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o

Finalidad del uso
la use para resolver dudas de programación en Java y Android Studio, comprender errores, recibir orientación sobre la conexión con Firebase Firestore y mejorar el funcionamiento de las pantallas de la aplicación.

consultas realizadas
Cómo guardar incidencias en Firebase Firestore desde Android Studio?
Cómo mostrar los registros guardados en una lista?
Cómo solucionar errores de conexión y permisos de Firestore?
Cómo implementar y mostrar un mapa con OpenStreetMap?
Cómo preparar el proyecto para subirlo a GitHub?

Acceder a Firebase Console.
Crear un proyecto de Firebase o utilizar uno existente.
Registrar la aplicación Android usando el nombre de paquete del proyecto Geo.
Descargar el archivo google-services.json correspondiente a la aplicación.
Colocar google-services.json dentro de la carpeta app del proyecto Android.
Activar Cloud Firestore y configurar sus reglas de seguridad según los requisitos de la aplicación.

rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /incidencias/{documento} {
      allow create: if true;
      allow read: if true;
    }
  }
}


Abrir el proyecto en Android Studio y sincronizar Gradle.
Ejecutar la aplicación y comprobar que se puedan registrar y consultar incidencias


tener Android Studio instalado.
Contar con conexión a Internet.
Tener acceso al proyecto de Firebase utilizado por la aplicación.
Disponer de un emulador Android o un dispositivo físico compatible.

Instrucciones de ejecución
Descargar o clonar este repositorio de GitHub.
Abrir Android Studio.
Seleccionar Open y elegir la carpeta principal del proyecto GeoRegistro.
Esperar a que termine la sincronización de Gradle.
Configurar Firebase siguiendo las instrucciones de la sección correspondiente.
Iniciar un emulador Android o conectar un dispositivo físico compatible.
Pulsar Run para ejecutar la aplicación.
Probar el registro y la consulta de incidencias y comprobar la pantalla del mapa.
