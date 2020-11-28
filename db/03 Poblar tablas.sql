USE RecetasDivertidasDB;

-- Truncar tablas
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Calificacion;
TRUNCATE TABLE CategoriaDeIngrediente;
TRUNCATE TABLE CategoriaDeReceta;
TRUNCATE TABLE Ingrediente;
TRUNCATE TABLE IngredienteReceta;
TRUNCATE TABLE Multimedia;
TRUNCATE TABLE PreguntasSeguridad;
TRUNCATE TABLE Receta;
TRUNCATE TABLE RelCatIngred;
TRUNCATE TABLE RelCatReceta;
TRUNCATE TABLE Usuario;
SET FOREIGN_KEY_CHECKS = 1;

-- Insertar preguntas de seguridad
INSERT INTO PreguntasSeguridad (pregunta)
VALUES ("¿Cómo se llamó tu primer mascota?");
INSERT INTO PreguntasSeguridad (pregunta)
VALUES ("¿Cuál fue tu primer auto?");
INSERT INTO PreguntasSeguridad (pregunta)
VALUES ("¿Cómo se llama tu mejor amigo de la infancia?");
INSERT INTO PreguntasSeguridad (pregunta)
VALUES ("¿Cuál es tu pelicula favorita?");
INSERT INTO PreguntasSeguridad (pregunta)
VALUES ("¿Cómo se llama tu serie preferida?");

-- Insertar usuarios
CALL spRegistroUsuario (
	"lbotano",
	1,
	"morita",
	"Lautaro",
	"Otaño",
	"lautaro123",
	0,
	"otanolautarob@gmail.com",
    true,
	@resultado);

CALL spRegistroUsuario (
	"jorgelina",
    2,
    "Toyota",
    "Jorgelina",
    "Gutiérrez",
    "jorgelina123",
    1,
    "jorgelina@jorgelinaventiladores.cl",
    false,
    @resultado);

CALL spRegistroUsuario (
	"ryukyz",
	1,
	"Galo",
	"Camilo",
	"Laruffa",
	"camilo200",
	0,
	"camiloreal@gmail.com",
    true,
	@resultado);
    
CALL spRegistroUsuario (
	"Chefsito",
	5,
	"Hajime no ippo",
	"Tusar",
	"Verma",
	"tusar200",
	0,
	"esteeselmailrealdetusar@gmail.com",
    true,
	@resultado);
    
-- Cambiar contraseñas
CALL spCambiarContrasena(
	"lbotano",
    "Morita",
    "lautaro200");


-- Subir receta

CALL spSubirReceta (
	'jorgelina',
	'Empanada',
    'Se come',
    'Haces empanadas ._.',
    '[{"iID": 2, "cantidad": 4, "unidadCantidad": "kg"}]',
    NULL,
    '[{"cID":1}]'
);
