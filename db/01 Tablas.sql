CREATE DATABASE IF NOT EXISTS RecetasDivertidasDB;
USE RecetasDivertidasDB;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS PreguntasSeguridad;
CREATE TABLE PreguntasSeguridad(
	id int NOT NULL AUTO_INCREMENT,
    pregunta varchar(64) NOT NULL,
    CONSTRAINT pk_preguntaSeguridad PRIMARY KEY (id)
);

-- Género:
-- 	0: masculino
-- 	1: femenino
-- 	cualquier otro nro: otro
DROP TABLE IF EXISTS Usuario;
CREATE TABLE Usuario(
	uNickname varchar(32) NOT NULL,
	uPreguntaSeguridad int,
	uRespuestaSeguridad varchar(64) NOT NULL,
	uNombre varchar(50) NOT NULL,
	uApellido varchar(50) NOT NULL,
	uContrasenia varchar(50) NOT NULL,
	uGenero boolean,
	uMail varchar(64) NOT NULL,
    uHabilitado boolean NOT NULL DEFAULT true,
    uEsAdmin boolean NOT NULL DEFAULT false,
	CONSTRAINT pk_u PRIMARY KEY (uNickname),
	CONSTRAINT uk_um UNIQUE (uMail),
    CONSTRAINT fk_usuario_preguntaSeguridad
		FOREIGN KEY (uPreguntaSeguridad)
		REFERENCES PreguntasSeguridad(id)
);

DROP TABLE IF EXISTS Ingrediente;
CREATE TABLE Ingrediente(
	iID int NOT NULL AUTO_INCREMENT,
	iNombre varchar(64) NOT NULL,
	CONSTRAINT pk_i PRIMARY KEY (iID),
    CONSTRAINT uk_i UNIQUE KEY (iNombre)
);

DROP TABLE IF EXISTS CategoriaDeIngrediente;
CREATE TABLE CategoriaDeIngrediente (
	cID int NOT NULL AUTO_INCREMENT,
	cNombre varchar(64) NOT NULL,
	CONSTRAINT pk_ci PRIMARY KEY (cID),
	CONSTRAINT uk_ci UNIQUE (cNombre)
);

DROP TABLE IF EXISTS CategoriaDeReceta;
CREATE TABLE CategoriaDeReceta(
	cID int NOT NULL AUTO_INCREMENT,
	cNombre varchar(64) NOT NULL ,
	CONSTRAINT pk_cr PRIMARY KEY (cID),
    CONSTRAINT uk_ci UNIQUE (cNombre)
);

DROP TABLE IF EXISTS RelCatIngred;
CREATE TABLE RelCatIngred(
	iID int NOT NULL,
	cID int NOT NULL,
	CONSTRAINT pk_rci PRIMARY KEY (iID, cID),
	CONSTRAINT fk_iid FOREIGN KEY (iID) REFERENCES Ingrediente(iID),
	CONSTRAINT fk_cid FOREIGN KEY (cID) REFERENCES CategoriaDeIngrediente(cID) 
);

DROP TABLE IF EXISTS Receta;
CREATE TABLE Receta (
	rID int NOT NULL AUTO_INCREMENT,
	rAutor varchar(32) NOT NULL,
	rNombre varchar(128) NOT NULL,
	rDescripcion text(512) NOT NULL,
    rInstrucciones text(2048) NOT NULL,
	CONSTRAINT pk_r PRIMARY KEY (rID),
    CONSTRAINT fk_r_u FOREIGN KEY (rAutor) REFERENCES Usuario(uNickname)
);

DROP TABLE IF EXISTS RelCatReceta;
CREATE TABLE RelCatReceta (
	rID int,
	cID int,
	CONSTRAINT pk_rcr PRIMARY KEY (rID, cID),
	CONSTRAINT fk_rcrrID FOREIGN KEY (rID) REFERENCES Receta (rID),
	CONSTRAINT fk_rcrcID FOREIGN KEY (cID) REFERENCES CategoriaDeReceta(cID)
);

DROP TABLE IF EXISTS IngredienteReceta;
CREATE TABLE IngredienteReceta (
	rID int NOT NULL,
	iID int NOT NULL,
	cantidad int NOT NULL DEFAULT 1,
	unidadCantidad varchar(32) NULL,
	CONSTRAINT pk_ir PRIMARY KEY (rID, iID),
	CONSTRAINT fk_irrID FOREIGN KEY (rID) REFERENCES Receta (rID),
	CONSTRAINT fk_iriID FOREIGN KEY (iID) REFERENCES Ingrediente(iID)
);

DROP TABLE IF EXISTS Multimedia;
CREATE TABLE Multimedia (
	mID int NOT NULL AUTO_INCREMENT,
	link text NOT NULL,
	rID int NOT NULL,
	CONSTRAINT pk_mID PRIMARY KEY (mID),
	CONSTRAINT fk_mrID FOREIGN KEY (rID) REFERENCES Receta(rID)
);

DROP TABLE IF EXISTS Calificacion;
CREATE TABLE Calificacion (
	uNickname varchar(32) NOT NULL,
	rID int NOT NULL,
	calificacion tinyint NOT NULL,
	CONSTRAINT pk_c PRIMARY KEY (uNickname, rID),
	CONSTRAINT fk_cunick FOREIGN KEY (uNickname) references Usuario(uNickname),
	CONSTRAINT fk_crid FOREIGN KEY (rID) references Receta(rID)
	
);

SET FOREIGN_KEY_CHECKS = 1;