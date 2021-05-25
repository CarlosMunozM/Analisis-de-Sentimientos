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
public class SubMenu {
    
    private int id_subMenu;
    private String nombre;
    Menu menu = new Menu();

    public int getId_subMenu() {
        return id_subMenu;
    }

    public void setId_subMenu(int id_subMenu) {
        this.id_subMenu = id_subMenu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    
    
    
}
