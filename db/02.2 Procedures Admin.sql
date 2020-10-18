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
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado';
        ROLLBACK;
	END;
    
    DECLARE EXIT HANDLER FOR sqlwarning
    BEGIN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error inesperado';
        ROLLBACK;
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
    SET autocommit=1;
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