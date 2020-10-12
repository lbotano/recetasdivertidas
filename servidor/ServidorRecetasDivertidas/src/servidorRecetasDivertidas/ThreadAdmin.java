package servidorRecetasDivertidas;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class ThreadAdmin extends User implements Runnable{

	//private static final String BORRARCATING = "";
	//private static final String BORRARCATREC = "";
	//private static final String BORRARUSUARIO = "";
	//private static final String BORRARREC = "";
	private static final String SUBIRCATING = "{call spAgregarCategoriaIngrediente(?)}";
	private static final String SUBIRCATREC = "{call spAgregarCategoriaReceta(?)}"; 
	
	public void borrarCatIng() {
		
	}
	
	public void borrarCatRec() {
		
	}
	
	public void borrarRec(){
		
	}
	
	public void borrarUsuario() {
		
	}
	
	//true: subir ingrediente
	//false: subir receta
	public void subirCatRecIng(boolean recoing) {
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
			answer.add(fail);
			if(e.getSQLState().contentEquals("45000")) {
        		answer.add(e.getMessage());
    		}else {
    			answer.add("Error en la base de datos");
    		}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			System.out.println("Connected with admin" + this.socket);
			//inicializacion de los atributos
			this.conn = cpds.getConnection();		    
	        this.output = new ObjectOutputStream(this.socket.getOutputStream());
	        this.input = new ObjectInputStream(this.socket.getInputStream());
	        this.answer = new ArrayList<String>();	        
	        //recibe el mensaje del cliente
			this.message = (ArrayList<String>) input.readObject();
			
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
			case "BORRARUSUARIO":
				borrarUsuario();
				break;
			case "SUBIRCATING":
				subirCatRecIng(true);
				break;
			case "SUBIRCATREC":
				subirCatRecIng(false);
				break;
			default:	    		
				//se manda esta respuesta si la peticion es invalida
	    		answer.add("MESSAGEERROR");
				break;
			}
	        //una vez ejecutados los metodos correspondientes, manda la respuesta
    		output.writeObject(answer);
    		//vacia el objeto para la proxima respuesta
			answer.clear();
		
		}catch (Exception e){
        	System.out.println();
        	System.out.println("Admin error: " + e.getMessage() + " in socket: " + socket);
		}finally {
	        try {
	        	this.output.close();
	        	this.input.close();
	        	this.conn.close();
	            this.socket.close();
	        } catch (Exception e) {
	        	System.out.println();
	        	System.out.println("Admin error: " + e.getMessage() + " in socket: " + socket);
	        }
	        System.out.println("Closed: " + socket );
	        System.out.println();
		}
	}

}
