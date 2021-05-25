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
public class JuegoImagen {
    
    private int id_juegoImagen;
    private String opinion;
    Juego juego = new Juego();
    Imagen imagen = new Imagen();

    public int getId_juegoImagen() {
        return id_juegoImagen;
    }

    public void setId_juegoImagen(int id_juegoImagen) {
        this.id_juegoImagen = id_juegoImagen;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }
    
    
    
}
