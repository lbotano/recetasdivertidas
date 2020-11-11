package servidorRecetasDivertidas;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import com.google.gson.*;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import jsonClasess.Categoria;
import jsonClasess.IdIngrediente;
import jsonClasess.Ingrediente;
import jsonClasess.Multimedia;

public class ThreadClient implements Runnable{
	//referencia al objeto que maneja las conexiones a la base de datos
	protected ComboPooledDataSource cpds;
	//objeto que maneja el enlace con el cliente
	protected Socket socket;
	//objetos que guardan los mensajes que se transmiten
	protected ArrayList<String> answer;
	protected ArrayList<String> message;
	//objeto para llamar a store procedures
	protected CallableStatement stmt;
	//objeto para hacer consultas simples a la bd 
	protected PreparedStatement pstmt;
	//objeto para guardar la conexion a la base de datos
	protected Connection conn;
	protected ArrayListStringValidator stringValidator;

	
	//llamadas a SPs
	private static final String BORRARRECUSU = "{call spUsuarioBorrarReceta(?,?)}";
	private static final String CALIFICAR = "{call spCalificarReceta(?,?,?)}";
	private static final String CAMBIARCONTRA = "{call spCambiarContrasena(?,?,?)}";
	private static final String CONSCALIFUSUARIO = "{call spGetCalificacionPorUsuario(?,?,?)}";
	private static final String CONSRECETASCATING = "{call spBuscarRecetasPorCatIngr(?,?)}";
	private static final String CONSRECETASCATREC = "{call spBuscarRecetasPorCatReceta(?,?)}";
	private static final String CONSRECETAING = "{call spBuscarRecetasPorIngr(?,?)}";
	private static final String CONSRECETATEXT = "{call spBuscarRecetasPorTexto(?,?)}";
	private static final String CONSTOPRECETAS = "{call spSeleccionarTopRecetas(?)}";
	private static final String DATOSRECETA = "{call spGetDatosReceta(?)}";
	private static final String INGREDIENTES = "{call spSeleccionarIngredientes()}";
	private static final String LISTARCATREC = "SELECT * FROM CategoriaDeReceta;";
	private static final String LISTARCATING = "SELECT * FROM CategoriaDeIngrediente;";
	private static final String LOGIN = "{call spInicioSesion(?,?,?,?)}";
	private static final String PREGUNTASSEG = "SELECT * FROM PreguntasSeguridad;";
	private static final String RECETASDEUSUARIO = "{call spGetRecetasUsuario(?)}";
	private static final String REGISTRO = "{call spRegistroUsuario(?,?,?,?,?,?,?,?,?,?)}";
	private static final String SUBIRRECETA = "{call spSubirReceta(?,?,?,?,?,?,?)}";
	private static final String USUPREGSEG = "SELECT * FROM PreguntasSeguridad WHERE id in" +
											" (SELECT uPreguntaSeguridad FROM usuario  WHERE uNickname = ?)";
	private static final String DefaultSQLErrorMsg = "Error en la base de datos";
	
	public ThreadClient(ComboPooledDataSource c, Socket s) {
		this.cpds = c;
		this.socket = s;
	}
	/*
	 * Cuando se ejecuta un sp puede tirar un error, este metodo recibe el SQLException
	 * y el mensaje de error que se la va a comunicar al cliente si el error de sql
	 * es 45000 (estos son los mensajes de error definidos por lauti pro gamer), si no lo es
	 * entonces le manda el 'DefaultSQLErrorMsg'
	 */
	
	protected void sqlExceptionHandler(SQLException e, String failMsg) {
		answer.clear();
		answer.add(failMsg);
		if(e.getSQLState().contentEquals("45000")) {
    		answer.add(e.getMessage());
		}else {
			answer.add(DefaultSQLErrorMsg);
			e.printStackTrace();
		}
	}

	protected void intExceptionHandler(NumberFormatException e, String failMsg){
		answer.clear();
		answer.add(failMsg);
		answer.add("Uno de los datos ingresados que debia ser numerico, no lo es");
	}
	
	private void borrarRecetaUsu() {
		try {
			stmt = conn.prepareCall(BORRARRECUSU);
			//id de la receta
			stmt.setInt(1, Integer.parseInt(message.get(1)));
			//nickname del usuario
			stmt.setString(2, message.get(2));
			stmt.execute();
			answer.add("BORRARRECOK");
		} catch (SQLException e) {
			sqlExceptionHandler(e, "BORRARRECFAIL");
		} catch(NumberFormatException e){
			intExceptionHandler(e, "BORRARRECFAIL");
		}

	}
	
