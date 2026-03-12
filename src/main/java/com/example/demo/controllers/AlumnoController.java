package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.AlumnoCreateDto;
import com.example.demo.dto.AlumnoUpdateDto;
import com.example.demo.entitys.Alumno;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.AlumnoService;

@Controller
public class AlumnoController {

    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    @GetMapping("/alumnos/list")
    public String listar(Model model) {
        model.addAttribute("alumnos", alumnoService.listarTodosDTO());
        return "alumnolist";
    }
    @GetMapping("/alumnos/new")
    public String mostrarFormulario(Model model) {
        model.addAttribute("alumno", new Alumno());
        return "alumnosform";
    }
    @PostMapping("/alumnos/save")
    public String guardar(AlumnoCreateDto dto) {
        alumnoService.crearDesdeDTO(dto);
        return "redirect:/alumnos/list";
    }
    @GetMapping("/alumnos/desactivar/{id}")
    public String desactivar(@PathVariable Long id) throws NotFoundException, BusinessException {
        alumnoService.desactivar(id);
        return "redirect:/alumnos/list";
    }
    @GetMapping("/alumnos/edit/{id}")
    public String editar(@PathVariable Long id, Model model) throws NotFoundException {
        Alumno alumno = alumnoService.buscarPorId(id);
        model.addAttribute("alumno", alumno);
        return "alumnosform";
    }
    @PostMapping("/alumnos/update/{id}")
    public String actualizar(@PathVariable Long id, AlumnoUpdateDto dto) throws NotFoundException {
        alumnoService.actualizarDesdeDTO(id, dto);
        return "redirect:/alumnos/list";
    }
    @GetMapping("/alumnos/buscar")
    public String buscar(@RequestParam String nombre, Model model) {
        model.addAttribute("alumnos", alumnoService.buscarPorNombre(nombre));
        return "alumnoslist";
    }
    @GetMapping("/alumnos/detail/{id}")
    public String detalle(@PathVariable Long id, Model model) throws NotFoundException {

        model.addAttribute("alumno", alumnoService.detalleDTO(id));
        model.addAttribute("inscripciones", alumnoService.inscripcionesActivas(id));

        return "alumnosdetail";
    }
}