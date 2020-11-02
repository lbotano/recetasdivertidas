USE RecetasDivertidasDB;

DROP PROCEDURE IF EXISTS spDarAdmin;
DELIMITER //
CREATE PROCEDURE spDarAdmin(
	IN nickAdmin varchar(32)
)
BEGIN
    DECLARE existe boolean;
    
    SELECT COUNT(*) > 0
    FROM Usuario
    WHERE uNickname = nickAdmin
    INTO existe;
    
    UPDATE Usuario
    SET uEsAdmin = true
    WHERE uNickname = nickAdmin;
    
    IF NOT existe THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario no existe';
    END IF;
END//
DELIMITER ;