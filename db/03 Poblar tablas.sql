USE RecetasDivertidasDB;

-- Truncar tablas
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE PreguntasSeguridad;
TRUNCATE TABLE Usuario;
TRUNCATE TABLE CategoriaDeReceta;
TRUNCATE TABLE CategoriaDeIngrediente;
TRUNCATE TABLE Ingrediente;
TRUNCATE TABLE RelCatIngred;
TRUNCATE TABLE Receta;
TRUNCATE TABLE Multimedia;
TRUNCATE TABLE IngredienteReceta;
TRUNCATE TABLE RelCatReceta;
SET FOREIGN_KEY_CHECKS = 1;

-- Insertar preguntas de seguridad
INSERT INTO PreguntasSeguridad (pregunta)
VALUES ("¿Cómo se llamó tu primer mascota?");
INSERT INTO PreguntasSeguridad (pregunta)
VALUES ("¿Cuál fue tu primer auto?");

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
    
-- Añadir categorías de recetas
CALL spAgregarCategoriaReceta("Almuerzo");
CALL spAgregarCategoriaReceta("Postre");

-- Añadir categorías de ingredientes
CALL spAgregarCategoriaIngrediente("Vegano");
CALL spAgregarCategoriaIngrediente("Celíaco");
CALL spAgregarCategoríaIngrediente("Vegetariano");
CALL spAgregarCategoríaIngrediente("Con azucar");

-- Añadir ingredientes
CALL spAgregarIngrediente("Leche", '[{"cID":1},{"cID":3}]');
CALL spAgregarIngrediente("Azúcar", '[{"cID":1},{"cID":2}]');

-- Subir receta
CALL spSubirReceta (
	'lbotano',
	'Alfajor Jorgito',
    'Es hermoso',
    'Hacelo',
    '[{"iID": 1, "cantidad": 100, "unidadCantidad": "ml"}, {"iID": 2, "cantidad": 200, "unidadCantidad": "g"}]',
    '[{"link":"http://www.google.com/foto.png"}, {"link":"http://www.cuantocabron.com/gracioso.jpg"}]',
    '[{"cID":1},{"cID":2}]'
);

CALL spSubirReceta (
	'jorgelina',
	'Empanada',
    'Se come',
    'Haces empanadas ._.',
    '[{"iID": 2, "cantidad": 4, "unidadCantidad": "kg"}]',
    NULL,
    '[{"cID":1}]'
);