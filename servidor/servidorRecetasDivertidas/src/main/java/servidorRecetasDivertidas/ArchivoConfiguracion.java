package servidorRecetasDivertidas;

import java.io.*;
import java.util.Properties;

//    System.out.println(System.getProperty("user.dir")); directorio actual

public class ArchivoConfiguracion extends File {
    private boolean tuvoQueCrearArchivo = false;
    private String mysqlIp;
    private String puerto;
    private String usuarioMysql;
    private String passMysql;


    public ArchivoConfiguracion(String path) {
        super(path);

        // Crea el archivo si no existe

        try {
            if (this.createNewFile()) {
                FileWriter writer = new FileWriter(this);
                writer.write("Port=7070\r\n" +
                            "Mysql-IP=localhost\r\n" +
                            "Mysql-user=root\r\n" +
                            "Mysql-pass=12345");
                writer.close();
                tuvoQueCrearArchivo = true;
            } else {
                cargarDatos();
            }
        } catch (Exception e){
            System.out.println("Hubo un error inesperado al crear el archivo de configuración.");
        }
    }

    public boolean getTuvoQueCrearArchivo() {
        return tuvoQueCrearArchivo;
    }

    public String getIp() {
        return mysqlIp;
    }

    public int getPuerto() {
        return Integer.parseInt(puerto);
    }

    public String getUsuarioMysql() {
        return usuarioMysql;
    }

    public String getPassMysql() {
        return passMysql;
    }

    private void cargarDatos() {
        try {
            Properties propiedades = new Properties();

            FileInputStream file = new FileInputStream(this.getPath());
            propiedades.load(file);
            file.close();
            this.puerto = propiedades.getProperty("Port");
            this.mysqlIp = propiedades.getProperty("Mysql-IP");
            this.usuarioMysql = propiedades.getProperty("Mysql-user");
            this.passMysql = propiedades.getProperty("Mysql-pass");
        } catch (Exception e) {
            System.out.println("Hubo un error inesperado al leer el archivo de configuración.");
        }
    }

    public boolean validarDatos() {
        if (ipEsInvalida(mysqlIp) && !mysqlIp.equals("localhost")) {
            System.out.println("Error: Dirección IP inválida.");
            System.out.println(mysqlIp);
            return false;
        }

        if (puertoEsInvalido(puerto)) {
            System.out.println("Error: Puerto inválido.");
            return false;
        }
        if (usuarioMysql == null || usuarioMysql.equals("")) {
            System.out.println("Error: El usuario de MySQL tiene que estar especificado");
            return false;
        }

        return true;
    }

    private boolean ipEsInvalida(String ip) {
        String regex = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return !ip.matches(regex);
    }

    private boolean puertoEsInvalido(String puerto) {
        String regex = "\\d{1,5}";
        if (!puerto.matches(regex)) return true;

        int puertoInt = Integer.parseInt(puerto);
        return puertoInt < 0 || puertoInt > 65535;
    }
}
