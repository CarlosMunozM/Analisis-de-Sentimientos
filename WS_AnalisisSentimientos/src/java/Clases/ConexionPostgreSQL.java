/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Carlos Muñoz
 */
public class ConexionPostgreSQL {

    //DATOS PARA LA CONEXION
  
    String user = "postgres";
    //String password = "12345";
    //String urlDatabase = "jdbc:postgresql://localhost:5432/analisis_sentimientos";
    String urlDatabase = "jdbc:postgresql://156.96.46.31:5432/analisis_sentimientos";
    String password = "1234567890";

    public Connection connection;
    Statement statement;
    String message;
    ResultSet resultSet;
    public CallableStatement callableStatement;

    //Constructor de clase que se conecta a la base de datoS
    public ConexionPostgreSQL() {

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(urlDatabase, user, password);
            message = "ok";
        } catch (Exception e) {
            System.out.println("Ocurrio un error : " + e.getMessage());
            message = e.getMessage();
        }
        //System.out.println("La conexión se realizo sin problemas! =) ");
    }
    
    public static  Connection ConexionPostgreSQL(String url,String user,String password) {
        Connection conneccion = null;
        try {
            Class.forName("org.postgresql.Driver");
            conneccion = DriverManager.getConnection(url, user, password);
            //message = "ok";
        } catch (Exception e) {
            System.out.println("Ocurrio un error : " + e.getMessage());
            //message = e.getMessage();
        }
        //System.out.println("La conexión se realizo sin problemas! =) ");
        return conneccion;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Connection ConexionPostgreSQLABD() {

        return connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    
    

    public ResultSet select(String sentence) {
        //ResultSet resultSet = null;

        if (message.equals("ok")) {
            try {
                statement = getConnection().createStatement();
                resultSet = statement.executeQuery(sentence);
            } catch (SQLException exSQL) {
                message = exSQL.getMessage();
            }
        }
        return resultSet;
    }

    public boolean ejecutar(String sentence) {
        //ResultSet resultSet = null;
        boolean b = false;
        if (message.equals("ok")) {
            try {
                statement = getConnection().createStatement();
                statement.execute(sentence);
                b = true;
            } catch (SQLException exSQL) {
                b = false;
                message = exSQL.getMessage();
            }
        }
        return b;
    }

    public int entero(String sentence) {
        int b = 0;
        if (message.equals("ok")) {
            try {
                statement = getConnection().createStatement();
                resultSet = statement.executeQuery(sentence);
                while (resultSet.next()) {
                    b = resultSet.getInt(1);
                }

            } catch (SQLException exSQL) {
                b = 0;
                message = exSQL.getMessage();
            }
        }

        return b;
    }
}
