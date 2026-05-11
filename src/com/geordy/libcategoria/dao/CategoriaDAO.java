package com.geordy.libcategoria.dao;

import com.geordy.libcategoria.dto.CategoriaDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class CategoriaDAO implements AutoCloseable {

    private final Connection conexion;
    private final boolean ownsConnection;

    /** Uso standalone: requiere llamar Conexion.configurar() antes. */
    public CategoriaDAO() {
        this.conexion = Conexion.conectar();
        this.ownsConnection = true;
    }

    /** Uso con Spring Boot: pasa la conexión de tu DataSource. */
    public CategoriaDAO(Connection conexion) {
        if (conexion == null) throw new IllegalArgumentException("Connection no puede ser null");
        this.conexion = conexion;
        this.ownsConnection = false;
    }

    /** Uso con Spring Boot DataSource — gestión automática de conexión. */
    public CategoriaDAO(DataSource dataSource) throws SQLException {
        if (dataSource == null) throw new IllegalArgumentException("DataSource no puede ser null");
        this.conexion = dataSource.getConnection();
        this.ownsConnection = true;
    }

    public boolean insertar(CategoriaDTO categoria) {
        String sql = "INSERT INTO categorias (idcategoria, nombrecategoria, descripcion) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, categoria.getIdcategoria());
            ps.setString(2, categoria.getNombrecategoria());
            ps.setString(3, categoria.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar categoria: " + e.getMessage(), e);
        }
    }

    public CategoriaDTO obtenerPorId(int id) {
        String sql = "SELECT * FROM categorias WHERE idcategoria = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener categoria: " + e.getMessage(), e);
        }
        return null;
    }

    public List<CategoriaDTO> obtenerTodas() {
        List<CategoriaDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las categorias: " + e.getMessage(), e);
        }
        return lista;
    }

    public boolean actualizar(CategoriaDTO categoria) {
        String sql = "UPDATE categorias SET nombrecategoria = ?, descripcion = ? WHERE idcategoria = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombrecategoria());
            ps.setString(2, categoria.getDescripcion());
            ps.setInt(3, categoria.getIdcategoria());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar categoria: " + e.getMessage(), e);
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM categorias WHERE idcategoria = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar categoria: " + e.getMessage(), e);
        }
    }

    private CategoriaDTO mapRow(ResultSet rs) throws SQLException {
        return new CategoriaDTO(
            rs.getInt("idcategoria"),
            rs.getString("nombrecategoria"),
            rs.getString("descripcion")
        );
    }

    public void cerrar() {
        if (ownsConnection) Conexion.desconectar(conexion);
    }

    @Override
    public void close() {
        cerrar();
    }
}