	private void calificar() {
		try {
			//define que utiliza el sp de calificar
			stmt = conn.prepareCall(CALIFICAR);
			//establece los parametros segun lo que haya enviado el cliente
			//nickanem
			stmt.setString(1, message.get(1));
			//id receta
			stmt.setInt(2, Integer.parseInt(message.get(2)));
			//calificacion
			stmt.setInt(3, Integer.parseInt(message.get(3)));
			//ejecuta el sp
			stmt.execute();
			//si sql no tira ningun error, significa que se pudo calificar correctamente
			answer.add("CALIFICAROK");
		} catch (SQLException e) {
			sqlExceptionHandler(e, "CALIFICARFAIL");
		}
	}

	private void cambiarContra() {
		try {
			stmt = conn.prepareCall(CAMBIARCONTRA);
			//1: nickname; 2: respuesta de la pregunta de seguridad; 3: contra nueva;
			for (int i = 1; i <= 3; i++) {
				stmt.setString(i, message.get(i));
			}
			stmt.execute();
			answer.add("CAMBIARCONTRAOK");
		}catch(SQLException e) {
			sqlExceptionHandler(e, "CAMBIARCONTRAFAIL");
		}
	}

	private void consCalifUsuario(){
		try {
			stmt = conn.prepareCall(CONSCALIFUSUARIO);
			//nickname
			stmt.setString(1,message.get(1));
			//id receta
			stmt.setInt(2, Integer.parseInt(message.get(2)));
			//calificación
			stmt.registerOutParameter(3, Types.FLOAT);
			stmt.execute();
			answer.add("CALIFICACIONUSUARIO");
			answer.add(String.valueOf(stmt.getFloat(3)));

		} catch (SQLException e) {
			sqlExceptionHandler(e, "CALIFICACIONUSUARIOFAIL");
		} catch (NumberFormatException e) {
			intExceptionHandler(e, "CALIFICACIONUSUARIOFAIL");
		}

	}

	/*Este metodo se usa para agregar a la respuesta que se va a mandar al cliente los datos de una receta
	Se hace en un metedo porque la utilizan los 3 tipos de consulta, por categoria de ingrediente,
	por categoria de recetas y por texto
	 */
	private void DatosConsultaRecetas(ResultSet rs) throws SQLException {
		if(rs != null){
			/*este indice sirve para contar cuantos registros tiene el resultset
			Si next devuelve fasle, entonces no hay resultados de busqueda
			 */
			int index = 0;
			while(rs.next()) {
				//id de la receta
				answer.add(Integer.toString(rs.getInt(1)));
				//autor de la receta
				answer.add(rs.getString(2));
				//nombre de la receta
				answer.add(rs.getString(3));
				//descripcion de la receta
				answer.add(rs.getString(4));
				//calificacion
				answer.add(String.valueOf(rs.getInt(5)));
				//cantidad de calificaciones
				answer.add(String.valueOf(rs.getInt(6)));
				index++;
			}
			rs.close();
			if(index == 0){
				throw new SQLException("No hay resultados para la busqueda", "45000");
			}else{
				answer.add(0,"RESPCONSULTA");
			}
		}else{
			throw new SQLException("Error en la consulta", "45000");
		}
	}
	//true: buscar por categorias de recetas
	//false: buscar por categorias de ingredientes
	private void consRecetasCatRecIng(boolean recoing) {
		try {
			if(recoing) {
				stmt = conn.prepareCall(CONSRECETASCATREC);				
			}else {
				stmt = conn.prepareCall(CONSRECETASCATING);				
				
			}
			/*Toma las categorias seleccionadas por el usuario y las pone en un arraylist para pasarlas
			 a json y mandarlo como parametro al sp
			 */
			ArrayList<String> categorias = new ArrayList<String>();
			for (int i = 2; i < message.size(); i++) {
				categorias.add(message.get(i));
			}
			stmt.setString(1, new Gson().toJson(categorias));
			//numero de pagina
			stmt.setInt(2,Integer.parseInt(message.get(1)));
			stmt.execute();

			DatosConsultaRecetas(stmt.getResultSet());
		}catch(SQLException e) {
			sqlExceptionHandler(e, "RESPOCONSULTAFAIL");
		}
	}
	
