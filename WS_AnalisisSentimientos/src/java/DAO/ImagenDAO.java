/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Clases.ConexionPostgreSQL;
import Modelo.Imagen;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 *
 * @author carlos
 */
public class ImagenDAO {
    
    private ConexionPostgreSQL connecPostgresql;
    
    public ArrayList<Imagen> listarImagenes() throws ParseException, SQLException {
        ArrayList<Imagen> lista = new ArrayList<>();
        try {
            connecPostgresql = new ConexionPostgreSQL();
            ResultSet consulta;
            Imagen imagen;
            connecPostgresql.callableStatement = connecPostgresql.connection.prepareCall("{call rutasimagenes()}");
            consulta = connecPostgresql.callableStatement.executeQuery();
            while (consulta.next()) {
                
                imagen = new Imagen();

                imagen.setId_imagen(consulta.getInt("id_imagen"));
                imagen.setRuta(consulta.getString("ruta"));
               
                lista.add(imagen);
            }

        } catch (SQLException ex) {
            connecPostgresql.getConnection().close();
            System.out.println(ex.getMessage());
        }
        connecPostgresql.getConnection().close();
        return lista;
    }
    
}
