USE RecetasDivertidasDB;

-- Truncar tablas
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Calificacion;
TRUNCATE TABLE CategoriaDeIngrediente;
TRUNCATE TABLE CategoriaDeReceta;
TRUNCATE TABLE Ingrediente;
TRUNCATE TABLE IngredienteReceta;
TRUNCATE TABLE Multimedia;
TRUNCATE TABLE PreguntasSeguridad;
TRUNCATE TABLE Receta;
TRUNCATE TABLE RelCatIngred;
TRUNCATE TABLE RelCatReceta;
TRUNCATE TABLE Usuario;
SET FOREIGN_KEY_CHECKS = 1;

-- Insertar preguntas de seguridad
INSERT INTO PreguntasSeguridad (pregunta)
VALUES ("¿Cómo se llamó tu primer mascota?");
INSERT INTO PreguntasSeguridad (pregunta)
VALUES ("¿Cuál fue tu primer auto?");
INSERT INTO PreguntasSeguridad (pregunta)
VALUES ("¿Cómo se llama tu mejor amigo de la infancia?");
INSERT INTO PreguntasSeguridad (pregunta)
VALUES ("¿Cuál es tu pelicula favorita?");
INSERT INTO PreguntasSeguridad (pregunta)
VALUES ("¿Cómo se llama tu serie preferida?");

-- Insertar usuarios
CALL spRegistroUsuario (
	"lbotano",
	1,
	"morita",
	"Lautaro",
	"Otaño",
	"lautaro123",
	0,
	"otanolautarob@gmail.com",
    true,
	@resultado);

CALL spRegistroUsuario (
	"jorgelina",
    2,
    "Toyota",
    "Jorgelina",
    "Gutiérrez",
    "jorgelina123",
    1,
    "jorgelina@jorgelinaventiladores.cl",
    false,
    @resultado);

CALL spRegistroUsuario (
	"ryukyz",
	1,
	"Galo",
	"Camilo",
	"Laruffa",
	"camilo200",
	0,
	"camiloreal@gmail.com",
    true,
	@resultado);
    
CALL spRegistroUsuario (
	"Chefsito",
	5,
	"Hajime no ippo",
	"Tusar",
	"Verma",
	"tusar200",
	0,
	"esteeselmailrealdetusar@gmail.com",
    true,
	@resultado);
    
-- Cambiar contraseñas
CALL spCambiarContrasena(
	"lbotano",
    "Morita",
    "lautaro200");
    
-- Añadir categorías de recetas
CALL spAgregarCategoriaReceta("Almuerzo");
CALL spAgregarCategoriaReceta("Postre");
CALL spAgregarCategoriaReceta("Cena");
CALL spAgregarCategoriaReceta("Desayuno");
CALL spAgregarCategoriaReceta("Merienda");
CALL spAgregarCategoriaReceta("Colación");
CALL spAgregarCategoriaReceta("Panes o masas");
CALL spAgregarCategoriaReceta("Snack");
CALL spAgregarCategoriaReceta("Bebida");
CALL spAgregarCategoriaReceta("Sopa o caldo");
CALL spAgregarCategoriaReceta("Aperitivo");
CALL spAgregarCategoriaReceta("Plato principal");
CALL spAgregarCategoriaReceta("Ensalada");
CALL spAgregarCategoriaReceta("Guarnición");
CALL spAgregarCategoriaReceta("Salsa");
CALL spAgregarCategoriaReceta("Al horno");
CALL spAgregarCategoriaReceta("A parrilla");
CALL spAgregarCategoriaReceta("Pasta");

-- Añadir categorías de ingredientes
CALL spAgregarCategoriaIngrediente("Vegano");
CALL spAgregarCategoriaIngrediente("Celíaco");
CALL spAgregarCategoríaIngrediente("Vegetariano");
CALL spAgregarCategoríaIngrediente("Con azucar");
CALL spAgregarCategoríaIngrediente("Carne vacuna");
CALL spAgregarCategoríaIngrediente("Carne porcina");
CALL spAgregarCategoríaIngrediente("Carne ovina");
CALL spAgregarCategoríaIngrediente("Carne de pollo");
CALL spAgregarCategoríaIngrediente("Pescado");
CALL spAgregarCategoríaIngrediente("Derivado lacteo");
CALL spAgregarCategoríaIngrediente("Legumbre");
CALL spAgregarCategoríaIngrediente("Verdura");
CALL spAgregarCategoríaIngrediente("Fruta");
CALL spAgregarCategoríaIngrediente("Fruto seco");
CALL spAgregarCategoríaIngrediente("Cereal o derivado");
CALL spAgregarCategoríaIngrediente("Grasa");
CALL spAgregarCategoríaIngrediente("Hierba aromática");
CALL spAgregarCategoríaIngrediente("Especia");


