package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CursoDto;
import com.example.demo.entitys.Curso;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.CursoRepository;

@Service
public class CursoService {

	private static final Logger log = LoggerFactory.getLogger(CursoService.class);

	@Autowired
	private CursoRepository repo;

	public List<CursoDto> listarCursos() {

		log.info("Listando todos los cursos");

		List<Curso> lista = repo.findAll();
		List<CursoDto> listaDTO = new ArrayList<>();

		for (int i = 0; i < lista.size(); i++) {
			Curso c = lista.get(i);
			listaDTO.add(convertirDTO(c));
		}

		return listaDTO;
	}

	public CursoDto crearCurso(CursoDto dto) {

		log.info("Creando curso {}", dto.getNombre());

		Curso entidad = new Curso();

		entidad.setNombre(dto.getNombre());
		entidad.setCategoria(dto.getCategoria());
		entidad.setNivel(dto.getNivel());
		entidad.setFechainicio(dto.getFechaInicio());
		entidad.setFechafin(dto.getFechaFin());
		entidad.setPlazas(dto.getPlazas());
		entidad.setActivo(dto.isActivo());

		Curso guardado = repo.save(entidad);

		log.info("Curso creado con id {}", guardado.getId());

		return convertirDTO(guardado);
	}

	public CursoDto editarCurso(Long id, CursoDto dto) throws NotFoundException {

		log.info("Editando curso con id {}", id);

		Optional<Curso> opt = repo.findById(id);

		if (opt.isEmpty()) {
			log.error("Curso no encontrado con id {}", id);
			throw new NotFoundException();
		}

		Curso c = opt.get();

		c.setNombre(dto.getNombre());
		c.setCategoria(dto.getCategoria());
		c.setNivel(dto.getNivel());
		c.setFechainicio(dto.getFechaInicio());
		c.setFechafin(dto.getFechaFin());
		c.setPlazas(dto.getPlazas());
		c.setActivo(dto.isActivo());

		Curso guardado = repo.save(c);

		log.info("Curso actualizado con id {}", guardado.getId());

		return convertirDTO(guardado);
	}

	public CursoDto obtenerPorId(Long id) throws NotFoundException {

		log.info("Buscando curso con id {}", id);

		Optional<Curso> opt = repo.findById(id);

		if (opt.isEmpty()) {
			log.error("Curso no encontrado con id {}", id);
			throw new NotFoundException();
		}

		return convertirDTO(opt.get());
	}

	public void eliminarCurso(Long id) throws NotFoundException {

		log.info("Eliminando curso con id {}", id);

		Optional<Curso> opt = repo.findById(id);

		if (opt.isEmpty()) {
			log.error("No se puede eliminar, curso no encontrado {}", id);
			throw new NotFoundException();
		}

		repo.delete(opt.get());

		log.info("Curso eliminado con id {}", id);
	}

	private CursoDto convertirDTO(Curso c) {

		CursoDto dto = new CursoDto();

		dto.setId(c.getId());
		dto.setNombre(c.getNombre());
		dto.setCategoria(c.getCategoria());
		dto.setNivel(c.getNivel());
		dto.setFechaInicio(c.getFechaInicio());
		dto.setFechaFin(c.getFechafin());
		dto.setPlazas(c.getPlazas());
		dto.setActivo(c.isActivo());

		return dto;
	}

	public List<CursoDto> listarPorCategoriaYNivel(String categoria, String nivel) {

		log.info("Buscando cursos por categoria {} y nivel {}", categoria, nivel);

		List<Curso> listaEntidades = repo.findByCategoriaIgnoreCaseAndNivelIgnoreCase(categoria, nivel);
		List<CursoDto> listaDTO = new ArrayList<CursoDto>();

		for (int i = 0; i < listaEntidades.size(); i++) {

			Curso c = listaEntidades.get(i);

			CursoDto dto = new CursoDto();
			dto.setId(c.getId());
			dto.setNombre(c.getNombre());
			dto.setCategoria(c.getCategoria());
			dto.setNivel(c.getNivel());
			dto.setFechaInicio(c.getFechaInicio());
			dto.setFechaFin(c.getFechafin());
			dto.setPlazas(c.getPlazas());
			dto.setActivo(c.isActivo());

			listaDTO.add(dto);
		}

		if (listaDTO.isEmpty()) {
			log.warn("No se encontraron cursos para categoria {} y nivel {}", categoria, nivel);
		}

		return listaDTO;
	}

	public List<CursoDto> topCursosMasInscritos() {

		log.info("Obteniendo top cursos con mas inscritos");

		List<Object[]> resultados = repo.topCursosMasInscritos();
		List<CursoDto> listaDTO = new ArrayList<CursoDto>();

		for (int i = 0; i < resultados.size(); i++) {

			Curso c = (Curso) resultados.get(i)[0];

			CursoDto dto = new CursoDto();
			dto.setId(c.getId());
			dto.setNombre(c.getNombre());
			dto.setCategoria(c.getCategoria());
			dto.setNivel(c.getNivel());
			dto.setFechaInicio(c.getFechaInicio());
			dto.setFechaFin(c.getFechafin());
			dto.setPlazas(c.getPlazas());
			dto.setActivo(c.isActivo());

			listaDTO.add(dto);
		}

		if (listaDTO.isEmpty()) {
			log.warn("No hay cursos con inscripciones registradas");
		}

		return listaDTO;
	}
}