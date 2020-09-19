--CREATE DATABASE RecetasDivertidasDB;
USE RecetasDivertidasDB;

CREATE TABLE Usuario(
	uNickname varchar(32) NOT NULL,
	uPreguntaSeguridad varchar(64) NOT NULL,
	uRespuestaSeguridad varchar(64) NOT NULL,
	uNombre varchar NOT NULL,
	uApellido varchar NOT NULL,
	uContrasenia varchar(50) NOT NULL,
	uGenero bit,
	uMail varchar(64) NOT NULL,
	CONSTRAINT pk_u PRIMARY KEY (uNickname),
	CONSTRAINT uk_um UNIQUE (uMail)
);

CREATE TABLE Ingrediente(
	iID int,
	iNombre varchar UNIQUE,
	CONSTRAINT pk_i PRIMARY KEY (iID)
);

CREATE TABLE CategoriaDeIngrediente (
	cID int,
	cNombre varchar,
	CONSTRAINT pk_ci PRIMARY KEY (cID),
	CONSTRAINT uk_ci UNIQUE (cNombre)
);

CREATE TABLE CategoriaDeReceta(
	cID int,
	cNombre varchar(64),
	CONSTRAINT pk_cr PRIMARY KEY (cID)
);

CREATE TABLE RelCatIngred(
	iID int,
	cID int,
	CONSTRAINT pk_rci PRIMARY KEY (iID, cID),
	CONSTRAINT fk_iid FOREIGN KEY (iID) REFERENCES Ingrediente(iID),
	CONSTRAINT fk_cid FOREIGN KEY (iID) REFERENCES CategoriaDeIngrediente(cID) 
);


CREATE TABLE Receta (
	rID int,
	rNombre varchar (128),
	rDescripcion text,
	CONSTRAINT pk_r PRIMARY KEY (rID)
);

CREATE TABLE RelCatReceta (
	rID int,
	cID int,
	CONSTRAINT pk_rcr PRIMARY KEY (rID, cID),
	CONSTRAINT fk_rcrrID FOREIGN KEY (rID) REFERENCES Receta (rID),
	CONSTRAINT fk_rcrcID FOREIGN KEY (cID) REFERENCES CategoriaDeReceta(cID)
);

CREATE TABLE IngredienteReceta (
	rID int,
	iID int,
	cantidad int,
	unidadCantidad varchar(16),
	CONSTRAINT pk_ir PRIMARY KEY (rID, iID),
	CONSTRAINT fk_irrID FOREIGN KEY (rID) REFERENCES Receta (rID),
	CONSTRAINT fk_iriID FOREIGN KEY (iID) REFERENCES Ingrediente(iID)
);

CREATE TABLE Multimedia (
	mID int,
	link text,
	rID int,
	CONSTRAINT pk_mID PRIMARY KEY (mID),
	CONSTRAINT fk_mrID FOREIGN KEY (rID) REFERENCES Receta(rID)
);


CREATE TABLE Calificacion (
	uNickname varchar(32),
	rID int,
	calificac�on tinyint,
	CONSTRAINT pk_c PRIMARY KEY (uNickname, rID),
	CONSTRAINT fk_cunick FOREIGN KEY (uNickname) references Usuario(uNickname),
	CONSTRAINT fk_crid FOREIGN KEY (rID) references Receta(rID)
	
);