	private void consRecetaIng() {
		try {
			stmt = conn.prepareCall(CONSRECETAING);
			ArrayList<String> ingredientes = new ArrayList<String>();

			//llena el arraylist con los id de ingredientes
			for (int i = 2; i < message.size(); i++) {
				ingredientes.add(message.get(i));
			}
			//pone el json en el primer parametro del sp

			System.out.println(new Gson().toJson(ingredientes));
			stmt.setString(1, new Gson().toJson(ingredientes));
			//numero de pagina
			stmt.setInt(2, Integer.parseInt(message.get(1)));
			stmt.execute();
			
			DatosConsultaRecetas(stmt.getResultSet());
			
		}catch (SQLException e) {
			sqlExceptionHandler(e, "RESPOCONSULTAFAIL");
		} catch(NumberFormatException e){
			intExceptionHandler(e, "RESPOCONSULTAFAIL");
		}
		
	}
	
	private void consRecetaText() {
		try {
			stmt = conn.prepareCall(CONSRECETATEXT);
			//num pag
			stmt.setInt(2, Integer.parseInt(message.get(1)));
			//texto
			stmt.setString(1, message.get(2));
			stmt.execute();

			DatosConsultaRecetas(stmt.getResultSet());

		} catch (SQLException e) {
			sqlExceptionHandler(e, "RESPOCONSULTAFAIL");
		} catch (NumberFormatException e){
			intExceptionHandler(e, "CONSTOPRECETASFAIL");
		}
	}

	private void consTopRecetas(){
		try {
			stmt = conn.prepareCall(CONSTOPRECETAS);
			stmt.setInt(1,Integer.parseInt(message.get(1)));
			stmt.execute();

			DatosConsultaRecetas(stmt.getResultSet());

		} catch (SQLException e) {
			sqlExceptionHandler(e, "CONSTOPRECETASFAIL");
		} catch (NumberFormatException e){
			intExceptionHandler(e, "CONSTOPRECETASFAIL");
		}
	}
	
	private void datosReceta() {
		try {
			stmt = conn.prepareCall(DATOSRECETA);
			//pone la id de la receta
			stmt.setString(1, message.get(1));
			stmt.execute();
			/*El primer resultset se debe tomar despues de llamar a execute()
			 * Si se llama a getMoreResults() primero, se perderia el primer resultset
			 * 
			 * Resultsets:
			 * 1. Datos basicos receta
			 * 2. Ingredientes de la receta
			 * 3. Categorías de la receta
			 * 4. Categorías de sus ingredientes
			 * 5. Multimedia* 
			 * 
			 * el 1 y 2 tienen el mismo esquema
			 * el 3, 4 y 5 tienen el mismo esquema
			 * 
			 */
			ResultSet rs = stmt.getResultSet();
			/*este indice cuenta los registros que tiene el primer resultset. Si el metodo next() devuelve false
			entonces no hay datos basicos para la receta, lo que significa que no existe.
			 */
			int index = 0;
			//1. (rID, rAutor, rNombre, rDescripcion, rInstrucciones, promedioCalificacion, cantidadCalificaciones
			while (rs.next()){
				//rID
				answer.add(String.valueOf(rs.getInt(1)));
				//rAutor, rNombre, rDescripcion, rInstrucciones
				for (int i = 2; i<= 5; i++){
					answer.add(rs.getString(i));
				}
				//promedioCalificacion
				answer.add(String.valueOf(rs.getInt(6)));
				//cantidadCalificaciones
				answer.add(String.valueOf(rs.getInt(7)));
				index++;
			}
			if(index == 0){
				throw new SQLException("La receta no existe", "45000");
			}
			rs.close();
			//Toma el proximo resultset, si no hay (o es un update) entonces error
			if(!stmt.getMoreResults()){
				throw new SQLException("Error en la consulta: no se encontro datos de los ingredientes de la receta", "45000");
			}
			rs = stmt.getResultSet();
			//2. ingredientes
			while(rs.next()){
				//iID id del ingrediente
				answer.add(String.valueOf(rs.getInt(1)));
				//Nombre nombre del ingrediente
				answer.add(rs.getString(2));
				//unidad del ingrediente
				answer.add(rs.getString(4));
				//cantidad del ingrediente
				answer.add(String.valueOf(rs.getInt(3)));
			}
			rs.close();
			answer.add("CATEGORIASRECETA");
			// Se toman los datos de las categorias de recetas, categorias de ingredientes y multimedia
			for (int i = 0; i < 3; i++) {
				//Toma el proximo resultset, si no hay (o es un update) entonces error
				if(!stmt.getMoreResults()) {
					if(i == 0){
						throw new SQLException("Error en la consulta: no se encontro las categorias de receta", "45000");
					}else if (i == 1){
						throw new SQLException("Error en la consulta: no se encontro las categorias de ingredietnes", "45000");
					}else{
						throw new SQLException("Error en la consulta: no se encontro la multimedia", "45000");
					}
				}
				rs = stmt.getResultSet();
				while(rs.next()) {
					//cId, id de la categoria (receta / ingrediente) / mID, id de multimedia
					answer.add(String.valueOf(rs.getInt(1)));
					//nombre / link
					answer.add(rs.getString(2));
				}
				rs.close();

				/*Indice 0 = CATEGORIASRECETAS
				INDICE 1 = CATEGORIASING
				INDICE 2 = MULTIMEDIA
				 */
				if(i == 0) {
					answer.add("CATEGORIASING");
				}else if (i == 1){
					answer.add("MULTIMEDIA");
				}
			}
			//si todo salio bien, pone el mensaje de ok
			answer.add(0,"DATOSRECETAOK");

		}catch (SQLException e) {
			sqlExceptionHandler(e, "DATOSRECETAFAIL");
		}
	}

