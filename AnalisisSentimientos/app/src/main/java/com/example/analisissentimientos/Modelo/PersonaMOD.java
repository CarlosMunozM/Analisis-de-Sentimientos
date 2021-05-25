package com.example.analisissentimientos.Modelo;

import java.util.Date;

public class PersonaMOD {

    private int id_persona;
    private String nombres, apellidos, telefono, usuario, clave, genero;
    private String fecha_nacimiento;
    TipoUsuarioMOD tipoUsuarioMOD = new TipoUsuarioMOD();

    public PersonaMOD() {
    }


    public PersonaMOD(int id_persona, String nombres, String apellidos, String telefono, String usuario, String genero, String fecha_nacimiento) {
        this.id_persona = id_persona;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.usuario = usuario;
        this.genero = genero;
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public TipoUsuarioMOD getTipoUsuarioMOD() {
        return tipoUsuarioMOD;
    }

    public void setTipoUsuarioMOD(TipoUsuarioMOD tipoUsuarioMOD) {
        this.tipoUsuarioMOD = tipoUsuarioMOD;
    }
}
