# Turnito - Sistema de Gestión de Turnos

Este proyecto, **Turnito**, es una aplicación de gestión de turnos desarrollada con **Spring Boot**. El objetivo principal es ofrecer una plataforma para solicitar turnos, así como realizar operaciones CRUD (Altas, Bajas, Modificaciones) sobre las entidades principales del sistema.

---

## Requisitos Previos para la Ejecución

Para poder levantar y ejecutar el proyecto **Turnito** correctamente, es necesario cumplir con los siguientes requisitos:

### 1. Java Development Kit (JDK) 21

Asegúrate de tener **JDK 21** instalado en tu sistema. Puedes verificar tu versión de Java ejecutando `java -version` en tu terminal.

### 2. Apache Maven

El proyecto utiliza **Maven** como herramienta de gestión de dependencias y construcción. Verifica que Maven esté instalado y configurado correctamente en tu PATH. Puedes hacerlo ejecutando `mvn -v` en tu terminal.

### 3. Base de Datos MySQL

Se requiere una base de datos **MySQL**. El proyecto está configurado para conectarse a una base de datos llamada `turnito_db`. Tienes dos opciones para la creación de esta base de datos:

* **Creación manual:** Crea una base de datos vacía con el nombre `turnito_db` en tu servidor MySQL.

### 4. Variables de Entorno para la Conexión a la Base de Datos

El proyecto utiliza **variables de entorno** para configurar la conexión a la base de datos. Es **crucial** que definas las siguientes variables en tu sistema antes de ejecutar la aplicación:

* `DB_URL`: La URL de conexión a tu base de datos MySQL (ejemplo: `jdbc:mysql://localhost:3306/turnito_db`).
* `USERNAME`: El nombre de usuario para acceder a tu base de datos MySQL.
* `PASSWORD`: La contraseña para acceder a tu base de datos MySQL.
* `USER_EMAIL`: Buscar en la entrega un archivo llamada contrasena_email.txt
* `PASSWORD_EMAIL`: Buscar en la entrega un archivo llamada contrasena_email.txt

### 5. Lombok

Este proyecto hace uso de la librería **Lombok**, asegurarse de tener el plugin descargado.

---

## Instrucciones para Levantar el Proyecto

Sigue estos pasos para poner en marcha el proyecto **Turnito**:

1.  **Clona el repositorio** (si aún no lo has hecho) y navega hasta el directorio raíz del proyecto.

2.  **Configura las variables de entorno:** Antes de ejecutar, asegúrate de que las variables de entorno `DB_URL`, `USERNAME`, `PASSWORD`, `USER_EMAIL`, y `PASSWORD_EMAIL`estén correctamente definidas en tu sistema o en el entorno de ejecución de tu IDE.

3.  **Compila y ejecuta el proyecto:** Abre una terminal en el directorio raíz del proyecto y ejecuta el siguiente comando Maven:

    ```bash
    mvn spring-boot:run
    ```

    Esto compilará el proyecto, descargará las dependencias necesarias y levantará el servidor Spring Boot.

4.  **Ejecuta el script SQL inicial:** Una vez que veas que el servidor Spring Boot se ha levantado correctamente (generalmente se indica con mensajes en la consola como "Started Application..."), **es fundamental ejecutar el script SQL inicial** proporcionado. Este script poblará la base de datos con los datos necesarios para el correcto funcionamiento de la aplicación.


---

## Navegación por la Aplicación

Una vez que el proyecto se ha levantado correctamente y el script SQL inicial ha sido ejecutado en la base de datos, podrás acceder a la aplicación **Turnito** a través de tu navegador web.

Desde allí, podrás:

* **Pedir un turno:** Explorar la funcionalidad principal para solicitar un nuevo turno y recibir un email con la confirmación del mismo.
* **Utilizar los ABM:** Acceder a las secciones dedicadas a la gestión (Altas, Bajas, Modificaciones) de las clases principales del sistema.
* **Ver listados de datos:** Consultar y visualizar los datos existentes de las diferentes entidades, aplicando diferentes tipos de filtros.

---