	private void ingredientes() {
		try {
			stmt = conn.prepareCall(INGREDIENTES);
			ResultSet rs = stmt.executeQuery();
			answer.add("INGREDIENTESOK");
			int lastIdIng = -1, idIng;
			while(rs.next()) {
				idIng =	rs.getInt(1);
				/*Si el id del ingrediente no es igual al id del ingrediente anterior, entonces es un ingrediente distinto
				Por lo tanto se agrega al mensaje los datos del nuevo ingrediente.
				 */
				if (idIng != lastIdIng) {
					lastIdIng = idIng;
					answer.add("NEXTING");
					//id del ingrediente
					answer.add(String.valueOf(idIng));
					//nombre del ingrediente
					answer.add(rs.getString(2));
				}
				//id de categoria
				answer.add(String.valueOf(rs.getInt(3)));
				//nombre de categoria
				answer.add(rs.getString(4));
			}
			
		}catch(SQLException e) {
			sqlExceptionHandler(e, "INGREDIENTESFAIL");
		}
	}
	
	private void listarCatIng() {
		try {
			pstmt = conn.prepareStatement(LISTARCATING);
			ResultSet rs = pstmt.executeQuery();
			answer.add("LISTCATING");
			while(rs.next()) {
				//id de la categoria
				answer.add(String.valueOf(rs.getInt(1)));
				//nombre de la categoria
				answer.add(rs.getString(2));
			}
		} catch (SQLException e) {
			sqlExceptionHandler(e, "LISTARCATINGFAIL");
		}
		
	}
	
	private void listarCatRec() {
		try {
			pstmt = conn.prepareStatement(LISTARCATREC);
			ResultSet rs = pstmt.executeQuery();
			answer.add("LISTCATREC");
			while(rs.next()) {
				//id de la categoria
				answer.add(String.valueOf(rs.getInt(1)));
				//nombre de la categoria
				answer.add(rs.getString(2));
			}
		} catch (SQLException e) {
			sqlExceptionHandler(e, "LISTARCATRECFAIL");
		}		
	}
	
	private void login() {
		try {
			//establece q codigo sql va a ejecutar
			stmt = conn.prepareCall(LOGIN);
			/*setea los parametros a partir del mensaje del cliente
			 * 1: nickname 2: contraseña 3: resultado 4: esAdmin
			 * 3 y 4 son parametros de salida
			 */			
			stmt.setString(1, message.get(1));
			stmt.setString(2, message.get(2));
			
			stmt.registerOutParameter(3, Types.BOOLEAN);
			stmt.registerOutParameter(4, Types.BOOLEAN);
			
			//ejecuta la consulta			
			stmt.execute();
			/*Si pudo logearse entonces manda un LOGINOK
			 * y si es admin o no (true: si)
			 * Se fija por el 3er parametro que es el resultado
			 * si es false cagaada xd
			 */
			if(stmt.getBoolean(3)) {
				answer.add("LOGINOK");
				answer.add(String.valueOf(stmt.getBoolean(4)));
			}else {
				answer.add("LOGINFAIL");
				answer.add("Error en la consulta");
			}
		} catch (SQLException e) {	
			sqlExceptionHandler(e, "LOGINFAIL");	
		}		
	}	
	
