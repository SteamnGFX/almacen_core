package com.cu.al.core;

import com.cu.al.model.Producto;
import com.cu.al.db.ConexionMySQL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ControladorProducto {

    public int isNull;

    //Este método insertará un empleado
    //Recibe un objeto de tipo Empleado
    public int insert(Producto p) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call insertarProducto(?, ?, ?, ?, ?, ?, ?, ?)}";  // Valores de Retorno

        //Aquí guardaremos los ID's que se generarán:
        int idProducto = -1;

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setString(1, p.getNombre());
        cstmt.setString(2, p.getTipo());
        cstmt.setString(3, p.getMarca());
        cstmt.setString(4, p.getDescripcion());
        cstmt.setInt(5, p.getExistencias());
        cstmt.setString(6, p.getProveedor());
        cstmt.setString(7, p.getFechaEntrada());

        //Registramos los parámetros de salida:
        cstmt.registerOutParameter(8, Types.INTEGER);

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idProducto = cstmt.getInt(8);

        p.setIdProducto(idProducto);

        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID del producto generado:
        return idProducto;
    }

    public void update(Producto p) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call actualizarProducto(?, ?, ?, ?, ?, ? , ?, ?)}";  // Valores de Retorno

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setString(1, p.getNombre());
        cstmt.setString(2, p.getTipo());
        cstmt.setString(3, p.getMarca());
        cstmt.setString(4, p.getDescripcion());
        cstmt.setInt(5, p.getExistencias());
        cstmt.setString(6, p.getProveedor());
        cstmt.setString(7, p.getFechaEntrada());
        cstmt.setInt(8, p.getIdProducto());                

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public void delete(int id) throws Exception {

        String sql = "{call eliminarProducto(?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setInt(1, id);

        cstmt.executeUpdate();
        connMySQL.close();
    }

    public List<Producto> getAll(String filtro) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM producto";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();

        List<Producto> producto = new ArrayList<>();

        while (rs.next()) {
            producto.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return producto;
    }

    private Producto fill(ResultSet rs) throws Exception {
        Producto p = new Producto();

        p.setIdProducto(rs.getInt("idProducto"));
        p.setNombre(rs.getString("nombre"));
        p.setTipo(rs.getString("tipo"));
        p.setMarca(rs.getString("marca"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setExistencias(rs.getInt("existencias"));
        p.setProveedor(rs.getString("proveedor"));
        p.setFechaEntrada(rs.getString("fecha"));
        p.setEstatus(rs.getInt("estatus"));
        return p;
    }

}
