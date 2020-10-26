# recetasdivertidas 

## Guía básica de git para los pi'

Clonar repositorio a tu PC:
`git clone https://github.com/lbotano/recetasdivertidas.git`

### Pasos para subir cambios
Antes de hacer cualquier cambio en el proyecto tenés que hacer `git pull`, éste comando actualiza tu repositorio local con los cambios que hubo en el repositorio remoto.

Añadir todos los archivos que cambiaste al stage: `git add --all`

Aplicar los cambios que hiciste al repositorio local (tu PC): `git commit`
Te va a abrir un editor de texto, en la primer línea ponés el título del commit, la segunda línea la dejás en blanco y de la tercera en adelante podés dar una descripción más detallada del commit si querés.

Subir los cambios de tu repositorio local al repositorio remoto (Github): `git push`
Puede pedir que te loguees en Github para comprobar tus permisos.

## Organización de las carpetas
`Documentacion`: La documentación.
`cliente`: Proyecto Java del cliente de Recetas Divertidas.
`db`: Scripts de la base de datos (MySQL)
`servidor`: Proyecto Java del servidor de Recetas Divertidas.

## Cómo importar un proyecto Maven

### En Intellij IDEA

* `Open or import` al inicio o `File` -> `Open project` cuando ya está abierto otro proyecto.
* Seleccionamos la carpeta del proyecto, ésta tiene que tener un archivo llamado `pom.xml` dentro.
* Para correr el programa hacemos click derecho en la clase principal y le damos a `Run`, después de esto podés usar `Shift + F10` cuando quieras volver a ejecutarlo.

### En Eclipse

* `File` -> `Import...`
* `Maven` -> `Existing Maven Projects`
* Seleccionamos la carpeta del proyecto, ésta tiene que tener un archivo llamado `pom.xml` dentro.
* `Finish`
* Para correr el programa hacemos click derecho en la clase prinipal y le damos a `Run As...` -> `Java Application`
