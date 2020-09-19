1---Registro usuarios-------------------------------------------------------------------------------
IF OBJECT_ID('[dbo].[spRegistroUsuario]') IS NULL
    EXEC('CREATE PROCEDURE [dbo].[spRegistroUsuario] AS;')
GO

ALTER PROCEDURE [dbo].[spRegistroUsuario]
(
	@uNickname varchar(32),
	@uPreguntaSeguridad varchar(64),
	@uRespuestaSeguridad varchar(64),
	@uNombre varchar,
	@uApellido varchar,
	@uContrasenia varchar(50),
	@uGenero tinyint,
	@uMail varchar(64)	
)
AS
BEGIN
	DECLARE @ExisteUsuario int;
	SET @ExisteUsuario = 
	(
		SELECT COUNT(*) FROM Usuario WHERE uNickname = @uNickname
	);
	IF @ExisteUsuario = 0
		BEGIN
			INSERT INTO Usuario
			VALUES (@uNickname,@uPreguntaSeguridad,@uRespuestaSeguridad,
					@uNombre,@uApellido,@uContrasenia,@uGenero,@uMail);
			SELECT 0 AS 'Result';
		END
	ELSE
		BEGIN
			SELECT 1 AS 'Result';
		END
 
END

2---Inicio sesion-------------------------------------------------------------------------------

IF OBJECT_ID('[dbo].[spInicioSesion]') IS NULL
    EXEC('CREATE PROCEDURE [dbo].[spInicioSesion] AS;')
GO

ALTER PROCEDURE [dbo].[spInicioSesion]
(	
	@uNombreUsu varchar(32),
	@uContra varchar(50)
	
)
AS
BEGIN
	DECLARE @BuscarUs int;
	DECLARE @ContraDB varchar(50);
	SET @BuscarUs =
		(
		SELECT COUNT(*) FROM Usuario WHERE uNickname = @uNombreUsu
		);
	--Si no encuentra un usuario, devuelve 0--
	IF @BuscarUs = 0
		BEGIN
			SELECT 0 AS 'Result';
		END
	--Si encuentra usuario:--
	ELSE
		BEGIN
			SELECT @ContraDB = uContrasenia FROM Usuario WHERE uNickname = @uNombreUsu;
			--coincide la contrasenia, devuelvue 1--
			IF @ContraDB = @uContra
				BEGIN
					SELECT 1 AS 'Result';
				END
			--coincide la contrasenia, devuelve 2--
			ELSE
				BEGIN
					SELECT 2 AS 'Result';
				END
		END	
END

3---Nombres de recetas de un usuario-------------------------------------------------------------------------------

IF OBJECT_ID('[dbo].[spRecetasUsuario]') IS NULL
    EXEC('CREATE PROCEDURE [dbo].[spRecetasUsuario] AS;')
GO

ALTER PROCEDURE [dbo].[spRecetasUsuario]
(
	@uNickname varchar(32)	
)
AS
BEGIN
	IF @uNickname IS NULL
		BEGIN
			SELECT 0 AS 'Result'	
		END
	ELSE
		BEGIN
			SELECT rID AS nroReceta, rNombre AS Nombre, rDescripcion AS Descripcion 
			FROM Receta WHERE uNickname = @uNickname;
		END
	
END

4---Datos de una receta (pasada como parametro por el ID)---------------------------------------------------------------

IF OBJECT_ID('[dbo].[spDatosReceta]') IS NULL
    EXEC('CREATE PROCEDURE [dbo].[spDatosReceta] AS SELECT -| AS ;')
GO

ALTER PROCEDURE [dbo].[spDatosReceta]
(
	@rID int
)
AS
BEGIN
	IF @rID IS NULL
		BEGIN
			SELECT 0 AS 'Result'	
		END
	ELSE
		BEGIN
			SELECT 
				i.iNombre, 
				ir.cantidad, 
				ir.unidadCantidad
			FROM
				IngredienteReceta ir
			INNER JOIN
				Ingrediente i
			ON i.iID = ir.iID
			WHERE ir.rID = @rID	
		END
END

5----Listar usuarios o datos de un usuario--------------------------------------------

IF OBJECT_ID('[dbo].[spListarUsuarios]') IS NULL
    EXEC('CREATE PROCEDURE [dbo].[spListarUsuarios] AS ;')
GO
ALTER PROCEDURE [dbo].[spDatosUsuario]
(
	@uNickname varchar(32) = NULL
)
AS
/*
Parametro opcional
Si no se pone:
	Lista todos los nicknames de los usuarios
Si se pone:
	Lista los datos del usuario que tenga el nickanme pasado como parametro
*/
BEGIN
	IF @uNickname IS NULL
		BEGIN				
			SELECT
				uNIckname
			FROM Usuario
		END
	ELSE
		BEGIN
			SELECT
				uNickname,
				uNombre,
				uApellido,
				uGenero,
				uMail		
			FROM Usuario
			WHERE uNickname = @uNickname
		END
END
6---Banear usuario-------------------------------------------------------------------------------
IF OBJECT_ID('[dbo].[spBaneoUsuario]') IS NULL
    EXEC('CREATE PROCEDURE [dbo].[spBaneoUsuario] AS ;')
GO
ALTER PROCEDURE [dbo].[spBaneoUsuario]
(
	@uNickname varchar(32)
)
AS
BEGIN
	IF @uNickname IS NULL
		BEGIN
			SELECT 0 AS 'Result'	
		END
	ELSE
		BEGIN
			DELETE FROM RecetasDivertidasDB.dbo.Usuario 
			WHERE uNickname = @uNickname;
			
			SELECT 1 AS 'Result'
		END
END
7---Calificar receta-------------------------------------------------------------------------------
IF OBJECT_ID('[dbo].[spCalificarReceta]') IS NULL
    EXEC('CREATE PROCEDURE [dbo].[spCalificarReceta] AS ;')
GO
ALTER PROCEDURE [dbo].[spCalificarReceta]
(
	@rID int,
	@uNickname varchar(32),
	@calificacion tinyint
)
AS
BEGIN
	IF @rID IS NULL OR @uNickname IS NULL OR @calificacion IS NULL
		BEGIN
			SELECT 0 AS 'Result'		
		END
	ELSE
		BEGIN
			INSERT INTO Calificacion
			VALUES (@uNickname, @rID, @calificacion)
			SELECT 1 AS 'Result'
		END
END
---Insertar categoria-------------------------------------------------------------------------------
IF OBJECT_ID('[dbo].[spSubirCategoria]') IS NULL
    EXEC('CREATE PROCEDURE [dbo].[spSubirCategoria] AS ;')
GO
ALTER PROCEDURE [dbo].[spSubirCategoria]
(
	@cNombre varchar(42),
	--0 categoria de ingrediente--
	--1 categoria de receta--
	@catRecetaoIngrediente tinyint
)
AS
BEGIN
	IF @cNombre IS NULL or @catRecetaoIngrediente IS NULL
		BEGIN
			SELECT 0 AS 'Result'
		END
	ELSE
		BEGIN
			IF @catRecetaoIngrediente = 0
				BEGIN
					INSERT INTO CategoriaDeIngrediente (cNombre)
					VALUES (@cNombre)
					SELECT 1 AS 'Result'
				END
			ELSE IF @catRecetaoIngrediente = 1
				BEGIN
					INSERT INTO CategoriaDeReceta (cNombre)
					VALUES (@cNombre)
					SELECT 1 AS 'Result'
				END
			ELSE
				BEGIN
					SELECT 0 AS 'Result'
				END
		END
END
----------------------------------------------------------------------------------