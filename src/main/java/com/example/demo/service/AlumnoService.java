package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AlumnoCreateDto;
import com.example.demo.dto.AlumnoDetailDto;
import com.example.demo.dto.AlumnoDto;
import com.example.demo.dto.AlumnoListDto;
import com.example.demo.entitys.Alumno;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AlumnoRepository;

@Service
public class AlumnoService {

	private static final Logger log = LoggerFactory.getLogger(AlumnoService.class);

	@Autowired
	private AlumnoRepository repo;

	public List<AlumnoListDto> listarAlumno() {

	    List<Alumno> lista = repo.findAll();
	    List<AlumnoListDto> listaDTO = new ArrayList<>();

	    for (int i = 0; i < lista.size(); i++) {
	        Alumno a = lista.get(i);

	        AlumnoListDto dto = new AlumnoListDto();
	        dto.setId(a.getId());
	        dto.setNombre(a.getNombre());
	        dto.setEmail(a.getEmail());

	        listaDTO.add(dto);
	    }

	    return listaDTO;
	}

	public AlumnoDetailDto crearAlumno(AlumnoCreateDto dto) {

	    Alumno entidad = new Alumno();

	    entidad.setNombre(dto.getNombre());
	    entidad.setEmail(dto.getEmail());

	    Alumno guardado = repo.save(entidad);

	    AlumnoDetailDto detalle = new AlumnoDetailDto();
	    detalle.setId(guardado.getId());
	    detalle.setNombre(guardado.getNombre());
	    detalle.setEmail(guardado.getEmail());

	    return detalle;
	}

	public AlumnoDetailDto editarAlumno(Long id, AlumnoUpdateDto dto) throws NotFoundException {

	    Alumno a = repo.findById(id).orElse(null);
	    if (a == null) {
	        throw new NotFoundException();
	    }

	    a.setNombre(dto.getNombre());
	    a.setApellidos(dto.getApellidos());
	    a.setEmail(dto.getEmail());
	    a.setTelefono(dto.getTelefono());
	    a.setFechaAlta(dto.getFechaAlta());
	    a.setActivo(dto.isActivo());

	    Alumno guardado = repo.save(a);

	    return convertirADetailDto(guardado);
	}

	public AlumnoDetailDto obtenerPorId(Long id) throws NotFoundException {
	    Alumno a = repo.findById(id).orElse(null);
	    if(a == null) throw new NotFoundException();

	    AlumnoDetailDto dto = new AlumnoDetailDto();
	    dto.setId(a.getId());
	    dto.setNombre(a.getNombre());
	    dto.setEmail(a.getEmail());

	    return dto;
	}

	public void eliminarAlumno(Long id) throws NotFoundException {

		log.info("Eliminando alumno con id {}", id);

		Optional<Alumno> opt = repo.findById(id);

		if (opt.isEmpty()) {
			log.error("No se puede eliminar, alumno no encontrado {}", id);
			throw new NotFoundException();
		}

		repo.delete(opt.get());

		log.info("Alumno eliminado con id {}", id);
	}

	private AlumnoDto convertirDTO(Alumno a) {

		AlumnoDto dto = new AlumnoDto();

		dto.setId(a.getId());
		dto.setNombre(a.getNombre());
		dto.setApellidos(a.getApellidos());
		dto.setEmail(a.getEmail());
		dto.setTelefono(a.getTelefono());
		dto.setFechaAlta(a.getFechaAlta());
		dto.setActivo(a.isActivo());

		return dto;
	}

    public List<AlumnoListDto> buscarPorNombre(String nombre) {
        log.info("Buscando alumnos por nombre {}", nombre);

        List<Alumno> lista = repo.findByNombreContainingIgnoreCase(nombre);
        List<AlumnoListDto> listaDTO = new ArrayList<>();

        for (int i = 0; i < lista.size(); i++) {
            Alumno a = lista.get(i);

            AlumnoListDto dto = new AlumnoListDto();
            dto.setId(a.getId());
            dto.setNombre(a.getNombre());
            dto.setEmail(a.getEmail());

            listaDTO.add(dto);
        }

        if (listaDTO.isEmpty()) {
            log.warn("No se encontraron alumnos con nombre {}", nombre);
        }

        return listaDTO;
    }

	public AlumnoDto buscarPorEmail(String email) throws NotFoundException {

		log.info("Buscando alumno por email {}", email);

		Alumno a = repo.findByEmailIgnoreCase(email);

		if (a == null) {
			log.error("Alumno no encontrado con email {}", email);
			throw new NotFoundException();
		}

		AlumnoDto dto = new AlumnoDto();
		dto.setId(a.getId());
		dto.setNombre(a.getNombre());
		dto.setApellidos(a.getApellidos());
		dto.setEmail(a.getEmail());
		dto.setTelefono(a.getTelefono());
		dto.setFechaAlta(a.getFechaAlta());
		dto.setActivo(a.isActivo());

		return dto;
	}
}