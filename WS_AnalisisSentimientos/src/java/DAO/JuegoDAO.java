/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Clases.ConexionPostgreSQL;
import Modelo.Imagen;
import Modelo.Juego;
import Modelo.JuegoImagen;
import Modelo.Persona;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author carlos
 */
public class JuegoDAO {

    HttpResponse<String> respuesta;
    String salida = "";

    private ConexionPostgreSQL connecPostgresql;

    public String analizarSentimientos(String frase) {
        try {
            respuesta = Unirest.post("https://api.meaningcloud.com/sentiment-2.1")
                    .header("content-type", "application/x-www-form-urlencoded")
                    .body("key=88c11eb49d029e853ed7d5096dd9ccb7"
                            + "&lang=es"
                            + "&txt=" + frase
                            + "&txtf=plain&url=YOUR_URL_VALUE&doc=YOUR_DOC_VALUE")
                    .asString();

            salida = respuesta.getBody();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return salida;
    }

    public int registrarJuego(Juego juego, String analisis) throws SQLException {
        int salida = 0;
        try {
            ResultSet consulta;
            connecPostgresql = new ConexionPostgreSQL();
            JSONObject objSentimientos = new JSONObject(analisis);   

            connecPostgresql.callableStatement = connecPostgresql.connection.prepareCall("{call registrarjuego (?,?)}");
            connecPostgresql.callableStatement.setInt(1, juego.getPersona().getId_persona());
            connecPostgresql.callableStatement.setString(2, objSentimientos.getString("score_tag"));

            consulta = connecPostgresql.callableStatement.executeQuery();

            if (consulta.next()) {
                salida = consulta.getInt(1);
            }

            connecPostgresql.getConnection().close();

            return salida;
        } catch (SQLException ex) {
            connecPostgresql.getConnection().close();
            System.out.println(ex.getMessage());
            return salida;
        }
    }

    public boolean registrarJuegoImagen(JuegoImagen juego_imagen) throws SQLException {
        try {
            connecPostgresql = new ConexionPostgreSQL();
            connecPostgresql.callableStatement = connecPostgresql.connection.prepareCall("{call registrarjuegoimagen (?,?,?)}");
            connecPostgresql.callableStatement.setInt(1, juego_imagen.getJuego().getId_juego());
            connecPostgresql.callableStatement.setInt(2, juego_imagen.getImagen().getId_imagen());
            connecPostgresql.callableStatement.setString(3, juego_imagen.getOpinion());

            connecPostgresql.callableStatement.executeUpdate();
            connecPostgresql.getConnection().close();
            return true;
        } catch (SQLException ex) {
            connecPostgresql.getConnection().close();
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public String obtenerAnalisis(Juego juego) throws SQLException {
        String salida = "";
        try {
            ResultSet consulta;
            connecPostgresql = new ConexionPostgreSQL();

            connecPostgresql.callableStatement = connecPostgresql.connection.prepareCall("{call obteneranalisis (?)}");
            connecPostgresql.callableStatement.setInt(1, juego.getId_juego());

            consulta = connecPostgresql.callableStatement.executeQuery();

            if (consulta.next()) {
                salida = consulta.getString(1);
            }

            connecPostgresql.getConnection().close();

            return salida;
        } catch (SQLException ex) {
            connecPostgresql.getConnection().close();
            System.out.println(ex.getMessage());
            return salida;
        }
    }

}
