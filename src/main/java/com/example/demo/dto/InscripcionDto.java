package com.example.demo.dto;

import java.time.LocalDateTime;

public class InscripcionDto {
	private Long id;
	private Long alumnoId;
	private Long cursoId;
	private LocalDateTime fechaInscripcion;
	private String estado;
	private String observaciones;

	public InscripcionDto() {
	}

	public InscripcionDto(Long id, Long alumnoId, Long cursoId, LocalDateTime fechaInscripcion, String estado,
			String observaciones) {
		this.id = id;
		this.alumnoId = alumnoId;
		this.cursoId = cursoId;
		this.fechaInscripcion = fechaInscripcion;
		this.estado = estado;
		this.observaciones = observaciones;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}

	public Long getCursoId() {
		return cursoId;
	}

	public void setCursoId(Long cursoId) {
		this.cursoId = cursoId;
	}

	public LocalDateTime getFechaInscripcion() {
		return fechaInscripcion;
	}

	public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	

}
