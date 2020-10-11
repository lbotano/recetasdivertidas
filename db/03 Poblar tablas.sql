USE RecetasDivertidasDB;

-- Truncar tablas
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE PreguntasSeguridad;
TRUNCATE TABLE Usuario;
TRUNCATE TABLE CategoriaDeReceta;
TRUNCATE TABLE CategoriaDeIngrediente;
TRUNCATE TABLE Ingrediente;
TRUNCATE TABLE RelCatIngred;
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
    @resultado);
    
-- Añadir categorías de recetas
CALL spAgregarCategoriaReceta ("Almuerzo");
CALL spAgregarCategoriaReceta ("Postre");

-- Añadir categorías de ingredientes
CALL spAgregarCategoriaIngrediente ("Vegano");
CALL spAgregarCategoriaIngrediente ("Celíaco");

-- Añadir ingredientes
CALL spAgregarIngrediente("Leche");
CALL spAgregarIngrediente("Azúcar");

-- Asignar categorias a ingredientes
CALL spAsignarCategoriaIngrediente(1, 2);
CALL spAsignarCategoriaIngrediente(2, 1);
CALL spAsignarCategoriaIngrediente(2, 2);
CALL spAsignarCategoriaIngrediente(1, 5);