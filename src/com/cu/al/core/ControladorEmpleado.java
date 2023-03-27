package com.cu.al.core;

import com.cu.al.db.ConexionMySQL;
import com.cu.al.model.Empleado;
import com.cu.al.model.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Componentes Unidos
 * @date 14/03/2023
 */
public class ControladorEmpleado {

    public int isNull;

    //Es un método static...
    //Comprobaremos si es administrador
    public static boolean isAdmin(Empleado e) {
        if (e == null || e.getUsuario() == null || e.getUsuario().getUsuario() == null) {
            return false;
        } else {
            return e.getUsuario().getRol().trim().toLowerCase().equals("administrador");
        }
    }

    //Este método insertará un empleado
    //Recibe un objeto de tipo Empleado
    public int insert(Empleado e) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call insertarEmpleado(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";  // Valores de Retorno

        //Aquí guardaremos los ID's que se generarán:
        String lastTokenGenerado = "";
        int idEmpleadoGenerado = -1;
        int idUsuarioGenerado = -1;
        String numeroUnicoGenerado = "";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        
        System.out.println(e.toString());
        
        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setString(1, e.getNombre());
        cstmt.setString(2, e.getApellidoPaterno());
        cstmt.setString(3, e.getApellidoMaterno());
        cstmt.setString(4, e.getGenero());
        cstmt.setString(5, e.getTelefonoFijo());
        cstmt.setString(6, e.getTelefonoMovil());
        cstmt.setString(7, e.getCorreo());
        cstmt.setString(8, e.getRfc());

        // Registramos parámetros de datos de seguridad:
        cstmt.setString(9, e.getUsuario().getUsuario());
        cstmt.setString(10, e.getUsuario().getContrasenia());
        cstmt.setString(11, e.getUsuario().getRol());

        //Registramos los parámetros de salida:
        cstmt.registerOutParameter(12, Types.INTEGER);
        cstmt.registerOutParameter(13, Types.INTEGER);
        cstmt.registerOutParameter(14, Types.INTEGER);
        cstmt.registerOutParameter(15, Types.VARCHAR);

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idUsuarioGenerado = cstmt.getInt(12);
        idEmpleadoGenerado = cstmt.getInt(13);
        numeroUnicoGenerado = cstmt.getString(14);
        lastTokenGenerado = cstmt.getString(15);

        e.setIdEmpleado(idEmpleadoGenerado);
        e.getUsuario().setIdUsuario(idUsuarioGenerado);
        e.setNumeroUnico(numeroUnicoGenerado);

        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Cliente generado:
        return idEmpleadoGenerado;
    }

    public void update(Empleado e) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call actualizarEmpleado(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";// 13 IDs

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setString(1, e.getNombre());
        cstmt.setString(2, e.getApellidoPaterno());
        cstmt.setString(3, e.getApellidoMaterno());
        cstmt.setString(4, e.getGenero());
        cstmt.setString(5, e.getTelefonoFijo());
        cstmt.setString(6, e.getTelefonoMovil());
        cstmt.setString(7, e.getCorreo());
        cstmt.setString(8, e.getRfc());
        cstmt.setString(9, e.getUsuario().getUsuario());
        cstmt.setString(10, e.getUsuario().getContrasenia());
        cstmt.setString(11, e.getUsuario().getRol());

        cstmt.setInt(12, e.getUsuario().getIdUsuario());
        cstmt.setInt(13, e.getIdEmpleado());

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public void delete(int id) throws Exception {

        String sql = "{call eliminarEmpleado(?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setInt(1, id);

        cstmt.executeUpdate();
        connMySQL.close();
    }

    public List<Empleado> getAll(String filtro) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM v_empleados";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();

        List<Empleado> empleados = new ArrayList<>();

        while (rs.next()) {
            empleados.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return empleados;
    }

    private Empleado fill(ResultSet rs) throws Exception {
        Empleado e = new Empleado();

        e.setIdEmpleado(rs.getInt("idEmpleado"));
        e.setNombre(rs.getString("nombre"));
        e.setNumeroUnico(rs.getString("numeroUnico"));
        e.setApellidoMaterno(rs.getString("apellidoMaterno"));
        e.setApellidoPaterno(rs.getString("apellidoPaterno"));
        e.setCorreo(rs.getString("correo"));
        e.setGenero(rs.getString("genero"));
        e.setTelefonoFijo(rs.getString("telefonoFijo"));
        e.setTelefonoMovil(rs.getString("telefonoMovil"));
        e.setEstatus(rs.getString("estatus"));
        e.setRfc(rs.getString("rfc"));
        e.setUsuario(new Usuario());
        e.getUsuario().setIdUsuario(rs.getInt("idUsuario"));
        e.getUsuario().setContrasenia(rs.getString("contrasenia"));
        e.getUsuario().setUsuario(rs.getString("nombreUsuario"));
        e.getUsuario().setRol(rs.getString("rol"));
        e.setNumeroUnico(rs.getString("numeroUnico"));

        return e;
    }

    public Empleado login(String usuario, String contrasenia) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = "call loginEmpleado(?, ?)";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement ps = conn.prepareCall(sql);

        ps.setString(1, usuario);
        ps.setString(2, contrasenia);

        ResultSet rs = ps.executeQuery();

        Empleado empleado = null;

        if (rs.next()) {
            empleado = fill(rs);
        }

        rs.close();
        ps.close();
        connMySQL.close();

        return empleado;
    }

    public void guardarToken(List<Empleado> emp) throws Exception {
        String query = "UPDATE usuario SET LastToken = ? , dateLastToken = now() "
                + "WHERE idUsuario = ?";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement ps = conn.prepareCall(query);
        ps.setInt(2, emp.get(0).getUsuario().getIdUsuario());

        ps.execute();

        ps.close();
        conn.close();
        connMySQL.close();

    }

    public void eliminarToken(List<Empleado> emp, String token) throws Exception {
        String query = "UPDATE usuario SET LastToken = ? WHERE LastToken = ? and idUsuario = ?";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement ps = conn.prepareCall(query);

        ps.setString(1, "");
        ps.setString(2, token);
        ps.setInt(3, emp.get(0).getUsuario().getIdUsuario());

        ps.execute();

        ps.close();
        conn.close();
        connMySQL.close();

    }

}
