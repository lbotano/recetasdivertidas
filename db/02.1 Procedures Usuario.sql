USE RecetasDivertidasDB;

-- Obtiene la calificación promedio de la receta
DROP FUNCTION IF EXISTS fnGetCalificacionReceta;
DELIMITER //
CREATE FUNCTION fnGetCalificacionReceta(
	idReceta int
) RETURNS int
READS SQL DATA
BEGIN
	DECLARE resultado int;
    SELECT AVG(c.calificacion)
    FROM Receta r, Calificacion c
    WHERE
		r.rID = c.rID AND
        r.rID = idReceta
	GROUP BY r.rID
	INTO resultado;
    
    SELECT IF(resultado IS NULL, 0, resultado) INTO resultado;
    
    RETURN resultado;
END//
DELIMITER ;

 -- Obtiene la cantidad de calificaciones que tuvo una receta
DROP FUNCTION IF EXISTS fnGetCalificacionesReceta;
DELIMITER //
CREATE FUNCTION fnGetCalificacionesReceta(
	idReceta int
) RETURNS int
READS SQL DATA
BEGIN
	DECLARE resultado int;
    SELECT COUNT(r.rID)
    FROM Receta r, Calificacion c
    WHERE
		r.rID = c.rID AND
        r.rID = idReceta
	INTO resultado;
    
    RETURN resultado;
END//;
DELIMITER ;

-- Obtiene la calificacion que le puso un usuario a una receta
DROP FUNCTION IF EXISTS fnGetCalificacionPorUsuario;
DELIMITER //
CREATE FUNCTION fnGetCalificacionPorUsuario(
    idReceta int,
	nickname varchar(32)
) RETURNS float
READS SQL DATA
BEGIN
	DECLARE rCalificacion float;
	SELECT c.calificacion
    FROM Calificacion c
    WHERE
		c.uNickname = nickname AND
        c.rID = idReceta
	INTO rCalificacion;
    
    RETURN rCalificacion;
END//
DELIMITER ;

-- Registro de usuarios --
-- Devuelve un True si el usuario se creó con éxito
DROP PROCEDURE IF EXISTS spRegistroUsuario;
DELIMITER //
CREATE PROCEDURE spRegistroUsuario (
	IN nickname varchar(32),
	IN preguntaSeguridad int,
	IN respuestaSeguridad varchar(64),
	IN nombre varchar(50),
	IN apellido varchar(50),
	IN contrasenia varchar(50),
	IN genero tinyint,
	IN mail varchar(64),
    IN esAdmin boolean,
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
            uMail,
            uEsAdmin)
		VALUES 
			(nickname,
			preguntaSeguridad,
			UPPER(respuestaSeguridad),
			nombre,
			apellido,
			contrasenia,
			genero,
			mail,
            esAdmin);
		SELECT true INTO resultado;
	ELSE
		SELECT false INTO resultado;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Este usuario ya existe.';
	END IF;
END//
DELIMITER ;

-- Inicio sesion --
-- Devuelve:
-- 	resultado: si se logueó correctamente
-- 	esAdmin: si el usuario es admin
DROP PROCEDURE IF EXISTS spInicioSesion;
DELIMITER //
CREATE PROCEDURE spInicioSesion (
	IN puNickname varchar(32),
	IN puContrasenia varchar(50),
	OUT resultado boolean,
    OUT esAdmin boolean
)
BEGIN
	DECLARE contraDB varchar(50);
	DECLARE cantUsuarios int;
    DECLARE estaBaneado boolean;
    
    -- Por defecto devuelve 0
    SELECT false INTO resultado;
    
	SELECT COUNT(*)
    INTO cantUsuarios
    FROM Usuario
    WHERE uNickname = puNickname;
    
    -- Ver si el usuario no está baneado
    SELECT NOT uHabilitado
    INTO estaBaneado
    FROM Usuario
    WHERE uNickname = puNickname;
    
	-- Si no encuentra un usuario, devuelve 0
	IF cantUsuarios = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Este usuario no existe.';
	ELSEIF estaBaneado THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cuenta está suspendida.';
	ELSE
        SELECT uContrasenia
        INTO contraDB
        FROM Usuario
        WHERE uNickname = puNickname
        LIMIT 1;
        
		IF contraDB = puContrasenia THEN
			SELECT true INTO resultado;
            SELECT uEsAdmin INTO esAdmin FROM Usuario WHERE uNickname = puNickname;
		ELSE
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Contraseña incorrecta';
		END IF;
	END IF;
