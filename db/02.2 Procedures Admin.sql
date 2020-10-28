USE RecetasDivertidasDB;

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
	nombre varchar(64),
    categorias json
)
BEGIN
	DECLARE idIngrediente int;
    
    DECLARE EXIT HANDLER FOR sqlexception
    BEGIN
		ROLLBACK;
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado';
	END;
    
    DECLARE EXIT HANDLER FOR sqlwarning
    BEGIN
		ROLLBACK;
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado';
	END;
    
    SET autocommit = 0;
    
    START TRANSACTION;
    
    -- Agregar ingrediente
    INSERT INTO Ingrediente (iNombre)
    VALUES (nombre);
    
    -- Asignarle las categorías
    SELECT LAST_INSERT_ID() INTO idIngrediente;
    
    DROP TEMPORARY TABLE IF EXISTS relCategoriaIngrediente;
    CREATE TEMPORARY TABLE relCategoriaIngrediente
    SELECT *
    FROM JSON_TABLE(
		categorias,
		'$[*]' COLUMNS (
			cID int PATH '$.cID'
        )
	) AS jt;
    
    INSERT INTO RelCatIngred (
		iID,
		cID
    )
    SELECT
		idIngrediente,
        cID
	FROM relCategoriaIngrediente;
    
    COMMIT;
    SET autocommit = 1;
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
	DECLARE existeUsuario boolean;
    
    -- Averiguar si el usuario existe
	SELECT COUNT(*) > 0
    INTO existeUsuario
	FROM Usuario
    WHERE uNickname = puNickname;
    
    IF NOT existeUsuario THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Este usuario no existe';
    ELSE
		UPDATE Usuario
		SET uHabilitado = false
		WHERE uNickname = puNickname;
    END IF;
END//
DELIMITER ;

-- Borrar receta (como usuario, sólo se puede borrar la receta de uno mismo)
DROP PROCEDURE IF EXISTS spAdminBorrarReceta;
DELIMITER //
CREATE PROCEDURE spAdminBorrarReceta (
	IN idReceta int -- ID de la receta que el usuario quiere borrar
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado al borrar receta';
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado al borrar receta';
    END;
	
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

DROP PROCEDURE IF EXISTS spBorrarCategoriaIngrediente;
DELIMITER //
CREATE PROCEDURE spBorrarCategoriaIngrediente (
	pcID int
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		ROLLBACK;
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado';
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
		ROLLBACK;
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado';
    END;
    
    SET autocommit = 0;
    START TRANSACTION;
    
    DELETE FROM RelCatIngred
    WHERE cID = pcID;
    
    DELETE FROM CategoriaDeIngrediente
    WHERE cID = pcID;
    
    COMMIT;
    SET autocommit = 1;
END//
DELIMITER ;

DROP PROCEDURE IF EXISTS spBorrarCategoriaReceta;
DELIMITER //
CREATE PROCEDURE spBorrarCategoriaReceta (
	pcID int
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		ROLLBACK;
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado';
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
		ROLLBACK;
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado';
    END;
    
    SET autocommit = 0;
    START TRANSACTION;
    
    DELETE FROM RelCatReceta
    WHERE cID = pcID;
    
    DELETE FROM CategoriaDeReceta
    WHERE cID = pcID;
    
    COMMIT;
    SET autocommit = 1;
END//
DELIMITER ;