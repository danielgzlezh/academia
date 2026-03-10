package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.InscripcionDto;
import com.example.demo.entitys.Alumno;
import com.example.demo.entitys.Curso;
import com.example.demo.entitys.EstadoInscripcion;
import com.example.demo.entitys.Inscripcion;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AlumnoRepository;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.InscripcionRepository;

@Service
public class InscripcionesService {

	private static final Logger log = LoggerFactory.getLogger(InscripcionesService.class);

	@Autowired
	private InscripcionRepository repo;

	@Autowired
	private AlumnoRepository alumnoRepo;

	@Autowired
	private CursoRepository cursoRepo;

	public InscripcionDto inscribirAlumno(InscripcionDto dto) throws BusinessException, NotFoundException {

		log.info("Inscribiendo alumno {} en curso {}", dto.getAlumnoId(), dto.getCursoId());

		Optional<Alumno> optAlumno = alumnoRepo.findById(dto.getAlumnoId());

		if (optAlumno.isEmpty()) {
			log.error("Alumno no encontrado {}", dto.getAlumnoId());
			throw new NotFoundException();
		}

		Optional<Curso> optCurso = cursoRepo.findById(dto.getCursoId());

		if (optCurso.isEmpty()) {
			log.error("Curso no encontrado {}", dto.getCursoId());
			throw new NotFoundException();
		}

		Alumno alumno = optAlumno.get();
		Curso curso = optCurso.get();

		if (!curso.isActivo()) {
			log.warn("Intento de inscripción en curso inactivo {}", curso.getId());
			throw new BusinessException();
		}

		long inscripcionesActivas = repo.countByCursoIdAndEstado(curso.getId(), EstadoInscripcion.ACTIVA);

		if (inscripcionesActivas >= curso.getPlazas()) {
			log.warn("No hay plazas disponibles en el curso {}", curso.getId());
			throw new BusinessException();
		}

		Inscripcion existente = repo.findByAlumnoIdAndCursoIdAndEstado(alumno.getId(), curso.getId(),
				EstadoInscripcion.ACTIVA);

		if (existente != null) {
			log.warn("El alumno {} ya está inscrito en el curso {}", alumno.getId(), curso.getId());
			throw new BusinessException();
		}

		Inscripcion ins = new Inscripcion();

		ins.setAlumno(alumno);
		ins.setCurso(curso);
		ins.setFechaInscripcion(LocalDateTime.now());
		ins.setEstado(EstadoInscripcion.ACTIVA);
		ins.setObservaciones(dto.getObservaciones());

		Inscripcion guardada = repo.save(ins);

		log.info("Inscripción creada con id {}", guardada.getId());

		return convertirDTO(guardada);
	}

	public List<InscripcionDto> listarInscripciones() {

		log.info("Listando todas las inscripciones");

		List<Inscripcion> lista = repo.findAll();
		List<InscripcionDto> listaDTO = new ArrayList<>();

		for (int i = 0; i < lista.size(); i++) {

			Inscripcion ins = lista.get(i);
			listaDTO.add(convertirDTO(ins));
		}

		return listaDTO;
	}

	public InscripcionDto cancelarInscripcion(Long id) throws NotFoundException {

		log.info("Cancelando inscripción {}", id);

		Optional<Inscripcion> opt = repo.findById(id);

		if (opt.isEmpty()) {
			log.error("Inscripción no encontrada {}", id);
			throw new NotFoundException();
		}

		Inscripcion ins = opt.get();

		ins.setEstado(EstadoInscripcion.CANCELADA);

		Inscripcion guardada = repo.save(ins);

		log.info("Inscripción cancelada {}", id);

		return convertirDTO(guardada);
	}

	private InscripcionDto convertirDTO(Inscripcion ins) {

		InscripcionDto dto = new InscripcionDto();

		dto.setId(ins.getId());
		dto.setAlumnoId(ins.getAlumno().getId());
		dto.setCursoId(ins.getCurso().getId());
		dto.setFechaInscripcion(ins.getFechaInscripcion());
		dto.setEstado(ins.getEstado().name());
		dto.setObservaciones(ins.getObservaciones());

		return dto;
	}

	public long contarInscripcionesActivas(Long cursoId) {

		log.info("Contando inscripciones activas del curso {}", cursoId);

		return repo.countByCursoIdAndEstado(cursoId, EstadoInscripcion.ACTIVA);
	}

	public List<InscripcionDto> listarInscripcionesActivas(Long alumnoId) {

		log.info("Listando inscripciones activas del alumno {}", alumnoId);

		List<Inscripcion> listaEntidades = repo.findByAlumnoIdAndEstado(alumnoId, EstadoInscripcion.ACTIVA);
		List<InscripcionDto> listaDTO = new ArrayList<InscripcionDto>();

		for (int i = 0; i < listaEntidades.size(); i++) {

			Inscripcion ins = listaEntidades.get(i);

			InscripcionDto dto = new InscripcionDto();
			dto.setId(ins.getId());
			dto.setAlumnoId(ins.getAlumno().getId());
			dto.setCursoId(ins.getCurso().getId());
			dto.setFechaInscripcion(ins.getFechaInscripcion());
			dto.setEstado(ins.getEstado().name());
			dto.setObservaciones(ins.getObservaciones());

			listaDTO.add(dto);
		}

		if (listaDTO.isEmpty()) {
			log.warn("El alumno {} no tiene inscripciones activas", alumnoId);
		}

		return listaDTO;
	}
}