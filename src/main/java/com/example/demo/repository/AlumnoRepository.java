package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entitys.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
	List<Alumno> findByNombreContainingIgnoreCase(String nombre);

	Alumno findByEmailIgnoreCase(String email);

}
