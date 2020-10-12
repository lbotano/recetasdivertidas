package servidorRecetasDivertidas;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public abstract class User {
	//referencia al objeto que maneja las conexiones a la base de datos
	protected static ComboPooledDataSource cpds;
	//objeto que maneja el enlace con el cliente
	protected Socket socket;
	//objetos que guardan los mensajes que se transmiten
	protected ArrayList<String> answer;
	protected ArrayList<String> message;
	//objeto para llamar a store procedures
	protected CallableStatement stmt;
	//objeto para guardar la conexion a la base de datos
	protected Connection conn;
	//objetos para pasar mensajes
	protected ObjectOutputStream output;
	protected ObjectInputStream input;
}