	private void preguntasSeg() {
		try {
			pstmt = conn.prepareStatement(PREGUNTASSEG);
			ResultSet rs = pstmt.executeQuery();
			answer.add("PREGUNTASSEG");
			while(rs.next()) {
				//id de la pregunta
				answer.add(String.valueOf(rs.getInt(1)));
				//preugnta
				answer.add(rs.getString(2));
			}
		} catch (SQLException e) {
			sqlExceptionHandler(e, "PREGUNTASSEGFAIL");
		}
	}
	
	private void recetaUsuario() {
		try {
			stmt = conn.prepareCall(RECETASDEUSUARIO);
			//pone de parametro del sp el nickname recibido del cliente
			stmt.setString(1, message.get(1));
			stmt.execute();

			ResultSet resultadoSP = stmt.getResultSet();
			//next = false: resultset vacio
			//next = true: resultset con datos, mueve el cursor a la fila 1.
			
			if(resultadoSP.next()) {

				answer.add("RECETASDEUSUARIOOK");
				do {
					//agarra los elementos de la fila y los pone en el aeeay de respuesta
					answer.add(resultadoSP.getString(1));
					answer.add(resultadoSP.getString(2));
					answer.add(resultadoSP.getString(3));		
				//Mueve el cursor a la siguiente fila, si hay mas resultados devuelve true
				} while (resultadoSP.next());
			}else {
				answer.add("RECETASDEUSUARIONULL");
			}
		
		}catch (SQLException e) {
			sqlExceptionHandler(e, "RECETASDEUSUARIOFAIL");
		}
	}
	
	private void registro() {
		try {
			stmt = conn.prepareCall(REGISTRO); 
			//poner en el statement todos los parametros de registro
    		for (int i = 1; i < message.size(); i++) {
    			if (i == 2 || i == 7) {
    				stmt.setInt(i, Integer.parseInt(message.get(i)));
    			}else {
            		stmt.setString(i, message.get(i));            				
    			}
			}
    		stmt.setBoolean(9,false);
    		stmt.registerOutParameter(10, Types.BOOLEAN);
    		stmt.execute();
    		answer.add("REGISTEROK");
		} catch (SQLException e) {
			sqlExceptionHandler(e, "REGISTERFAIL");
		} catch(NumberFormatException e){
			intExceptionHandler(e, "REGISTERFAIL");
		}
		 
	}

	private void servidorVive(){
		answer.add("SERVIDORESTAVIVO");
	}
	
	private void subirReceta() {
		try {
			stmt = conn.prepareCall(SUBIRRECETA);
			ArrayList<Ingrediente> ing = new ArrayList<Ingrediente>();
			ArrayList<Categoria> catRec = new ArrayList<Categoria>();
			ArrayList<Multimedia> mult = new ArrayList<Multimedia>();
			int i;
			for (i = 1; i < 5; i++) {
				//1: nickname, 2: nombre receta, 3: descripcion, 4: instrucciones
				stmt.setString(i, message.get(i));
			}
			//agregar ingredientes
			while(!(message.get(i).contentEquals("CATEGORIASRECETA"))){
				ing.add(new Ingrediente(Integer.parseInt(message.get(i)) ,Integer.parseInt(message.get(i+1)),message.get(i+2)));
				i+=3;
			}
			//suma uno para saltarse el mensaje de CATEGORIASRECETA
			i++;
			//agregar categorias de recetas
			while(!(message.get(i).contentEquals("INICIOMULTIMEDIA"))){
				catRec.add(new Categoria(Integer.parseInt(message.get(i))));
				i++;
			}
			//suma uno para saltarse el mensaje de INICIOMULTIMEDIA
			i++;
			//agregar multimedia
			while(i < message.size()){
				mult.add(new Multimedia(message.get(i)));
				i++;
			}

			//cuarto elemento el json de ingredientes
		    stmt.setString(5,new Gson().toJson(ing));
			//cuarto elemento el json de multimedia
		    stmt.setString(6,new Gson().toJson(mult));
			//cuarto elemento el json de categorias de recetas
		    stmt.setString(7,new Gson().toJson(catRec));
		    
			stmt.execute();
			answer.add("SUBIRRECETAOK");
		}catch(SQLException e) {
			sqlExceptionHandler(e, "SUBIRRECETAFAIL");
		} catch(NumberFormatException e){
			intExceptionHandler(e, "SUBIRRECETAFAIL");
		}
	}
	
