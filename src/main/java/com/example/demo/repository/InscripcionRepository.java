package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entitys.EstadoInscripcion;
import com.example.demo.entitys.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    
    boolean existsByAlumnoIdAndCursoIdAndEstado(Long alumnoId, Long cursoId, EstadoInscripcion activa);

  
    long countByCursoIdAndEstado(Long cursoId, EstadoInscripcion activa);


    List<Inscripcion> findByAlumnoIdAndEstado(Long alumnoId, EstadoInscripcion estado);
    List<Inscripcion> findByCursoIdAndEstado(Long cursoId, EstadoInscripcion activa);
    List<Inscripcion> findByEstado(EstadoInscripcion estado);

}
