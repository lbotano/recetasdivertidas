USE RecetasDivertidasDB;

-- Registro de usuarios --
-- Devuelve un True si el usuario se creó con éxito
DROP PROCEDURE IF EXISTS spRegistroUsuario;
DELIMITER //
CREATE PROCEDURE spRegistroUsuario
(
	IN uNickname varchar(32),
	IN uPreguntaSeguridad int,
	IN uRespuestaSeguridad varchar(64),
	IN uNombre varchar(50),
	IN uApellido varchar(50),
	IN uContrasenia varchar(50),
	IN uGenero tinyint,
	IN uMail varchar(64),
	OUT resultado boolean
)
BEGIN
	DECLARE existeUsuario int;
	SET existeUsuario = 
		(
			SELECT COUNT(*)
            FROM Usuario
			WHERE uNickname = uNickname
		);
	IF existeUsuario = 0 THEN
		INSERT INTO Usuario
			(uNickname,
            uPreguntaSeguridad,
            uRespuestaSeguridad,
            uNombre,
            uApellido,
            uContrasenia,
            uGenero,
            uMail)
		VALUES 
			(uNickname,
			uPreguntaSeguridad,
			uRespuestaSeguridad,
			uNombre,
			uApellido,
			uContrasenia,
			uGenero,
			uMail);
		SELECT true INTO resultado;
	ELSE
		SELECT false INTO resultado;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Este usuario ya existe.';
	END IF;
END//

DELIMITER ;

-- Inicio sesion --
-- Devuelve:
-- 	0 si el usuario ingresado no existe
--  1 si el inicio de sesión es exitoso
--  2 si la contraseña es la incorrecta
DROP PROCEDURE IF EXISTS spInicioSesion;
DELIMITER //
CREATE PROCEDURE spInicioSesion
(	
	IN puNickname varchar(32),
	IN puContrasenia varchar(50),
	OUT resultado tinyint
)
BEGIN
	DECLARE contraDB varchar(50);
	DECLARE cantUsuarios int;
    
	SELECT COUNT(*)
    INTO cantUsuarios
    FROM Usuario
    WHERE uNickname = puNickname;
	-- Si no encuentra un usuario, devuelve 0
	IF cantUsuarios = 0 THEN
		SELECT 0 into resultado;
	ELSE
        SELECT uContrasenia
        INTO contraDB
        FROM Usuario
        WHERE uNickname = puNickname
        LIMIT 1;
        
		IF contraDB = puContrasenia THEN
			SELECT 1 INTO resultado;
		ELSE
            SELECT 2 INTO resultado;
		END IF;
	END IF;
END//
DELIMITER ;

-- Obtener las id de las recetas de un usuario---
-- Devuelve -1 si el usuario no existe
DROP PROCEDURE IF EXISTS spGetRecetasUsuario;
DELIMITER //
CREATE PROCEDURE spGetRecetasUsuario
(
	uNickname varchar(32)
)
BEGIN
	SELECT rID AS nroReceta, rNombre AS Nombre, rDescripcion AS Descripcion 
	FROM Receta WHERE uNickname = @uNickname;
END//
DELIMITER ;

-- Obtener todos los datos de una receta
-- Devuelve varios resultsets
-- 1. Datos básicos de la receta
-- 2. Ingredientes de la receta
-- 3. Categorías de la receta
-- 4. Categorías de sus ingredientes
-- 5. Multimedia
DROP PROCEDURE IF EXISTS spGetDatosReceta;
DELIMITER //
CREATE PROCEDURE spGetDatosReceta
(
	prID int
)
BEGIN
	-- Obtener los datos de la receta
    SELECT *
    FROM Receta
    WHERE rID = prID;
    
    -- Seleccionar ingredientes
	SELECT i.iID, i.iNombre
	FROM
		Ingrediente i,
		IngredienteReceta ir,
		Receta r
	WHERE
		r.rID = prID AND
		r.rID = ir.rID AND
		i.iID = ir.iID;	
	
    -- Seleccionar categorías de la receta
    SELECT c.cID, c.cNombre
    FROM
		CategoriaDeReceta c,
        RelCatReceta cr,
        Receta r
	WHERE
		c.cID = cr.cID AND
        r.rID = cr.rID AND
        r.rID = prID;
	
    -- Seleccionar categorías los ingredientes
	SELECT DISTINCT
		c.cID,
        c.cNombre
    FROM
		CategoriaDeIngrediente c,
        RelCatIngrediente cr,
        Ingrediente i,
        IngredienteReceta ir
	WHERE
		i.iID = cr.iID AND
        c.cID = cr.cID AND
        i.iID = ir.iID AND
        r.rID = ir.rID AND
        ir.rID = prID;
	
    -- Seleccionar multimedia
    SELECT mID, link
	FROM Multimedia
    WHERE rID = prID;
END//
DELIMITER ;

-- Listar usuarios o datos de un usuario --
DROP PROCEDURE IF EXISTS spDatosUsuario;
DELIMITER //
CREATE PROCEDURE spDatosUsuario
(
	puNickname varchar(32)
)
BEGIN
	SELECT
		uNickname,
		uNombre,
		uApellido,
		uGenero,
		uMail,
		uHabilitado
	FROM Usuario
	WHERE uNickname = puNickname;
END//
DELIMITER ;

-- Banear usuario --
DROP PROCEDURE IF EXISTS spBaneoUsuario;
DELIMITER //
CREATE PROCEDURE spBaneoUsuario
(
	puNickname varchar(32)
)
BEGIN
	UPDATE Usuario
    SET uHabilitado = false
    WHERE uNickname = puNichname;
END//
DELIMITER ;

-- Calificar receta --
DROP PROCEDURE IF EXISTS spCalificarReceta;
DELIMITER //
CREATE PROCEDURE spCalificarReceta
(
	prID int,
	puNickname varchar(32),
	pcalificacion tinyint
)
BEGIN
	-- Se borra la calificación anterior (si es que la tiene)
    -- por si el usuario quiso cambiarla
	DELETE FROM Calificacion
    WHERE
		rID = prID AND
        uNickname = puNickname;
	
    INSERT INTO Calificacion
    VALUES (puNickname, prID, pcalificacion);
END//
DELIMITER ;

-- Buscar recetas (por ingredientes)
-- Obtiene una lista de ingredientes (como string)
--  Formato del string: (ingrediente), (ingrediente), ...
-- Obtiene la página donde de la búsqueda
-- Devuelve las ids de las recetas que se pueden preparar
-- con esos ingrdientes
DROP PROCEDURE IF EXISTS spBuscarRecetaPorIngr;
DELIMITER //
CREATE PROCEDURE spBuscarRecetaPorIngr
(
	IN ingredientes varchar(512),
    IN pagina int
)
BEGIN
	-- DROP TEMPORARY TABLE IF EXISTS IngredientesBusqueda;
	-- SET @sql = CONCAT('CREATE TEMPORARY TABLE IngredientesBusqueda AS
	SET @sql = CONCAT('
    SELECT r.rID
    FROM
		Receta r,
        Ingrediente i,
        IngredienteReceta ir
	WHERE
		r.rID = ir.rID AND
        i.iID = ir.iID AND
        i.iNombre IN (', ingredientes, ')
	LIMIT 10
    OFFSET ', pagina, '
    ORDER BY r.rID DESC;');
    
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END//
DELIMITER ;