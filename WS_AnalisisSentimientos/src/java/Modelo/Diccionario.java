/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author carlos
 */
public class Diccionario {
    
    private int id_palabra;
    private String palabra, sentimiento;

    public int getId_palabra() {
        return id_palabra;
    }

    public void setId_palabra(int id_palabra) {
        this.id_palabra = id_palabra;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public String getSentimiento() {
        return sentimiento;
    }

    public void setSentimiento(String sentimiento) {
        this.sentimiento = sentimiento;
    }
    
    
    
}
