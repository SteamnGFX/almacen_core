package com.cu.al.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionMySQL {

    Connection conn;

    public Connection open() {
        //Usuario de base de datos
        String user = "root";
        //Contraseña de base de datos
        String password = "root";
        //URL de BaseDeDatos
        String url = "jdbc:mysql://127.0.0.1:3306/optiqalumnos?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";

        //Realizamos un TRY para la conexión de BD
        try {
            //Registramos el manejador del controlador
            Class.forName("com.mysql.cj.jdbc.Driver");
            //conexión del driver, url, usuario y contraseña
            conn = DriverManager.getConnection(url, user, password);
            //devolvemos la conexión
            return conn;
        } //Realizamos una excepción
        catch (Exception e) {
            //Devolvemos la exepción
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception controlada.");
            }
        }

    }
}
