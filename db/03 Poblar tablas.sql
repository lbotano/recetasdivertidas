USE RecetasDivertidasDB;

-- Truncar tablas
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE PreguntasSeguridad;
TRUNCATE TABLE Usuario;
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