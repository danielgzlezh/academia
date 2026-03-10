package com.example.demo.entitys;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Inscripcion", schema = "academia")
public class Inscripcion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @ManyToOne
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    private LocalDateTime fechaInscripcion;

    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estado;

    private String observaciones;
    public Inscripcion() {
    }

    public Inscripcion(Alumno alumno, Curso curso,
                       LocalDateTime fechaInscripcion,
                       EstadoInscripcion estado,
                       String observaciones) {
        this.alumno = alumno;
        this.curso = curso;
        this.fechaInscripcion = fechaInscripcion;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public Inscripcion(Long id, Alumno alumno, Curso curso,
                       LocalDateTime fechaInscripcion,
                       EstadoInscripcion estado,
                       String observaciones) {
        this.id = id;
        this.alumno = alumno;
        this.curso = curso;
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

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public LocalDateTime getFechaInscripcion() {
		return fechaInscripcion;
	}

	public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}

	public EstadoInscripcion getEstado() {
		return estado;
	}

	public void setEstado(EstadoInscripcion estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	

}
