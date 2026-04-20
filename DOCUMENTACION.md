# Documentación Final del Reto – Ricoh
**Actividad:** El Arte de Desarrollar con IA  
**Empresa:** Ricoh  
**Ponente:** Rubén Ramos  
**Equipo:** Hugo Campillo  
**Fecha:** Abril 2026

---

## 1. Problema elegido

Los videoclubs necesitan gestionar manualmente qué películas están disponibles, quién las tiene alquiladas y cuándo se devuelven. Sin un sistema digitalizado, es fácil perder el control del inventario y cometer errores en las reservas.

**Necesidad resuelta:** una aplicación que permita registrar películas, clientes y reservas, controlando automáticamente las copias disponibles de cada película.

---

## 2. Descripción de la aplicación

**VideoClub – Sistema de Reservas** es una aplicación de escritorio desarrollada en Java con interfaz gráfica (Swing). Permite gestionar de forma completa el ciclo de vida de una reserva en un videoclub: desde añadir películas y clientes, hasta crear reservas, registrar devoluciones y consultar el historial.

La aplicación se organiza en tres áreas accesibles desde pestañas:

| Pestaña | Funcionalidad |
|---|---|
| Películas | Catálogo completo con búsqueda y gestión |
| Clientes | Registro de socios del videoclub |
| Reservas | Gestión de alquileres activos e histórico |

---

## 3. Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 17+ | Lenguaje principal de la aplicación |
| Java Swing | Librería para la interfaz gráfica (incluida en el JDK) |
| Visual Studio Code | Entorno de desarrollo |
| Extension Pack for Java | Extensión de VSCode para compilar y ejecutar Java |
| Claude AI (IA) | Asistente de programación utilizado en el desarrollo |
| GitHub | Control de versiones y repositorio del proyecto |

No se utilizó base de datos: los datos se almacenan en memoria (listas `ArrayList`) durante la ejecución.

---

## 4. Funcionalidades implementadas

### Películas
- Listar todas las películas con título, género y copias disponibles
- Añadir nueva película (título, género, número de copias)
- Modificar los datos de una película existente
- Eliminar película (solo si no tiene reservas activas)
- Buscar películas por título (filtro parcial, sin distinguir mayúsculas)
- Buscar películas por género (filtro exacto)

### Clientes
- Listar todos los clientes
- Añadir nuevo cliente (nombre y teléfono)
- Modificar datos de un cliente
- Eliminar cliente (solo si no tiene reservas activas)

### Reservas
- Ver reservas activas (filtro por defecto)
- Ver historial completo de todas las reservas
- Crear nueva reserva seleccionando cliente y película desde listas desplegables
- Registrar devolución de una película (actualiza copias disponibles automáticamente)
- Eliminar una reserva del historial

### Reglas de negocio implementadas
- Al crear una reserva, se descuenta una copia disponible de la película
- Al devolver, se suma de nuevo esa copia
- No se puede reservar una película sin copias disponibles
- No se puede eliminar una película o cliente con reservas activas vigentes

---

## 5. Código apoyado por IA

Se utilizó Claude AI como asistente de programación. Las partes generadas con apoyo de IA fueron:

| Parte del código | Petición realizada | ¿Se utilizó? |
|---|---|---|
| Clases del modelo (`Pelicula`, `Cliente`, `Reserva`) | "Crea las clases Java para un videoclub con sus atributos y getters/setters" | Sí, con ajustes |
| Clase `VideoClub` con lógica CRUD | "Genera una clase Java que gestione listas de películas, clientes y reservas" | Sí, con ajustes |
| Paneles Swing (`PeliculasPanel`, `ClientesPanel`, `ReservasPanel`) | "Crea una interfaz gráfica con JTable y botones para cada sección" | Sí, con ajustes |
| Ventana principal `MainWindow` con pestañas | "Crea un JFrame con JTabbedPane con tres secciones" | Sí |
| Configuración de VSCode (`.vscode/settings.json`, `launch.json`) | "El proyecto no compila en VSCode, crea la configuración necesaria" | Sí |

---

## 6. Cambios realizados sobre las propuestas de IA

| Propuesta original de IA | Cambio realizado | Motivo |
|---|---|---|
| Métodos `void` que imprimían por consola | Se cambiaron a métodos que devuelven `String` | Necesario para mostrar mensajes en la GUI con `JOptionPane` |
| Aplicación de consola con `Scanner` | Se sustituyó por interfaz gráfica Swing | Mejor experiencia de usuario y mayor claridad visual |
| Listas `listarPeliculas()` que hacían `System.out.println` | Se eliminaron; la GUI obtiene los datos con `getPeliculas()` | El panel gestiona directamente la visualización en la tabla |
| `JComboBox` mostraba el `toString()` completo del objeto | Se mantuvo porque el `toString()` de `Pelicula` ya muestra título y copias | Era suficientemente claro para el usuario |
| No había validación de campos vacíos | Se añadió validación en los formularios de añadir/modificar | Evita insertar datos incompletos en el sistema |
| Sin datos de ejemplo al arrancar | Se añadió `cargarDatosEjemplo()` en `VideoClub` | Permite probar la aplicación sin tener que introducir datos manualmente |

