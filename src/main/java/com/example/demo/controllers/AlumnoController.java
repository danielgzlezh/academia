	package com.example.demo.controllers;
	
	import java.util.List;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.*;
	
	import com.example.demo.dto.AlumnoCreateDto;
	import com.example.demo.dto.AlumnoDetailDto;
	import com.example.demo.dto.AlumnoListDto;
	import com.example.demo.dto.AlumnoUpdateDto;	
	import com.example.demo.exception.NotFoundException;
	import com.example.demo.service.AlumnoService;
	
	@Controller
	@RequestMapping("/alumnos")
	public class AlumnoController {
	
	    @Autowired
	    private AlumnoService service;
	
	    // LISTAR ALUMNOS
	    @GetMapping
	    public String listar(Model model) {
	
	        List<AlumnoListDto> lista = service.listarAlumno();
	        model.addAttribute("alumnos", lista);
	
	        return "alumnos/lista";
	    }
	
	    // VER DETALLE
	    @GetMapping("/{id}")
	    public String detalle(@PathVariable Long id, Model model) throws NotFoundException {
	
	        AlumnoDetailDto alumno = service.obtenerPorId(id);
	        model.addAttribute("alumno", alumno);
	
	        return "alumnos/detalle";
	    }
	
	    // FORMULARIO CREAR
	    @GetMapping("/nuevo")
	    public String formularioCrear(Model model) {
	
	        model.addAttribute("alumno", new AlumnoCreateDto());
	
	        return "alumnos/formCrear";
	    }
	
	    // GUARDAR ALUMNO
	    @PostMapping("/guardar")
	    public String guardar(@ModelAttribute AlumnoCreateDto dto) {
	
	        service.crearAlumno(dto);
	
	        return "redirect:/alumnos";
	    }
	
	    // FORMULARIO EDITAR
	    @GetMapping("/editar/{id}")
	    public String formularioEditar(@PathVariable Long id, Model model) throws NotFoundException {
	
	        AlumnoDetailDto alumno = service.obtenerPorId(id);
	
	        AlumnoUpdateDto dto = new AlumnoUpdateDto();
	        dto.setNombre(alumno.getNombre());
	        dto.setEmail(alumno.getEmail());
	
	        model.addAttribute("alumno", dto);
	        model.addAttribute("id", id);
	
	        return "alumnos/formEditar";
	    }
	
	    // ACTUALIZAR
	    @PostMapping("/actualizar/{id}")
	    public String actualizar(@PathVariable Long id, @ModelAttribute AlumnoUpdateDto dto) throws NotFoundException {
	
	        service.editarAlumno(id, dto);
	
	        return "redirect:/alumnos";
	    }
	
	    // ELIMINAR
	    @GetMapping("/eliminar/{id}")
	    public String eliminar(@PathVariable Long id) throws NotFoundException {
	
	        service.eliminarAlumno(id);
	
	        return "redirect:/alumnos";
	    }
	
	    // BUSCAR POR NOMBRE
	    @GetMapping("/buscar")
	    public String buscar(@RequestParam String nombre, Model model) {
	
	        List<AlumnoListDto> lista = service.buscarPorNombre(nombre);
	        model.addAttribute("alumnos", lista);
	
	        return "alumnos/lista";
	    }
	}