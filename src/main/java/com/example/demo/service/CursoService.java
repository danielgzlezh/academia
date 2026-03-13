package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CursoCreateDto;
import com.example.demo.dto.CursoDetailDto;
import com.example.demo.dto.CursoListDto;
import com.example.demo.dto.CursoUpdateDto;
import com.example.demo.entitys.Curso;
import com.example.demo.entitys.EstadoInscripcion;
import com.example.demo.entitys.Inscripcion;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.InscripcionRepository;

@Service
public class CursoService {

    private static final Logger logger = LoggerFactory.getLogger(CursoService.class);

    private final CursoRepository cursoRepository;
    private final InscripcionRepository inscripcionRepository;

    public CursoService(CursoRepository cursoRepository, InscripcionRepository inscripcionRepository) {
        this.cursoRepository = cursoRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    public List<Curso> listarTodos() {
        List<Curso> cursos = cursoRepository.findAll();
        logger.info("Listado de cursos realizado correctamente. Total " + cursos.size());
        return cursos;
    }

    public Curso buscarPorId(Long id) throws NotFoundException {
        Curso curso = cursoRepository.findById(id).orElse(null);
        if (curso == null) throw new NotFoundException();
        logger.info("Curso encontrado correctamente id " + id);
        return curso;
    }

    public Curso crear(Curso curso) {
        curso.setActivo(true);
        Curso guardado = cursoRepository.save(curso);
        logger.info("Curso creado correctamente con id " + guardado.getId());
        return guardado;
    }

    public void actualizar(Long id, Curso datosFormulario) throws NotFoundException {
        Curso cursoBD = cursoRepository.findById(id).orElse(null);
        if (cursoBD == null) throw new NotFoundException();

        cursoBD.setNombre(datosFormulario.getNombre());
        cursoBD.setCategoria(datosFormulario.getCategoria());
        cursoBD.setNivel(datosFormulario.getNivel());
        cursoBD.setFechainicio(datosFormulario.getFechaInicio());
        cursoBD.setFechafin(datosFormulario.getFechafin());
        cursoBD.setPlazas(datosFormulario.getPlazas());

        cursoRepository.save(cursoBD);
        logger.info("Curso actualizado correctamente id " + id);
    }

    public void desactivar(Long id) throws NotFoundException {
        Curso cursoBD = cursoRepository.findById(id).orElse(null);
        if (cursoBD == null) throw new NotFoundException();

        cursoBD.setActivo(false);
        cursoRepository.save(cursoBD);
        logger.info("Curso desactivado correctamente id " + id);
    }

    public long plazasOcupadas(Long cursoId) throws NotFoundException {
        Curso curso = cursoRepository.findById(cursoId).orElse(null);
        if (curso == null) throw new NotFoundException();

        long ocupadas = inscripcionRepository.countByCursoIdAndEstado(cursoId, EstadoInscripcion.ACTIVA);
        logger.info("Plazas ocupadas calculadas correctamente para curso " + cursoId + " total " + ocupadas);
        return ocupadas;
    }

    public List<Curso> buscarPorCategoria(String categoria) {
        List<Curso> cursos = cursoRepository.findByCategoriaContainingIgnoreCase(categoria);
        logger.info("Búsqueda por categoría realizada correctamente resultados " + cursos.size());
        return cursos;
    }

    public void actualizarDesdeDTO(Long id, CursoUpdateDto dto) throws NotFoundException {
        Curso c = cursoRepository.findById(id).orElse(null);
        if (c == null) throw new NotFoundException();

        c.setNombre(dto.getNombre());
        c.setCategoria(dto.getCategoria());
        c.setNivel(dto.getNivel());
        c.setFechainicio(dto.getFechaInicio());
        c.setFechafin(dto.getFechaFin());
        c.setPlazas(dto.getPlazas());
        c.setActivo(dto.isActivo());

        cursoRepository.save(c);
        logger.info("Curso actualizado desde DTO correctamente id " + id);
    }

    public void crearDesdeDTO(CursoCreateDto dto) {
        Curso c = new Curso();

        c.setNombre(dto.getNombre());
        c.setCategoria(dto.getCategoria());
        c.setNivel(dto.getNivel());
        c.setFechainicio(dto.getFechaInicio());
        c.setFechafin(dto.getFechaFin());
        c.setPlazas(dto.getPlazas());
        c.setActivo(dto.isActivo());

        cursoRepository.save(c);
        logger.info("Curso creado desde DTO correctamente nombre " + dto.getNombre());
    }

    public List<CursoListDto> listarTodosDTO() {
        List<Curso> cursos = cursoRepository.findAll();
        List<CursoListDto> listaDTO = new ArrayList<>();

        for (Curso c : cursos) {
            CursoListDto dto = new CursoListDto();

            dto.setId(c.getId());
            dto.setNombre(c.getNombre());
            dto.setCategoria(c.getCategoria());
            dto.setNivel(c.getNivel());

            if (c.getFechaInicio() != null) {
                dto.setFechaInicio(c.getFechaInicio().toString());
            } else {
                dto.setFechaInicio("");
            }

            if (c.getFechafin() != null) {
                dto.setFechaFin(c.getFechafin().toString());
            } else {
                dto.setFechaFin("");
            }

            dto.setPlazas(c.getPlazas());
            dto.setActivo(c.isActivo());

            listaDTO.add(dto);
        }

        logger.info("Listado DTO de cursos generado correctamente total " + listaDTO.size());
        return listaDTO;
    }

    public CursoDetailDto detalleDTO(Long id) throws NotFoundException {
        Curso c = cursoRepository.findById(id).orElse(null);
        if (c == null) throw new NotFoundException();

        long plazasOcupadas = inscripcionRepository.countByCursoIdAndEstado(id, EstadoInscripcion.ACTIVA);
        long plazasRestantes = c.getPlazas() - plazasOcupadas;

        List<Inscripcion> inscripciones = inscripcionRepository.findByCursoIdAndEstado(id, EstadoInscripcion.ACTIVA);
        List<String> alumnosInscritos = new ArrayList<>();
        for (Inscripcion ins : inscripciones) {
            String nombreAlumno = ins.getAlumno().getNombre() + " " + ins.getAlumno().getApellidos();
            alumnosInscritos.add(nombreAlumno);
        }

        CursoDetailDto dto = new CursoDetailDto();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setCategoria(c.getCategoria());
        dto.setNivel(c.getNivel());

        if (c.getFechaInicio() != null) dto.setFechaInicio(c.getFechaInicio().toString());
        else dto.setFechaInicio("");

        if (c.getFechafin() != null) dto.setFechaFin(c.getFechafin().toString());
        else dto.setFechaFin("");

        dto.setPlazas(c.getPlazas());
        dto.setActivo(c.isActivo());
        dto.setPlazasOcupadas((int) plazasOcupadas);
        dto.setPlazasRestantes((int) plazasRestantes);
        dto.setAlumnosInscritos(alumnosInscritos);

        logger.info("Detalle de curso cargado correctamente id " + id);
        return dto;
    }
}