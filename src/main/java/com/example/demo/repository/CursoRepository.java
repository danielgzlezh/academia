package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entitys.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
	List<Curso> findByCategoriaIgnoreCaseAndNivelIgnoreCase(String categoria, String nivel);

	@Query("SELECT i.curso, COUNT(i) as total " + "FROM Inscripcion i " + "WHERE i.estado = 'ACTIVA' "
			+ "GROUP BY i.curso " + "ORDER BY total DESC")
	List<Object[]> topCursosMasInscritos();
}
