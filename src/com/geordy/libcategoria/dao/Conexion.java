package com.geordy.libcategoria.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {

    private static volatile String url;
    private static volatile String usuario;
    private static volatile String contrasena;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private Conexion() {}

    /**
     * Configura los datos de conexión antes de usar CategoriaDAO.
     * Llamar una sola vez al iniciar la aplicación (ej. en main o @PostConstruct).
     */
    public static void configurar(String jdbcUrl, String user, String password) {
        url = jdbcUrl;
        usuario = user;
        contrasena = password;
    }

    public static Connection conectar() {
        if (url == null) {
            throw new IllegalStateException(
                "Base de datos no configurada. "
                + "Llame Conexion.configurar(url, user, password) antes de usar el DAO.");
        }
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(url, usuario, contrasena);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL driver no encontrado en el classpath", e);
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar a la base de datos: " + e.getMessage(), e);
        }
    }

    public static void desconectar(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}