-- Añadir ingredientes
-- Especias
CALL spAgregarIngrediente("Azúcar", '[{"cID":1},{"cID":2},{"cID":3},{"cID":4},{"cID":18}]');
CALL spAgregarIngrediente("Sal", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Pimienta", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Curcuma", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Pimentón", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Comino", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Azafran", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Nuez moscada", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Clavo de olor", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Ajo en polvc", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Oregano", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Curry", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Guindilla", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Anís estrellado", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Cardamomo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Jengibre en polvo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Canela", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Semillas de cilandro", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
CALL spAgregarIngrediente("Garan nasala", '[{"cID":1},{"cID":2},{"cID":3},{"cID":18}]');
-- Legumbres
CALL spAgregarIngrediente("Arveja", '[{"cID":1},{"cID":2},{"cID":3},{"cID":11}]');
CALL spAgregarIngrediente("Chaucha", '[{"cID":1},{"cID":2},{"cID":3},{"cID":11}]');
CALL spAgregarIngrediente("Choclo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":11}]');
CALL spAgregarIngrediente("Garbanzo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":11}]');
CALL spAgregarIngrediente("Lenteja", '[{"cID":1},{"cID":2},{"cID":3},{"cID":11}]');
CALL spAgregarIngrediente("Soja", '[{"cID":1},{"cID":2},{"cID":3},{"cID":11}]');
CALL spAgregarIngrediente("Poroto", '[{"cID":1},{"cID":2},{"cID":3},{"cID":11}]');
-- Grasas
CALL spAgregarIngrediente("Aceite de sesamo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":16}]');
CALL spAgregarIngrediente("Aceite de coco", '[{"cID":1},{"cID":2},{"cID":3},{"cID":16}]');
CALL spAgregarIngrediente("Aceite de oliva", '[{"cID":1},{"cID":2},{"cID":3},{"cID":16}]');
CALL spAgregarIngrediente("Aceite de girasol", '[{"cID":1},{"cID":2},{"cID":3},{"cID":16}]');
CALL spAgregarIngrediente("Grasa animal", '[{"cID":1},{"cID":2},{"cID":3},{"cID":16}]');
-- Cereales y derivados
CALL spAgregarIngrediente("Trigo", '[{"cID":1},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Avena", '[{"cID":1},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Cebada", '[{"cID":1},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Centeno", '[{"cID":1},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Arroz integral", '[{"cID":1},{"cID":2},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Arroz", '[{"cID":1},{"cID":2},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Maiz", '[{"cID":1},{"cID":2},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Mijo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Harina de trigo", '[{"cID":1},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Harina de centeno", '[{"cID":1},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Pan de centeno", '[{"cID":1},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Copos de avena", '[{"cID":1},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Harina de maiz", '[{"cID":1},{"cID":2},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Copos de maiz", '[{"cID":1},{"cID":2},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Quinoa", '[{"cID":1},{"cID":2},{"cID":3},{"cID":15}]');
CALL spAgregarIngrediente("Harina de quinoa", '[{"cID":1},{"cID":2},{"cID":3},{"cID":15}]');
-- Lacteos y derivados
CALL spAgregarIngrediente("Crema de leche", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Dulce de leche", '[{"cID":2},{"cID":3},{"cID":10},{"cID":4}]');
CALL spAgregarIngrediente("Leche", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Leche condensada", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Leche en polvo", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Manteca", '[{"cID":2},{"cID":3},{"cID":10},{"cID":16}]');
CALL spAgregarIngrediente("Manteca clarificada", '[{"cID":2},{"cID":3},{"cID":16}]');
CALL spAgregarIngrediente("Queso azul", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Queso cheddar", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Queso brie", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Queso crema", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Queso de cabra", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Queso emmental", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Queso gouda", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Queso mascarpone", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Queso mozarella", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Queso parmesano", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Queso provolone", '[{"cID":2},{"cID":3},{"cID":10}]');
CALL spAgregarIngrediente("Yogurt", '[{"cID":2},{"cID":3},{"cID":10}]');
-- Verduras
CALL spAgregarIngrediente("Acelga", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Ajo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Apio", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Batata", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Berenjena", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Brocoli", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Calabaza", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Cebolla", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Champiñón", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Coliflor", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Esparrago", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Espinaca", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Jengibre", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Lechuga", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Morrón verde", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Morrón rojo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Morrón amarillo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Papa", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Pepino", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Remolacha", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Repollo blanco", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Repollo colorado", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Tomate", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Tomate cherry", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Zanahoria", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
CALL spAgregarIngrediente("Zapallo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":12}]');
-- Frutas
CALL spAgregarIngrediente("Palta", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Damasco", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Arandano", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Cereza", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Ciruela", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Frutilla", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Frambuesa", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Coco", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Granada", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Higo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Kiwi", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Lima", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Limon", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Mango", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Manzana", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Durazno", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Papaya", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Naranja", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Mandarina", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Piña", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Mora", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Pera", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Banana", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Uva", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
CALL spAgregarIngrediente("Pomelo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":13}]');
-- Frutos secos
CALL spAgregarIngrediente("Almendra", '[{"cID":1},{"cID":2},{"cID":3},{"cID":14}]');
CALL spAgregarIngrediente("Avellana", '[{"cID":1},{"cID":2},{"cID":3},{"cID":14}]');
CALL spAgregarIngrediente("Nuezs", '[{"cID":1},{"cID":2},{"cID":3},{"cID":14}]');
CALL spAgregarIngrediente("Pistacho", '[{"cID":1},{"cID":2},{"cID":3},{"cID":14}]');
CALL spAgregarIngrediente("Pasas de uva", '[{"cID":1},{"cID":2},{"cID":3},{"cID":14}]');
CALL spAgregarIngrediente("Maní", '[{"cID":1},{"cID":2},{"cID":3},{"cID":14}]');
CALL spAgregarIngrediente("Castañas de cajú", '[{"cID":1},{"cID":2},{"cID":3},{"cID":14}]');
-- Hierbas aromáticas
CALL spAgregarIngrediente("Albahaca", '[{"cID":1},{"cID":2},{"cID":3},{"cID":17}]');
CALL spAgregarIngrediente("Cilandro", '[{"cID":1},{"cID":2},{"cID":3},{"cID":17}]');
CALL spAgregarIngrediente("Laurel", '[{"cID":1},{"cID":2},{"cID":3},{"cID":17}]');
CALL spAgregarIngrediente("Menta", '[{"cID":1},{"cID":2},{"cID":3},{"cID":17}]');
CALL spAgregarIngrediente("Perejil", '[{"cID":1},{"cID":2},{"cID":3},{"cID":17}]');
CALL spAgregarIngrediente("Romero", '[{"cID":1},{"cID":2},{"cID":3},{"cID":17}]');
CALL spAgregarIngrediente("Tomillo", '[{"cID":1},{"cID":2},{"cID":3},{"cID":17}]');
-- Pescados
CALL spAgregarIngrediente("Anchoa", '[{"cID":9}]');
CALL spAgregarIngrediente("Anguila", '[{"cID":9}]');
CALL spAgregarIngrediente("Atún", '[{"cID":9}]');
CALL spAgregarIngrediente("Bacalao", '[{"cID":9}]');
CALL spAgregarIngrediente("Bagre", '[{"cID":9}]');
CALL spAgregarIngrediente("Bonito", '[{"cID":9}]');
CALL spAgregarIngrediente("Caballa", '[{"cID":9}]');
CALL spAgregarIngrediente("Calamar", '[{"cID":9}]');
CALL spAgregarIngrediente("Camarones", '[{"cID":9}]');
CALL spAgregarIngrediente("Cangrejo de mar", '[{"cID":9}]');
CALL spAgregarIngrediente("Carpa", '[{"cID":9}]');
CALL spAgregarIngrediente("Dorado", '[{"cID":9}]');
CALL spAgregarIngrediente("Langosta", '[{"cID":9}]');
CALL spAgregarIngrediente("Langostino", '[{"cID":9}]');
CALL spAgregarIngrediente("Lenguado", '[{"cID":9}]');
CALL spAgregarIngrediente("Merluza", '[{"cID":9}]');
CALL spAgregarIngrediente("Tiburon", '[{"cID":9}]');
CALL spAgregarIngrediente("Trucha", '[{"cID":9}]');
CALL spAgregarIngrediente("Salmon", '[{"cID":9}]');
-- Cortes de carne de pollo
CALL spAgregarIngrediente("Alita de pollo", '[{"cID":8}]');
CALL spAgregarIngrediente("Muslo de pollo", '[{"cID":8}]');
CALL spAgregarIngrediente("Pata de pollo", '[{"cID":8}]');
CALL spAgregarIngrediente("Pechuga de pollo", '[{"cID":8}]');
-- Cortes de carne vacuna
CALL spAgregarIngrediente("Asado", '[{"cID":5}]');
CALL spAgregarIngrediente("Azotillo", '[{"cID":5}]');
CALL spAgregarIngrediente("Bife ancho", '[{"cID":5}]');
CALL spAgregarIngrediente("Bola de lomo", '[{"cID":5}]');
CALL spAgregarIngrediente("Colita de cuadril", '[{"cID":5}]');
CALL spAgregarIngrediente("Cuadril", '[{"cID":5}]');
CALL spAgregarIngrediente("Falda", '[{"cID":5}]');
CALL spAgregarIngrediente("Lomo", '[{"cID":5}]');
CALL spAgregarIngrediente("Matambre", '[{"cID":5}]');
CALL spAgregarIngrediente("Nalga cuadrada", '[{"cID":5}]');
CALL spAgregarIngrediente("Osobuco", '[{"cID":5}]');
CALL spAgregarIngrediente("Paleta vacuna", '[{"cID":5}]');
CALL spAgregarIngrediente("Peceto", '[{"cID":5}]');
CALL spAgregarIngrediente("Pecho vacuno", '[{"cID":5}]');
CALL spAgregarIngrediente("Roast beaf", '[{"cID":5}]');
CALL spAgregarIngrediente("Vacio", '[{"cID":5}]');
-- Cortes de carne porcina
CALL spAgregarIngrediente("Bondiola", '[{"cID":6}]');
CALL spAgregarIngrediente("Carre", '[{"cID":6}]');
CALL spAgregarIngrediente("Codillo", '[{"cID":6}]');
CALL spAgregarIngrediente("Costillar", '[{"cID":6}]');
CALL spAgregarIngrediente("Churrasquito", '[{"cID":6}]');
CALL spAgregarIngrediente("Jamón", '[{"cID":6}]');
CALL spAgregarIngrediente("Manta parrillera", '[{"cID":6}]');
CALL spAgregarIngrediente("Matambrito", '[{"cID":6}]');
CALL spAgregarIngrediente("Paleta porcina", '[{"cID":6}]');
CALL spAgregarIngrediente("Panceta", '[{"cID":6}]');
CALL spAgregarIngrediente("Pechito porcino", '[{"cID":6}]');
CALL spAgregarIngrediente("Solomillo", '[{"cID":6}]');
CALL spAgregarIngrediente("Tocino", '[{"cID":6}]');

-- Subir receta
CALL spSubirReceta (
	'lbotano',
	'Alfajor Jorgito',
    'Es hermoso',
    'Hacelo',
    '[{"iID": 1, "cantidad": 100, "unidadCantidad": "ml"}, {"iID": 2, "cantidad": 200, "unidadCantidad": "g"}]',
    '[{"link":"http://www.google.com/foto.png"}, {"link":"http://www.cuantocabron.com/gracioso.jpg"}]',
    '[{"cID":1},{"cID":2}]'
);

CALL spSubirReceta (
	'jorgelina',
	'Empanada',
    'Se come',
    'Haces empanadas ._.',
    '[{"iID": 2, "cantidad": 4, "unidadCantidad": "kg"}]',
    NULL,
    '[{"cID":1}]'
);

-- Calificar recetas
CALL spCalificarReceta('lbotano', 1, 5);
CALL spCalificarReceta('jorgelina', 1, 3);
CALL spCalificarReceta('jorgelina', 2, 1);