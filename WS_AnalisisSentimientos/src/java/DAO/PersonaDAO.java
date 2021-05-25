/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Clases.ConexionPostgreSQL;
import Modelo.Imagen;
import Modelo.Persona;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author carlos
 */
public class PersonaDAO {

    private ConexionPostgreSQL connecPostgresql;

    public Persona login(Persona person) throws SQLException {
        Persona persona = null;
        ResultSet consulta;

        try {
            connecPostgresql = new ConexionPostgreSQL();

            if (connecPostgresql.getMessage().equals("ok")) {
                connecPostgresql.callableStatement = connecPostgresql.connection.prepareCall("{call login (?,?)}");
                connecPostgresql.callableStatement.setString(1, person.getUsuario());
                connecPostgresql.callableStatement.setString(2, person.getClave());

                consulta = connecPostgresql.callableStatement.executeQuery();

                if (consulta.next()) {
                    persona = new Persona();

                    persona.setId_persona(consulta.getInt("id_persona"));
                    persona.setNombres(consulta.getString("nombres"));
                    persona.setApellidos(consulta.getString("apellidos"));
                    persona.getTipoUsuario().setId_tipoUsuario(consulta.getInt("id_tipousuario"));
                }
            }

        } catch (Exception ex) {
            connecPostgresql.getConnection().close();
            System.out.println(ex.getMessage());
        }

        connecPostgresql.getConnection().close();
        return persona;
    }

    public boolean registrarJugador(Persona persona) throws SQLException {
        try {
            connecPostgresql = new ConexionPostgreSQL();
            connecPostgresql.callableStatement = connecPostgresql.connection.prepareCall("{call registrarjugador (?,?,?,?,?,?,?)}");
            connecPostgresql.callableStatement.setString(1, persona.getNombres());
            connecPostgresql.callableStatement.setString(2, persona.getApellidos());
            connecPostgresql.callableStatement.setString(3, persona.getTelefono());
            connecPostgresql.callableStatement.setString(4, persona.getFechaNacimiento());
            connecPostgresql.callableStatement.setString(5, persona.getUsuario());
            connecPostgresql.callableStatement.setString(6, persona.getClave());
            connecPostgresql.callableStatement.setString(7, persona.getGenero());

            connecPostgresql.callableStatement.executeUpdate();
            connecPostgresql.getConnection().close();
            return true;
        } catch (SQLException ex) {
            connecPostgresql.getConnection().close();
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean buscarUsuario(Persona persona) throws SQLException {
        try {
            String usuario = "";
            boolean b = false;
            ResultSet consulta;
            connecPostgresql = new ConexionPostgreSQL();
            if (connecPostgresql.getMessage().equals("ok")) {
                connecPostgresql.callableStatement = connecPostgresql.connection.prepareCall("{call buscarusuario(?)}");
                connecPostgresql.callableStatement.setString(1, persona.getUsuario());
                consulta = connecPostgresql.callableStatement.executeQuery();
                if (consulta.next()) {
                    usuario = consulta.getString(1);

                    if (usuario.equals("si")) {
                        b = true;
                    }
                }
                connecPostgresql.getConnection().close();
            }
            return b;
        } catch (SQLException ex) {
            connecPostgresql.getConnection().close();
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public ArrayList<Persona> listarJugadores() throws ParseException, SQLException {
        
        ArrayList<Persona> lista = new ArrayList<>();
        try {
            connecPostgresql = new ConexionPostgreSQL();
            ResultSet consulta;
            connecPostgresql.callableStatement = connecPostgresql.connection.prepareCall("{call mostrar_jugadores()}");
            consulta = connecPostgresql.callableStatement.executeQuery();
            while (consulta.next()) {

                Persona persona = new Persona();

                persona.setId_persona(consulta.getInt("idpersona"));
                persona.setNombres(consulta.getString("nombres"));
                persona.setApellidos(consulta.getString("apellidos"));
                persona.setTelefono(consulta.getString("telefono"));
                persona.setFechaNacimiento(consulta.getString("fecha_nacimiento"));
                persona.setGenero(consulta.getString("genero"));
                persona.setUsuario(consulta.getString("usuario"));
                

                lista.add(persona);
            }

        } catch (SQLException ex) {
            connecPostgresql.getConnection().close();
            System.out.println(ex.getMessage());
        }
        connecPostgresql.getConnection().close();
        return lista;
    }
    
    public boolean eliminarJugador(Persona persona) throws SQLException {
        try {
            connecPostgresql = new ConexionPostgreSQL();
            connecPostgresql.callableStatement = connecPostgresql.connection.prepareCall("{call eliminar_jugador(?)}");
            connecPostgresql.callableStatement.setInt(1, persona.getId_persona());
          
            connecPostgresql.callableStatement.executeUpdate();
            connecPostgresql.getConnection().close();
            return true;
        } catch (SQLException ex) {
            connecPostgresql.getConnection().close();
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean modificarJugador(Persona persona) throws SQLException {
        try {
            connecPostgresql = new ConexionPostgreSQL();
            connecPostgresql.callableStatement = connecPostgresql.connection.prepareCall("{call modificar_jugador (?,?,?,?,?,?)}");
            connecPostgresql.callableStatement.setInt(1, persona.getId_persona());
            connecPostgresql.callableStatement.setString(2, persona.getNombres());
            connecPostgresql.callableStatement.setString(3, persona.getApellidos());
            connecPostgresql.callableStatement.setString(4, persona.getTelefono());
            connecPostgresql.callableStatement.setString(5, persona.getFechaNacimiento());
            connecPostgresql.callableStatement.setString(6, persona.getGenero());

            connecPostgresql.callableStatement.executeUpdate();
            connecPostgresql.getConnection().close();
            return true;
        } catch (SQLException ex) {
            connecPostgresql.getConnection().close();
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
}
