package com.unipacifico.peluqueria.clases;

public class Administrador {
    String identificacion, telefono,contrasena,nombre;

    public Administrador(String identificacion, String telefono, String contrasena, String nombre) {
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.nombre = nombre;
    }

    public Administrador() {
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