---

## 7. Errores detectados

| Error | Descripción | Solución |
|---|---|---|
| La aplicación no se abría desde VSCode | VSCode no reconocía `src/` como raíz del proyecto Java, por lo que los paquetes (`import ui.MainWindow`) no se resolvían | Se crearon `.vscode/settings.json` con `"java.project.sourcePaths": ["src"]` y `launch.json` |
| `javac` no disponible en el terminal bash | El JDK no estaba en el PATH del entorno Git Bash | Se solucionó usando el botón Run de VSCode en lugar del terminal |
| Copias disponibles no se actualizaban visualmente | Al crear una reserva desde el panel de Reservas, el panel de Películas no se refrescaba automáticamente | Se añadió un `ChangeListener` en las pestañas de `MainWindow` para refrescar cada panel al seleccionarlo |
| Posible `NullPointerException` al modificar | Si se borraba el ID de la tabla entre operaciones, `buscarPeliculaPorId` podía devolver `null` | Se añadió comprobación `if (p == null) return;` antes de abrir el formulario |

---

## 8. Decisiones técnicas tomadas

**¿Por qué Java?**  
Es el lenguaje que conocemos mejor. Usar una tecnología ya aprendida permitió centrarse en el diseño de la aplicación en lugar de en aprender la sintaxis.

**¿Por qué aplicación de escritorio y no web?**  
Una aplicación web habría requerido aprender HTML/CSS/JavaScript o un framework como Spring, lo que habría consumido demasiado tiempo. Swing permite crear una interfaz visual completa solo con Java.

**¿Por qué Swing y no JavaFX?**  
Swing viene incluido en el JDK sin necesidad de instalar dependencias adicionales. JavaFX requiere configuración extra que habría complicado la puesta en marcha.

**¿Por qué listas en memoria y no base de datos?**  
Para una aplicación de demostración del reto, una base de datos (MySQL, SQLite) habría añadido complejidad innecesaria de configuración. Con listas `ArrayList` el código es más sencillo, claro y fácil de mantener.

**¿Por qué tres clases de modelo separadas (`Pelicula`, `Cliente`, `Reserva`)?**  
Seguir el principio de responsabilidad única hace el código más fácil de entender y modificar. Cada clase representa exactamente una entidad del mundo real.

**¿Por qué `VideoClub` devuelve `String` en lugar de `void`?**  
La GUI necesita capturar el mensaje de resultado para mostrarlo al usuario. Si el método solo imprimiera por consola, la interfaz no podría informar al usuario. Esta fue una decisión que se tomó al revisar la propuesta inicial de la IA.

---

## 9. Conclusiones sobre el uso de IA

**Lo que la IA hizo bien:**
- Generó la estructura inicial del proyecto (clases, paquetes, métodos) muy rápidamente
- Propuso un diseño ordenado con separación entre modelo, lógica y UI
- Sugirió soluciones para la configuración de VSCode cuando el proyecto no arrancaba
- Produjo código limpio y legible que fue fácil de revisar

**Lo que fue necesario revisar o corregir:**
- La primera versión era de consola; hubo que decidir cambiarlo a GUI
- Los métodos `void` no servían para la interfaz gráfica y tuvieron que rediseñarse
- La configuración del entorno (VSCode, rutas de paquetes) requirió intervención manual
- La validación de campos vacíos en los formularios no estaba incluida en la propuesta inicial

**Reflexión:**  
La IA aceleró enormemente la escritura del código repetitivo (getters, setters, estructura de paneles Swing). Sin embargo, no sustituyó la necesidad de entender el código: fue imprescindible revisar cada propuesta, detectar lo que no encajaba con el diseño real de la aplicación y tomar decisiones propias. El valor del desarrollador estuvo en decidir *qué* construir y *cómo* conectar las piezas, no solo en escribir el código.

---

## Estructura final del proyecto

```
Ricoh/
├── src/
│   ├── Main.java                  ← Punto de entrada, lanza la ventana
│   ├── model/
│   │   ├── Pelicula.java          ← Entidad película
│   │   ├── Cliente.java           ← Entidad cliente
│   │   └── Reserva.java           ← Entidad reserva (vincula cliente + película)
│   ├── manager/
│   │   └── VideoClub.java         ← Lógica de negocio y datos en memoria
│   └── ui/
│       ├── MainWindow.java        ← Ventana principal con pestañas
│       ├── PeliculasPanel.java    ← Panel de gestión de películas
│       ├── ClientesPanel.java     ← Panel de gestión de clientes
│       └── ReservasPanel.java     ← Panel de gestión de reservas
├── .vscode/
│   ├── settings.json              ← Configuración de rutas para VSCode
│   └── launch.json                ← Configuración de ejecución
└── DOCUMENTACION.md               ← Este documento
```
