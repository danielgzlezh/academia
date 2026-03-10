package com.example.demo.dto;

public class AlumnoCreateDto {

    private String nombre;
    private String email;

    public AlumnoCreateDto() {
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
