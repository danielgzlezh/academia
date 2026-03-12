package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.exception.BusinessException;
import com.example.demo.exception.NotFoundException;

@ControllerAdvice
@org.springframework.stereotype.Controller
public class ControladorExcepciones {

	private static final Logger logger = LoggerFactory.getLogger(ControladorExcepciones.class);

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> manejarNotFound(NotFoundException ex) {

		logger.error("Recurso no encontrado: {}", ex.getMessage());

		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<String> manejarBusiness(BusinessException ex) {

		logger.warn("Error de negocio: {}", ex.getMessage());

		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> manejarErrorGeneral(Exception ex) {

		logger.error("Error inesperado: {}", ex.getMessage());

		return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}