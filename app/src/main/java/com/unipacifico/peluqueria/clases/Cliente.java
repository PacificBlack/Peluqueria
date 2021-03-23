package com.unipacifico.peluqueria.clases;

import java.io.Serializable;

public class Cliente implements Serializable {
    String telefono, nombre, apellido, direccion, contraseña;

    public Cliente() {
    }

    public Cliente(String telefono, String nombre, String apellido, String direccion, String contraseña) {
        this.telefono = telefono;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.contraseña = contraseña;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
