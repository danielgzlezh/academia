package com.example.demo.dto;

public class AlumnoUpdateDto {

    private String nombre;
    private String email;

    public AlumnoUpdateDto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}