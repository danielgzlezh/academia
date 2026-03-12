package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.CursoCreateDto;
import com.example.demo.dto.CursoUpdateDto;
import com.example.demo.entitys.Curso;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.CursoService;

@Controller
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping("/cursos/list")
    public String listar(Model model) {
        model.addAttribute("cursos", cursoService.listarTodosDTO());
        return "cursos-list";
    }

    @GetMapping("/cursos/new")
    public String mostrarFormulario(Model model) {
        model.addAttribute("curso", new Curso());
        return "cursos-form";
    }

    @PostMapping("/cursos/save")
    public String guardar(CursoCreateDto dto) {
        cursoService.crearDesdeDTO(dto);
        return "redirect:/cursos/list";
    }

    @GetMapping("/cursos/edit/{id}")
    public String editar(@PathVariable Long id, Model model) throws NotFoundException {
        Curso curso = cursoService.buscarPorId(id);
        model.addAttribute("curso", curso);
        return "cursos-form";
    }

    @PostMapping("/cursos/update/{id}")
    public String actualizar(@PathVariable Long id, CursoUpdateDto dto) throws NotFoundException {
        cursoService.actualizarDesdeDTO(id, dto);
        return "redirect:/cursos/list";
    }

    @GetMapping("/cursos/desactivar/{id}")
    public String desactivar(@PathVariable Long id) throws NotFoundException {
        cursoService.desactivar(id);
        return "redirect:/cursos/list";
    }
    @GetMapping("/cursos/detail/{id}")
    public String detalle(@PathVariable Long id, Model model) throws NotFoundException {

        model.addAttribute("curso", cursoService.detalleDTO(id));

        return "cursos-detail";
    }
    @GetMapping("/cursos/buscar")
    public String buscar(@RequestParam String categoria, Model model) {
        model.addAttribute("cursos", cursoService.buscarPorCategoria(categoria));
        return "cursos-list";
    }
}
