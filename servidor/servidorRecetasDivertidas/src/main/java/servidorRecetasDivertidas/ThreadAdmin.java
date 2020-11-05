package servidorRecetasDivertidas;

import com.google.gson.Gson;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import jsonClasess.Categoria;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ThreadAdmin extends ThreadClient{

	public ThreadAdmin(ComboPooledDataSource c, Socket s) {
		super(c, s);
	}

	private static final String BANEARUSUARIO = "{call spBaneoUsuario(?)}";
	private static final String BORRARCATING = "{call spBorrarCategoriaIngrediente(?)}}";
	private static final String BORRARCATREC = "{call spBorrarCategoriaReceta(?)}";
	private static final String BORRARREC = "{call spAdminBorrarReceta(?)}";
	private static final String BORRARING = "{}";
	private static final String SUBIRCATING = "{call spAgregarCategoriaIngrediente(?)}";
	private static final String SUBIRCATREC = "{call spAgregarCategoriaReceta(?)}"; 
	private static final String SUBIRING = "{call spAgregarIngrediente(?,?)}";
	
	private void borrarCatIng() {
		try {
			stmt = conn.prepareCall(BORRARCATING);
			//id de la categoria de ingrediente
			stmt.setInt(1, Integer.parseInt(message.get(1)));
			stmt.execute();
			answer.add("BORRARCATINGOK");
		} catch (SQLException e) {
			sqlExceptionHandler(e, "BORRARCATINGFAIL");
		} catch(NumberFormatException e){
			intExceptionHandler(e, "BORRARCATINGFAIL");
		}
	}
	
	private void borrarCatRec() {
		try {
			stmt = conn.prepareCall(BORRARCATREC);
			//id de la categoria de receta
			stmt.setInt(1, Integer.parseInt(message.get(1)));
			stmt.execute();
			answer.add("BORRARCATRECOK");
		} catch (SQLException e) {
			sqlExceptionHandler(e, "BORRARCATRECFAIL");
		}catch(NumberFormatException e){
			intExceptionHandler(e, "BORRARCATRECFAIL");
		}
	}
	
	private void borrarRec(){
		try {
			stmt = conn.prepareCall(BORRARREC);
			//id de la receta
			stmt.setInt(1, Integer.parseInt(message.get(1)));
			stmt.execute();
			answer.add("BORRARRECOK");
		} catch (SQLException e) {
			sqlExceptionHandler(e, "BORRARRECFAIL");
		} catch(NumberFormatException e){
			intExceptionHandler(e, "BORRARRECFAIL");
		}
	}

	private void borrarIng(){

	}
	
	private void banearUsuario() {
		try {
			stmt = conn.prepareCall(BANEARUSUARIO);
			stmt.setString(1, message.get(1));			
			stmt.execute();
			answer.add("BANEARUSUOK");
		}catch(SQLException e) {
			sqlExceptionHandler(e, "BORRARUSUFAIL");
		}
	}
	
	//true: subir ingrediente
	//false: subir receta
	private void subirCatRecIng(boolean recoing) {
		String ok, fail = "";
		try {
			if(recoing) {
				stmt = conn.prepareCall(SUBIRCATING);
				ok = "SUBIRCATINGOK";
				fail = "SUBIRCATINGFAIL";
			}else {
				stmt = conn.prepareCall(SUBIRCATREC);		
				ok = "SUBIRCATRECOK";
				fail = "SUBIRCATRECFAIL";
			}
			stmt.setString(1,message.get(1));
			stmt.execute();
			answer.add(ok);
		} catch (SQLException e) {
			sqlExceptionHandler(e,fail);
		}
	}
	
	private void subirIng() {
		try {
			stmt = conn.prepareCall(SUBIRING);
			ArrayList<Categoria> catIng = new ArrayList<Categoria>();
			//primer parametro: nombre del ingrediente
			stmt.setString(1, message.get(1));
			int i = 2;
			//Arma el json con los ingredientes
			while(i < message.size()) {
				catIng.add(new Categoria(Integer.parseInt(message.get(i))));
				i++;
			}
			stmt.setString(2, new Gson().toJson(catIng));
			stmt.execute();
			answer.add("SUBIRINGOK");
		}catch (SQLException e) {
			sqlExceptionHandler(e, "SUBIRINGFAIL");
		}catch(NumberFormatException e){
			intExceptionHandler(e, "SUBIRINGFAIL");
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		try {
			System.out.println("Admin: Connected with admin" + this.socket);
			//inicializacion de los atributos
			this.conn = cpds.getConnection();		    
	        ObjectOutputStream output = new ObjectOutputStream(this.socket.getOutputStream());
	        ObjectInputStream input = new ObjectInputStream(this.socket.getInputStream());
	        this.answer = new ArrayList<String>();	        
	        //recibe el mensaje del cliente
			this.message = (ArrayList<String>) input.readObject();
			stringValidator = new ArrayListStringValidator(message);		
			if(stringValidator.elementArrayListBlank(message)) {
				answer.add("ELEMENTBLANK");
			}else {
				switch(message.get(0)) {
				case "BORRARCATING":
					borrarCatIng();
					break;
				case "BORRARCATREC":
					borrarCatRec();
					break;
				case "BORRARREC":
					borrarRec();
					break;
				case "BORRARING":
					borrarIng();
					break;
				case "BANEARUSUARIO":
					banearUsuario();
					break;
				case "SUBIRCATING"://
		        	if(stringValidator.esSubirCatValido()) { 
						subirCatRecIng(true);
		        	}else {
		        		answer.add("FORMATERROR");
		        	}
					break;
				case "SUBIRCATREC"://		        	
					if(stringValidator.esSubirCatValido()) { 
						subirCatRecIng(false);
		        	}else {
		        		answer.add("FORMATERROR");
		        	}
					break;
				case "SUBIRING"://
					if(stringValidator.esSubirIngValido()) { 
						subirIng();
		        	}else {
		        		answer.add("FORMATERROR");
		        	}
					break;
				default:	    		
					opcionesCliente(message.get(0));
					break;
				}
		        //una vez ejecutados los metodos correspondientes, manda la respuesta
	    		output.writeObject(answer);
	    		//vacia el objeto para la proxima respuesta
				answer.clear();
			}
		
		}catch (Exception e){
        	System.out.println();
        	System.out.println("Admin error: " + e.getMessage() + " in socket: " + socket);
		}finally {

			try {
				this.conn.close();
			} catch (SQLException e) {
				System.out.println("Admin error: " + e.getMessage() + " in socket: " + socket);
			}
			try {
				this.socket.close();
			} catch (Exception e) {
				System.out.println("Admin error: " + e.getMessage() + " in socket: " + socket);
			}
			System.out.println("Closed: " + socket );
			System.out.println();
		}
	}

}
