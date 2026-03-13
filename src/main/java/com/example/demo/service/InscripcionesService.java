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
import com.example.demo.dto.InscripcionListDto;
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

	private static final Logger logger = LoggerFactory.getLogger(InscripcionesService.class);

	private InscripcionRepository inscripcionRepository;
	private AlumnoRepository alumnoRepository;
	private CursoRepository cursoRepository;

	public InscripcionesService(InscripcionRepository inscripcionRepository, AlumnoRepository alumnoRepository,
			CursoRepository cursoRepository) {
		this.inscripcionRepository = inscripcionRepository;
		this.alumnoRepository = alumnoRepository;
		this.cursoRepository = cursoRepository;
	}

	public List<Inscripcion> listarTodas() {

		List<Inscripcion> lista = inscripcionRepository.findAll();

		logger.info("Listado de inscripciones realizado correctamente. Total {}", lista.size());

		return lista;
	}

	public List<Alumno> alumnosActivos() {

		List<Alumno> alumnos = alumnoRepository.findByActivoTrue();

		logger.info("Listado de alumnos activos realizado correctamente. Total {}", alumnos.size());

		return alumnos;
	}

	public List<Curso> cursosActivosConPlazas() throws NotFoundException {

		List<Curso> activos = cursoRepository.findByActivoTrue();
		List<Curso> resultado = new ArrayList<>();

		for (int i = 0; i < activos.size(); i++) {

			Curso c = activos.get(i);

			if (plazasRestantes(c.getId()) > 0) {
				resultado.add(c);
			}
		}

		logger.info("Listado de cursos activos con plazas disponible total {}", resultado.size());

		return resultado;
	}

	public long plazasRestantes(Long cursoId) throws NotFoundException {

		Curso curso = cursoRepository.findById(cursoId).orElse(null);

		if (curso == null) {
			logger.error("Curso no encontrado al calcular plazas restantes id {}", cursoId);
			throw new NotFoundException();
		}

		long ocupadas = inscripcionRepository.countByCursoIdAndEstado(cursoId, EstadoInscripcion.ACTIVA);

		long restantes = curso.getPlazas() - ocupadas;

		logger.info("Plazas restantes calculadas correctamente curso {} restantes {}", cursoId, restantes);

		return restantes;
	}

	public void inscribir(Long alumnoId, Long cursoId, String observaciones) throws BusinessException, NotFoundException {

		Alumno alumno = alumnoRepository.findById(alumnoId).orElse(null);
		Curso curso = cursoRepository.findById(cursoId).orElse(null);

		if (alumno == null) {
			logger.error("Alumno no encontrado id {}", alumnoId);
			throw new NotFoundException();
		}

		if (curso == null) {
			logger.error("Curso no encontrado id {}", cursoId);
			throw new NotFoundException();
		}

		if (!alumno.isActivo() || !curso.isActivo()) {
			logger.warn("Alumno o curso inactivo alumnoId {} cursoId {}", alumnoId, cursoId);
			throw new BusinessException();
		}

		boolean duplicada = inscripcionRepository.existsByAlumnoIdAndCursoIdAndEstado(alumnoId, cursoId, EstadoInscripcion.ACTIVA);

		if (duplicada) {
			logger.warn("Intento de inscripción duplicada alumnoId {} cursoId {}", alumnoId, cursoId);
			throw new BusinessException();
		}

		long restantes = plazasRestantes(cursoId);

		if (restantes <= 0) {
			logger.warn("Intento de inscripción en curso sin plazas cursoId {}", cursoId);
			throw new BusinessException();
		}

		Inscripcion i = new Inscripcion();

		i.setAlumno(alumno);
		i.setCurso(curso);
		i.setFechaInscripcion(LocalDateTime.now());
		i.setEstado(EstadoInscripcion.ACTIVA);
		i.setObservaciones(observaciones);

		inscripcionRepository.save(i);

		logger.info("Inscripción creada correctamente alumnoId {} cursoId {}", alumnoId, cursoId);
	}

	public void cancelar(Long inscripcionId) throws NotFoundException {

		Inscripcion ins = inscripcionRepository.findById(inscripcionId).orElse(null);

		if (ins == null) {
			logger.error("Inscripción no encontrada id {}", inscripcionId);
			throw new NotFoundException();
		}

		ins.setEstado(EstadoInscripcion.CANCELADA);

		inscripcionRepository.save(ins);

		logger.info("Inscripción cancelada correctamente id {}", inscripcionId);
	}

	public List<Inscripcion> listarPorEstado(EstadoInscripcion estado) {

		List<Inscripcion> lista = inscripcionRepository.findByEstado(estado);

		logger.info("Listado de inscripciones por estado {} total {}", estado, lista.size());

		return lista;
	}

	public List<InscripcionListDto> listarTodasDTO() {

		List<Inscripcion> inscripciones = inscripcionRepository.findAll();
		List<InscripcionListDto> listaDTO = new ArrayList<>();

		for (int i = 0; i < inscripciones.size(); i++) {

			Inscripcion ins = inscripciones.get(i);

			InscripcionListDto dto = new InscripcionListDto();

			String nombreCompleto = ins.getAlumno().getNombre() + " " + ins.getAlumno().getApellidos();

			dto.setId(ins.getId());
			dto.setAlumnoNombreCompleto(nombreCompleto);
			dto.setCursoNombre(ins.getCurso().getNombre());
			dto.setFechaInscripcion(ins.getFechaInscripcion());
			dto.setEstado(ins.getEstado().name());
			dto.setObservaciones(ins.getObservaciones());

			listaDTO.add(dto);
		}

		logger.info("Listado DTO de inscripciones generado correctamente total {}", listaDTO.size());

		return listaDTO;
	}
}