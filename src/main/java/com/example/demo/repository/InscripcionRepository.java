package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entitys.EstadoInscripcion;
import com.example.demo.entitys.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
	long countByCursoIdAndEstado(Long cursoId, EstadoInscripcion estado);

	List<Inscripcion> findByAlumnoIdAndEstado(Long alumnoId, EstadoInscripcion estado);

	Inscripcion findByAlumnoIdAndCursoIdAndEstado(Long alumnoId, Long cursoId, EstadoInscripcion estado);

}