END//
DELIMITER ;

-- Subir receta --
DROP PROCEDURE IF EXISTS spSubirReceta;
DELIMITER //
CREATE PROCEDURE spSubirReceta (
	autor varchar(32),
    nombre varchar(128), -- Nombre de la receta
    descripcion text(512), -- Descripción de la receta
    instrucciones text(2048), -- Instrucciones de la receta
    ingredientes json, -- Los ingredientes (en JSON): iID:number, cantidad:number y unidadCantidad:string
    multimedia json, -- Los medios audiovisiales de la receta (en JSON): link:string
    categoriasReceta json
)
BEGIN
	DECLARE idReceta INT;
    
    DECLARE exit handler for sqlexception
	BEGIN
        ROLLBACK;
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado.';
	END;
    
    DECLARE exit handler for sqlwarning
	BEGIN
        ROLLBACK;
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado.';
	END;
    
    SET autocommit=0;
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
    DROP TEMPORARY TABLE IF EXISTS tmpIngRec;
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
    
    -- Asignar los multimedios
    DROP TEMPORARY TABLE IF EXISTS tmpMultimedia;
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
    
    -- Asignar las categorías de receta
    DROP TEMPORARY TABLE IF EXISTS tmpCategoriasReceta;
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
    
    SET autocommit=1;
    COMMIT;
END//
DELIMITER ;

-- Obtener las id de las recetas de un usuario---
-- Devuelve -1 si el usuario no existe
DROP PROCEDURE IF EXISTS spGetRecetasUsuario;
DELIMITER //
CREATE PROCEDURE spGetRecetasUsuario (
	uNickname varchar(32)
)
BEGIN
	SELECT
		rID,
        rAutor,
        rNombre,
        rDescripcion,
        fnGetCalificacionReceta(rID),
        fnGetCalificacionesReceta(rID)
	FROM Receta WHERE rAutor = uNickname;
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
CREATE PROCEDURE spGetDatosReceta (
	prID int,
    pUsuario nvarchar(32)
)
BEGIN
	-- Obtener los datos de la receta
    SELECT *, fnGetCalificacionReceta(rID), fnGetCalificacionesReceta(rID), fnGetCalificacionPorUsuario(prID, pUsuario)
    FROM Receta
    WHERE rID = prID;
    
    -- Seleccionar ingredientes
	SELECT i.iID, i.iNombre, ir.cantidad, ir.unidadCantidad
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
	SELECT COUNT(*)
    FROM (
		SELECT DISTINCT iID
        FROM IngredienteReceta ir
        WHERE ir.rID = prID) t
	INTO @cantIngredientes;
    
    SELECT c.cID, cNombre FROM RelCatIngred rci
    INNER JOIN CategoriaDeIngrediente c
    ON rci.cID = c.cID
	GROUP BY c.cID, c.cNombre
	HAVING COUNT(iID) = @cantIngredientes;
	
    -- Seleccionar multimedia
    SELECT mID, link
	FROM Multimedia m
    WHERE m.rID = prID;
    
END//
DELIMITER ;

