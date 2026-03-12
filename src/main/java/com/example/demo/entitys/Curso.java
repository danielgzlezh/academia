package com.example.demo.entitys;

import java.time.LocalDate;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Curso", schema = "academia")
public class Curso {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String categoria;
	private String nivel;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private int plazas;
	private boolean activo;

	public Curso() {
	}

	public Curso(String nombre, String categoria, String nivel, LocalDate fechaInicio, LocalDate fechaFin, int plazas,
			boolean activo) {
		this.nombre = nombre;
		this.categoria = categoria;
		this.nivel = nivel;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.plazas = plazas;
		this.activo = activo;
	}

	public Curso(Long id, String nombre, String categoria, String nivel, LocalDate fechaInicio, LocalDate fechaFin,
			int plazas, boolean activo) {
		this.id = id;
		this.nombre = nombre;
		this.categoria = categoria;
		this.nivel = nivel;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.plazas = plazas;
		this.activo = activo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechainicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechafin() {
		return fechaFin;
	}

	public void setFechafin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public int getPlazas() {
		return plazas;
	}

	public void setPlazas(int plazas) {
		this.plazas = plazas;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

}