	private void usuPregSeg() {
		try {
			pstmt = conn.prepareStatement(USUPREGSEG);
			//pone de parametro el nickname que le paso el cliente
			pstmt.setString(1, message.get(1));
			ResultSet rs = pstmt.executeQuery();
			//si no hubo ningun error ejecutando el query, entonces pone este mensaje
			if(rs.next()) {

				answer.add("RESPUSUPREGSEG");
				//id de la pregunta
				answer.add(String.valueOf(rs.getInt(1)));
				//pregunta del usuario
				answer.add(rs.getString(2));
			}else {
				throw new SQLException("Error en la consulta", "45000");
			}
		}catch(SQLException e) {
			sqlExceptionHandler(e, "RESPUSUPREGSEGFAIL");
		}
	}
	
	protected void opcionesCliente(String peticion) {
		//segun la peticion, ejecuta cierto metodo
        switch(peticion) {
	        case "BORRARRECUSU":
	        	borrarRecetaUsu();
	        	break;
	        case "CALIFICAR"://
	        	if(stringValidator.esCalificarValido()) {
	        		calificar();
	        	}else {
	        		answer.add("FORMATERROR");
	        	}
	        	break;
	        case "CAMBIARCONTRA": //
	        	if(stringValidator.esCambiarContraValido()) {
	        		cambiarContra();
	        	}else {
	        		answer.add("FORMATERROR");	        		
	        	}
	        	break;
			case "CONSCALIFUSUARIO":
				consCalifUsuario();
				break;
	        case "CONSRECETASCATING":
	        	consRecetasCatRecIng(false);
	        	break;
	        case "CONSRECETASCATREC":
	        	consRecetasCatRecIng(true);
	        	break;
	        case "CONSRECETAING":
	        	consRecetaIng();
	        	break;
	        case "CONSRECETATEXT":
	        	consRecetaText();
	        	break;
			case "CONSTOPRECETAS":
				consTopRecetas();
				break;
	        case "DATOSRECETA":
	        	datosReceta();
	        	break;
	        case "INGREDIENTES":
	        	ingredientes();
	        	break;
	        case "LISTARCATING":
	        	listarCatIng();
	        	break;
	        case "LISTARCATREC":
	        	listarCatRec();
	        	break;
	        case "LOGIN":
	        	login();
	        	break;
	        case "PREGUNTASSEG":
	        	preguntasSeg();
	        	break;
	        case "RECETASDEUSUARIO":
	        	recetaUsuario();
	        	break;
	        case "REGISTRO":   //       	
	        	if(stringValidator.esResgistroValido()) {     		
	        		registro();     
	        	}else {
	        		answer.add("FORMATERROR");
	        	}
	    		break;
			case "SERVIDORVIVE":
				servidorVive();
				break;
	        case "SUBIRRECETA"://  	
	        	if(stringValidator.esSubirRecetaValido()) { 
	        		subirReceta();    
	        	}else {
	        		answer.add("FORMATERROR");
	        	}
	        	break;
	        case "USUPREGSEG":
	        	usuPregSeg();
	        	break;
	    	default:
	    		//se manda esta respuesta si la peticion es invalida
	    		answer.add("MESSAGEERROR");
        }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {	
			System.out.println("Client: Connected with client" + this.socket);
			//inicializacion de los atributos
			this.conn = cpds.getConnection();
			this.answer = new ArrayList<String>();
			ObjectOutputStream output = new ObjectOutputStream(this.socket.getOutputStream());
	        ObjectInputStream input = new ObjectInputStream(this.socket.getInputStream());
	        //recibe el mensaje del cliente
			this.message = (ArrayList<String>) input.readObject();
			System.out.println("Recived " + message.get(0) + " from socket: " + this.socket);
			stringValidator = new ArrayListStringValidator(message);
			if(stringValidator.elementArrayListBlank(message)) {
				answer.add("ELEMENTBLANK");
			}else {
				//switch de opcioens del cliente
		        opcionesCliente(message.get(0));
			}
	        //una vez ejecutados los metodos correspondientes, manda la respuesta
    		output.writeObject(answer);
    		//vacia el objeto para la proxima respuesta
			answer.clear();
			
		}catch (Exception e){
        	System.out.println("Client error: " + e.getMessage() + " in socket: " + socket);
		}finally {

			try {
				if(conn != null){
					this.conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Client error: " + e.getMessage() + " in socket: " + socket);
			}
			try {
	        	this.socket.close();
	        } catch (Exception e) {
	        	System.out.println("Client error: " + e.getMessage() + " in socket: " + socket);
	        }
	        System.out.println("Closed: " + socket );
	        System.out.println();
		}
	}
}
