USE RecetasDivertidasDB;

-- Registro de usuarios --
-- Devuelve un True si el usuario se creó con éxito
DROP PROCEDURE IF EXISTS spRegistroUsuario;
DELIMITER //
CREATE PROCEDURE spRegistroUsuario
(
	IN nickname varchar(32),
	IN preguntaSeguridad int,
	IN respuestaSeguridad varchar(64),
	IN nombre varchar(50),
	IN apellido varchar(50),
	IN contrasenia varchar(50),
	IN genero tinyint,
	IN mail varchar(64),
	OUT resultado boolean
)
BEGIN
	DECLARE existeUsuario int;
	SET existeUsuario = 
		(
			SELECT COUNT(*)
            FROM Usuario
			WHERE uNickname = nickname
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
			(nickname,
			preguntaSeguridad,
			respuestaSeguridad,
			nombre,
			apellido,
			contrasenia,
			genero,
			mail);
		SELECT true INTO resultado;
	ELSE
		SELECT false INTO resultado;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Este usuario ya existe.';
	END IF;
END//
DELIMITER ;

-- Inicio sesion --
-- Devuelve:
-- 	true: si se logueó correctamente.
--  false: si hubo un error al loguearse.
DROP PROCEDURE IF EXISTS spInicioSesion;
DELIMITER //
CREATE PROCEDURE spInicioSesion
(	
	IN puNickname varchar(32),
	IN puContrasenia varchar(50),
	OUT resultado boolean
)
BEGIN
	DECLARE contraDB varchar(50);
	DECLARE cantUsuarios int;
    
    -- Por defecto devuelve 0
    SELECT false INTO resultado;
    
	SELECT COUNT(*)
    INTO cantUsuarios
    FROM Usuario
    WHERE uNickname = puNickname;
	-- Si no encuentra un usuario, devuelve 0
	IF cantUsuarios = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Este usuario no existe.';
	ELSE
        SELECT uContrasenia
        INTO contraDB
        FROM Usuario
        WHERE uNickname = puNickname
        LIMIT 1;
        
		IF contraDB = puContrasenia THEN
			SELECT true INTO resultado;
		ELSE
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Contraseña incorrecta';
		END IF;
	END IF;
END//
DELIMITER ;

-- Añadir categoría de receta (Solo para admin)
DROP PROCEDURE IF EXISTS spAgregarCategoriaReceta;
DELIMITER //
CREATE PROCEDURE spAgregarCategoriaReceta
(
	IN nombre varchar(64)
)
BEGIN
	DECLARE EXIT HANDLER FOR 1062
    BEGIN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Esta categoría ya existe';
    END;
    
    INSERT INTO CategoriaDeReceta (cNombre)
    VALUES (nombre);
END//
DELIMITER ;

-- Añadir categoría de ingrediente (Solo para admin)
DROP PROCEDURE IF EXISTS spAgregarCategoriaIngrediente;
DELIMITER //
CREATE PROCEDURE spAgregarCategoriaIngrediente
(
	IN nombre varchar(50)
)
BEGIN
	DECLARE EXIT HANDLER FOR 1062
    BEGIN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Esta categoría ya existe';
	END;
    
    INSERT INTO CategoriaDeIngrediente (cNombre)
    VALUES (nombre);
END//
DELIMITER ;

-- Añadir ingrediente
-- Luego de este SP hay que llamar a spAsignarCategoriaIngrediente
DROP PROCEDURE IF EXISTS spAgregarIngrediente;
DELIMITER //
CREATE PROCEDURE spAgregarIngrediente(
	nombre varchar(64)
)
BEGIN
	DECLARE EXIT HANDLER FOR 1062
    BEGIN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Este ingrediente ya existe';
	END;
    
    INSERT INTO Ingrediente (iNombre)
    VALUES (nombre);
END//
DELIMITER ;

-- Asociar categoria de ingrediente a un ingrediente
-- Parámetros:
--  iID: ID del ingrediente
--  cID: ID de la categoría del ingrediente
DROP PROCEDURE IF EXISTS spAsignarCategoriaIngrediente;
DELIMITER //
CREATE PROCEDURE spAsignarCategoriaIngrediente (
	iID int,
    cID int
)
BEGIN
	DECLARE EXIT HANDLER FOR 1062
    BEGIN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ingrediente ya es de esta categoría.';
	END;
    
    DECLARE EXIT HANDLER FOR 1452
    BEGIN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ingrediente o la categoría no existen.';
    END;
    
	INSERT INTO RelCatIngred
    VALUES (iID, cID);
END//
DELIMITER ;

-- Subir receta --
-- Parámetros:
-- 	nombre: nombre de la receta
--  descripcion: descripcion de la receta
--  instrucciones: instrucciones de la receta
--  ingredientes: ingredientes en JSON
-- 		iID number: ID del Ingrediente
--      cantidad int: Cantidad necesaria del ingrediente
-- 		unidadCantidad string: Unidad de medida de la cantidad
--  multimedia: medios audiovisuales de la receta
-- 		iID
DROP PROCEDURE IF EXISTS spSubirReceta;
DELIMITER //
CREATE PROCEDURE spSubirReceta
(
	autor varchar(32),
    nombre varchar(128),
    descripcion text(512),
    instrucciones text(2048),
    ingredientes text,
    multimedia text,
    categoriasReceta text
)
BEGIN
	DECLARE idReceta INT;
    
    DECLARE exit handler for sqlexception
		BEGIN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado.';
        ROLLBACK;
	END;
    
    DECLARE exit handler for sqlwarning
		BEGIN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado.';
        ROLLBACK;
	END;
    
    START TRANSACTION;
    
	-- Añadir la receta a la tabla
	INSERT INTO Receta (
		rNombre,
        rAutor,
        rDescripcion,
        rInstrucciones
	)
    VALUES (
		nombre,
        autor,
        descripcion,
        instrucciones
    );
    
    -- Guardar el ID de la nueva receta
    SELECT LAST_INSERT_ID() INTO idReceta;
    
    -- Asignar los ingredientes
    CREATE TEMPORARY TABLE tmpIngRec
    SELECT *
    FROM JSON_TABLE(
		ingredientes,
        '$[*]' COLUMNS(
            iID int PATH '$.iID',
            cantidad int PATH '$.cantidad',
            unidadCantidad varchar(16) PATH '$.unidadCantidad'
		)
	) as jt;
    
    INSERT INTO IngredienteReceta (
		rID,
        iID,
        cantidad,
        unidadCantidad
	)
    SELECT
		idReceta AS rID,
        iID,
        cantidad,
        unidadCantidad
	FROM tmpIngRec;
    
    DROP TEMPORARY TABLE IF EXISTS tmpIngRec;
    
    -- Asignar los multimedios
    CREATE TEMPORARY TABLE tmpMultimedia
    SELECT *
    FROM JSON_TABLE(
		multimedia,
        '$[*]' COLUMNS (
            link text PATH '$.link'
		)
	) as jt;
    
    INSERT INTO Multimedia (
        link,
        rID
	)
    SELECT
        link,
        idReceta AS rID
	FROM tmpMultimedia;
    
    DROP TEMPORARY TABLE IF EXISTS tmpMultimedia;
    
    -- Asignar las categorías de receta
    CREATE TEMPORARY TABLE tmpCategoriasReceta
    SELECT *
    FROM JSON_TABLE(
		categoriasReceta,
        '$[*]' COLUMNS (
			cID int PATH '$.cID'
		)
	) as jt;
    
    INSERT INTO RelCatReceta (
		rID,
        cID
	)
    SELECT
		idReceta AS rID,
        cID
	FROM tmpCategoriasReceta;
    
    COMMIT;
    
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