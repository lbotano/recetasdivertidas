package servidorRecetasDivertidas;

import java.io.*;

public class ArchivoConfiguracion extends File {
    private boolean tuvoQueCrearArchivo = false;
    private String ip;
    private String puertoAdmin;
    private String puertoCliente;
    private String usuarioMysql;
    private String passMysql;

    public ArchivoConfiguracion(String path) {
        super(path);
        // Crea el archivo si no existe

        try {
            if (this.createNewFile()) {
                FileWriter writer = new FileWriter(this);
                writer.write("IP: localhost\r\n" +
                        "Admin-port: 6969\r\n" +
                        "Client-port: 7070\r\n" +
                        "Mysql-user: root\r\n" +
                        "Mysql-pass: 12345");
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
        return ip;
    }

    public int getPuertoAdmin() {
        return Integer.parseInt(puertoAdmin);
    }

    public int getPuertoCliente() {
        return Integer.parseInt(puertoCliente);
    }

    public String getUsuarioMysql() {
        return usuarioMysql;
    }

    public String getPassMysql() {
        return passMysql;
    }

    private void cargarDatos() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this));
            String linea, dato, valor;

            while ((linea = br.readLine()) != null) {
                dato = linea.substring(0, linea.indexOf(':')).strip();
                valor = linea.substring(dato.length() + 1).strip();

                switch (dato) {
                    case "IP":
                        this.ip = valor;
                        break;
                    case "Admin-port":
                        this.puertoAdmin = valor;
                        break;
                    case "Client-port":
                        this.puertoCliente = valor;
                        break;
                    case "Mysql-user":
                        this.usuarioMysql = valor;
                        break;
                    case "Mysql-pass":
                        this.passMysql = valor;
                }
            }
        } catch (Exception e) {
            System.out.println("Hubo un error inesperado al leer el archivo de configuración.");
        }
    }

    public boolean validarDatos() {
        if (ipEsInvalida(ip) && !ip.equals("localhost")) {
            System.out.println("Error: Dirección IP inválida.");
            System.out.println(ip);
            return false;
        }

        if (puertoAdmin.equals(puertoCliente)){
            System.out.println("Error: El puerto del admin y el puerto del cliente no pueden ser el mismo.");
            return false;
        }

        if (puertoEsInvalido(puertoAdmin)) {
            System.out.println("Error: Puerto para admin inválido.");
            return false;
        }

        if (puertoEsInvalido(puertoCliente)) {
            System.out.println("Error: Puerto para cliente inválido.");
            return false;
        }

        if (usuarioMysql == null || usuarioMysql.equals("")) {
            System.out.println("Error: El usuario de MySQL tiene que estar especificado");
            System.out.println(usuarioMysql);
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
