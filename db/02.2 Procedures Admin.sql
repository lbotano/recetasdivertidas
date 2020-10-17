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