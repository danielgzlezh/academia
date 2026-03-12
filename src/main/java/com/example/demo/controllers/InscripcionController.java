package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.InscripcionCreateDto;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.InscripcionesService;

@Controller
public class InscripcionController {

    private final InscripcionesService inscripcionService;

    public InscripcionController(InscripcionesService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @GetMapping("/inscripciones/list")
    public String listar(Model model) {
        model.addAttribute("inscripciones", inscripcionService.listarTodasDTO());
        return "inscripciones-list";
    }
    
    @GetMapping("/inscripciones/new")
    public String formulario(Model model) throws NotFoundException {
        model.addAttribute("alumnos", inscripcionService.alumnosActivos());
        model.addAttribute("cursos", inscripcionService.cursosActivosConPlazas());
        return "inscripciones-form";
    }

    @GetMapping("/inscripciones/cancelar/{id}")
    public String cancelar(@PathVariable Long id) throws NotFoundException {
        inscripcionService.cancelar(id);
        return "redirect:/inscripciones/list";
    }
    @GetMapping("/inscripciones/estado")
    public String porEstado(@RequestParam String estado, Model model) {
        model.addAttribute("inscripciones", inscripcionService.listarPorEstado(estado));
        return "inscripciones-list";
    }
    @PostMapping("/inscripciones/save")
    public String guardar(InscripcionCreateDto dto) throws BusinessException, NotFoundException {
        inscripcionService.inscribir(dto.getAlumnoId(), dto.getCursoId(), dto.getObservaciones());
        return "redirect:/inscripciones/list";
    }
}
