package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.BusinessException;
import com.example.demo.dto.AlumnoCreateDto;
import com.example.demo.dto.AlumnoDetailDto;
import com.example.demo.dto.AlumnoListDto;
import com.example.demo.dto.AlumnoUpdateDto;
import com.example.demo.entitys.Alumno;
import com.example.demo.entitys.Inscripcion;
import com.example.demo.repository.AlumnoRepository;
import com.example.demo.repository.InscripcionRepository;

@Service
public class AlumnoService {

    private static final Logger logger = LoggerFactory.getLogger(AlumnoService.class);

    private final AlumnoRepository alumnoRepository;
    private final InscripcionRepository inscripcionRepository;

    public AlumnoService(AlumnoRepository alumnoRepository, InscripcionRepository inscripcionRepository) {
        this.alumnoRepository = alumnoRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    public List<Alumno> listarTodos() {

        List<Alumno> alumnos = alumnoRepository.findAll();
        logger.info("Listado de alumnos realizado correctamente. Total {}", alumnos.size());

        return alumnos;
    }

    public Alumno crear(Alumno alumno) {


        alumno.setFechaAlta(LocalDate.now());
        alumno.setActivo(true);

        Alumno guardado = alumnoRepository.save(alumno);

        logger.info("Alumno creado correctamente con id {}", guardado.getId());

        return guardado;
    }

    public Alumno buscarPorId(Long id) throws NotFoundException {

        Alumno alumno = alumnoRepository.findById(id).orElse(null);

        if (alumno == null) {
            logger.error("Alumno no encontrado con id {}", id);
            throw new NotFoundException();
        }

        logger.info("Alumno encontrado correctamente id {}", id);

        return alumno;
    }

    public void desactivar(Long id) throws NotFoundException, BusinessException {

        Alumno alumno = alumnoRepository.findById(id).orElse(null);

        if (alumno == null) {
            logger.error("Alumno no encontrado al intentar desactivar id {}", id);
            throw new NotFoundException();
        }

        if (!alumno.isActivo()) {
            logger.warn("Intento de desactivar alumno ya inactivo id {}", id);
            throw new BusinessException();
        }

        alumno.setActivo(false);
        alumnoRepository.save(alumno);

        logger.info("Alumno desactivado correctamente id {}", id);
    }

    public void actualizar(Long id, Alumno datosFormulario) throws NotFoundException {

        Alumno alumnoBD = alumnoRepository.findById(id).orElse(null);

        if (alumnoBD == null) {
            logger.error("Alumno no encontrado al actualizar id {}", id);
            throw new NotFoundException();
        }

        alumnoBD.setNombre(datosFormulario.getNombre());
        alumnoBD.setApellidos(datosFormulario.getApellidos());
        alumnoBD.setEmail(datosFormulario.getEmail());
        alumnoBD.setTelefono(datosFormulario.getTelefono());

        alumnoRepository.save(alumnoBD);

        logger.info("Alumno actualizado correctamente id {}", id);
    }

    public List<Alumno> buscarPorNombre(String nombre) {

        List<Alumno> alumnos = alumnoRepository.findByNombreContainingIgnoreCase(nombre);

        if (alumnos.isEmpty()) {
            logger.warn("Búsqueda por nombre sin resultados {}", nombre);
        } else {
            logger.info("Búsqueda por nombre realizada correctamente. Resultados {}", alumnos.size());
        }

        return alumnos;
    }

    public List<Inscripcion> inscripcionesActivas(Long alumnoId) throws NotFoundException {

        Alumno alumno = alumnoRepository.findById(alumnoId).orElse(null);

        if (alumno == null) {
            logger.error("Alumno no encontrado al consultar inscripciones id {}", alumnoId);
            throw new NotFoundException();
        }

        List<Inscripcion> lista = inscripcionRepository.findByAlumnoIdAndEstado(alumnoId, "ACTIVA");

        logger.info("Consulta de inscripciones activas realizada para alumno {} total {}", alumnoId, lista.size());

        return lista;
    }

    public void crearDesdeDTO(AlumnoCreateDto dto) {


        Alumno a = new Alumno();
        a.setNombre(dto.getNombre());
        a.setApellidos(dto.getApellidos());
        a.setEmail(dto.getEmail());
        a.setActivo(dto.isActivo());
        a.setFechaAlta(LocalDate.now());

        alumnoRepository.save(a);

        logger.info("Alumno creado desde DTO correctamente email {}", dto.getEmail());
    }

    public void actualizarDesdeDTO(Long id, AlumnoUpdateDto dto) throws NotFoundException {

        Alumno a = alumnoRepository.findById(id).orElse(null);

        if (a == null) {
            logger.error("Alumno no encontrado al actualizar DTO id {}", id);
            throw new NotFoundException();
        }

        a.setNombre(dto.getNombre());
        a.setApellidos(dto.getApellidos());
        a.setEmail(dto.getEmail());
        a.setTelefono(dto.getTelefono());
        a.setActivo(dto.isActivo());

        alumnoRepository.save(a);

        logger.info("Alumno actualizado desde DTO correctamente id {}", id);
    }

    public List<AlumnoListDto> listarTodosDTO() {

        List<Alumno> alumnos = alumnoRepository.findAll();
        List<AlumnoListDto> listaDTO = new ArrayList<>();

        for (int i = 0; i < alumnos.size(); i++) {

            Alumno a = alumnos.get(i);

            AlumnoListDto dto = new AlumnoListDto();
            dto.setId(a.getId());
            dto.setNombre(a.getNombre());
            dto.setApellidos(a.getApellidos());
            dto.setEmail(a.getEmail());
            dto.setActivo(a.isActivo());

            listaDTO.add(dto);
        }

        logger.info("Listado DTO generado correctamente total {}", listaDTO.size());

        return listaDTO;
    }

    public AlumnoDetailDto detalleDTO(Long id) throws NotFoundException {

        Alumno a = alumnoRepository.findById(id).orElse(null);

        if (a == null) {
            logger.error("Alumno no encontrado en detalle id {}", id);
            throw new NotFoundException();
        }

        AlumnoDetailDto dto = new AlumnoDetailDto();
        dto.setId(a.getId());
        dto.setNombre(a.getNombre());
        dto.setApellidos(a.getApellidos());
        dto.setEmail(a.getEmail());
        dto.setTelefono(a.getTelefono());
        dto.setFechaAlta(a.getFechaAlta().toString());
        dto.setActivo(a.isActivo());

        logger.info("Detalle de alumno cargado correctamente id {}", id);

        return dto;
    }
}