-- Listar usuarios o datos de un usuario --
DROP PROCEDURE IF EXISTS spDatosUsuario;
DELIMITER //
CREATE PROCEDURE spDatosUsuario (
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

-- Calificar receta --
DROP PROCEDURE IF EXISTS spCalificarReceta;
DELIMITER //
CREATE PROCEDURE spCalificarReceta (
	puNickname varchar(32),
	prID int,
	pCalificacion tinyint
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado';
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado';
    END;
	-- Se borra la calificación anterior (si es que la tiene)
    -- por si el usuario quiso cambiarla
	DELETE FROM Calificacion
    WHERE
		rID = prID AND
        uNickname = puNickname;
	
    INSERT INTO Calificacion
    VALUES (puNickname, prID, pCalificacion);
END//
DELIMITER ;

DROP PROCEDURE IF EXISTS spCambiarContrasena;
DELIMITER //
CREATE PROCEDURE spCambiarContrasena (
	nickUsuario varchar(32),
    respuesta varchar(64),
    nuevaContrasenia varchar(50)
)
BEGIN
	UPDATE Usuario
    SET uContrasenia = nuevaContrasenia
    WHERE
		uNickname = nickUsuario AND
        uRespuestaSeguridad = UPPER(respuesta);
	
    IF ROW_COUNT() = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Respuesta de seguridad incorrecta";
    END IF;
END//
DELIMITER ;

-- Buscar recetas (por ingredientes)
DROP PROCEDURE IF EXISTS spBuscarRecetasPorIngr;
DELIMITER //
CREATE PROCEDURE spBuscarRecetasPorIngr (
	IN ingredientes JSON, -- JSON de un array de ingredientes (tienen que ser en string) Ej: ["1","4","3"]
    IN pagina int -- Página de la búsqueda (0, 1, 2, etc)
)
BEGIN
    SET @pagina = pagina;
    SET @paginaHasta = pagina + 10;
    SET @ingredientes = ingredientes;
    
    PREPARE stmt FROM 'SELECT
			rID,
			rAutor,
			rNombre,
            rDescripcion,
            fnGetCalificacionReceta(rID),
            fnGetCalificacionesReceta(rID)
		FROM (
			SELECT
				r.rID,
				r.rAutor,
				r.rNombre,
                r.rDescripcion,
				SUM(JSON_LENGTH(JSON_SEARCH(@ingredientes, \'all\', CONVERT(i.iID, char)))) AS coincidencias
			FROM
				Receta r,
				Ingrediente i,
				IngredienteReceta ir
			WHERE
				r.rID = ir.rID AND
				i.iID = ir.iID
			GROUP BY r.rID
			HAVING
				coincidencias > 0
		) resultadosConOrden
		ORDER BY coincidencias DESC
        LIMIT ?, ?';
    EXECUTE stmt USING @pagina, @paginaHasta;
    
END//
DELIMITER ;

-- Buscar recetas (por categorías de receta)
DROP PROCEDURE IF EXISTS spBuscarRecetasPorCatReceta;
DELIMITER //
CREATE PROCEDURE spBuscarRecetasPorCatReceta (
	IN categorias JSON, -- JSON de un array de categorías (tienen que ser en string) Ej: ["1","4","3"]
    IN pagina int -- Página de la búsqueda (0, 1, 2, 3, etc)
)
BEGIN
    SET @pagina = pagina;
    SET @paginaHasta = pagina + 10;
    SET @categorias = categorias;
    
    PREPARE stmt FROM 'SELECT
			rID,
			rAutor,
			rNombre,
            rDescripcion,
            fnGetCalificacionReceta(rID),
            fnGetCalificacionesReceta(rID)
		FROM (
			SELECT
				r.rID,
				r.rAutor,
				r.rNombre,
                r.rDescripcion,
				SUM(JSON_LENGTH(JSON_SEARCH(@categorias, \'all\', CONVERT(c.cID, char)))) AS coincidencias
			FROM
				Receta r,
				CategoriaDeReceta c,
				RelCatReceta rc
			WHERE
				r.rID = rc.rID AND
				c.cID = rc.cID
			GROUP BY r.rID
			HAVING
				coincidencias > 0
		) resultadosConOrden
		ORDER BY coincidencias DESC
        LIMIT ?, ?';
    EXECUTE stmt USING @pagina, @paginaHasta;
    
END//
DELIMITER ;

-- Buscar recetas (por categorías de ingredientes)
DROP PROCEDURE IF EXISTS spBuscarRecetasPorCatIngr;
DELIMITER //
CREATE PROCEDURE spBuscarRecetasPorCatIngr (
	IN categorias JSON, -- JSON de un array de categorías (tienen que ser en string) Ej: ["1","4","3"]
    IN pagina int -- Página de la búsqueda (0, 1, 2, 3, etc)
)
BEGIN
    SET @pagina = pagina;
    SET @paginaHasta = pagina + 10;
    SET @categorias = categorias;
    
    PREPARE stmt FROM 'SELECT
		rID,
		rAutor,
		rNombre,
        rDescripcion,
        fnGetCalificacionReceta(rID),
        fnGetCalificacionesReceta(rID)
	FROM (
		SELECT
			r.rID,
			r.rAutor,
			r.rNombre,
            r.rDescripcion,
			JSON_SEARCH(@categorias, \'all\', CONVERT(c.cID, char)) AS coincidencias
		FROM
			Receta r,
			IngredienteReceta ir,
			Ingrediente i,
			RelCatIngred ic,
			CategoriaDeIngrediente c
		WHERE
			r.rID = ir.rID AND
			ir.iID = i.iID AND
			i.iID = ic.iID AND
			ic.cID = c.cID
		GROUP BY r.rID, coincidencias
		HAVING
			COUNT(coincidencias) > 0
	) resultadosConOrden
	GROUP BY rID
	ORDER BY COUNT(coincidencias) DESC
	LIMIT ?, ?;';
    EXECUTE stmt USING @pagina, @paginaHasta;
    
END//
DELIMITER ;

-- Buscar recetas por texto
DROP PROCEDURE IF EXISTS spBuscarRecetasPorTexto;
DELIMITER //
CREATE PROCEDURE spBuscarRecetasPorTexto (
	texto TEXT, -- El texto a buscar
    pagina int
)
BEGIN
	DECLARE patron text;
    DECLARE siguientePagina int;
    SELECT CONCAT('%', texto, '%') INTO patron;
    SELECT pagina + 10 INTO siguientePagina;
    
	SELECT
		rID,
        rAutor,
        rNombre,
        rDescripcion,
		fnGetCalificacionReceta(rID),
        fnGetCalificacionesReceta(rID)
	FROM Receta
    WHERE
		rNombre LIKE patron OR
        rDescripcion LIKE patron OR
        rInstrucciones LIKE patron
	LIMIT pagina, siguientePagina;
END//
DELIMITER ;

-- Borrar receta (como usuario, sólo se puede borrar la receta de uno mismo)
DROP PROCEDURE IF EXISTS spUsuarioBorrarReceta;
DELIMITER //
CREATE PROCEDURE spUsuarioBorrarReceta (
	IN idReceta int, -- ID de la receta que el usuario quiere borrar
    IN nickname varchar(32) -- Nickname del usuario que quiere borrar la receta
)
BEGIN
	DECLARE usuarioDeReceta varchar(32); -- El verdadero usuario que hizo la receta que se quiere borrar
    
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado al borrar receta';
        ROLLBACK;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado al borrar receta';
        ROLLBACK;
    END;
    
    -- Verificar que el usuario este borrando una receta suya
    SELECT rAutor
    INTO usuarioDeReceta
    FROM Receta
    WHERE rID = idReceta;
    
    IF usuarioDeReceta != nickname THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: Estás tratando de borrar una receta que no es tuya';
    END IF;
	
    -- Borrar la receta
	SET autocommit = 0;
    START TRANSACTION;
    
	DELETE FROM RelCatReceta
    WHERE rID = idReceta;
    
    DELETE FROM Multimedia
    WHERE rID = idReceta;
    
    DELETE FROM IngredienteReceta
    WHERE rID = idReceta;
    
    DELETE FROM Calificacion
    WHERE rID = idReceta;
    
    DELETE FROM Receta
    WHERE rID = idReceta;
    
    COMMIT;
    SET autocommit = 1;
END//
DELIMITER ;

DROP PROCEDURE IF EXISTS spSeleccionarIngredientes;
DELIMITER //
CREATE PROCEDURE spSeleccionarIngredientes()
BEGIN
	SELECT
		i.iID,
        i.INombre,
        c.cID,
        c.cNombre
    FROM
		Ingrediente i,
		RelCatIngred ic,
        CategoriaDeIngrediente c
	WHERE
        i.iID = ic.iID AND
        ic.cID = c.cID
	ORDER BY i.INombre;
END//
DELIMITER ;

DROP PROCEDURE IF EXISTS spSeleccionarTopRecetas;
DELIMITER //
CREATE PROCEDURE spSeleccionarTopRecetas(
	pagina int
)
BEGIN
	DECLARE paginaActual int;
	DECLARE paginaSiguiente int;
    SELECT pagina * 10 INTO paginaActual;
    SELECT paginaActual + 10 INTO paginaSiguiente;
    
	SELECT
		rID,
		rAutor,
		rNombre,
        rDescripcion,
        fnGetCalificacionReceta(rID),
        fnGetCalificacionesReceta(rID)
    FROM Receta
    ORDER BY fnGetCalificacionReceta(rID) DESC, fnGetCalificacionesReceta(rID) DESC
    LIMIT paginaActual, paginaSiguiente;
END//
DELIMITER ;