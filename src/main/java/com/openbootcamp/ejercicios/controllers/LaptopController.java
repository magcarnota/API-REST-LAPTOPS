package com.openbootcamp.ejercicios.controllers;

import com.openbootcamp.ejercicios.entities.Laptop;
import com.openbootcamp.ejercicios.repositories.LaptopRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class LaptopController {
    private Logger logger = LoggerFactory.getLogger(LaptopController.class);
    private LaptopRepository laptopRepository;

    public LaptopController(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    /**
     * Devuelve todos los laptops que hay en el repositorio
     * @return
     */
    @GetMapping("/api/laptops")
    @ApiOperation("Devuelve todos los laptops del repositorio")
    public List<Laptop> findAll() {
        return laptopRepository.findAll();
    }

    /**
     * Devuelve el laptop asociado con el id indicado
     * @param id
     * @return
     */
    @GetMapping("api/laptops/{id}")
    @ApiOperation("Devuelve el laptop del repositorio indicado por id (Long)")
    public ResponseEntity<Laptop> findOneById(@ApiParam("Clave primaria de tipo Long") @PathVariable Long id) {
        Optional<Laptop> laptopOptional = laptopRepository.findById(id);
        if(laptopOptional.isPresent()) {
            return ResponseEntity.ok(laptopOptional.get());
        }
        else {
            logger.warn("Tratando de recuperar un laptop que no existe!");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creamos un nuevo laptop y lo guardamos en el repositorio
     * @param laptop
     * @return
     */
    @PostMapping("/api/laptops")
    @ApiOperation("Guarda el laptop indicado por JSON en el repositorio")
    public ResponseEntity<Laptop> create(@ApiParam("Laptop pasado en JSON") @RequestBody Laptop laptop) {
        if(laptop.getId() != null) {
            logger.warn("Tratando de crear un laptop con un id!");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(laptopRepository.save(laptop));
    }

    /**
     * Actualizar un laptop existente en el repositorio
     * @param laptop
     * @return
     */
    @PutMapping("/api/laptops")
    @ApiOperation("Actualiza el laptop pasado por JSON si se encuentra en el repositorio")
    public ResponseEntity<Laptop> update(@ApiParam("Laptop pasado en JSON") @RequestBody Laptop laptop) {
        if(laptop.getId() == null) {
            logger.warn("Tratando de crear y no actualizar un nuevo laptop!");
            return ResponseEntity.badRequest().build();
        }
        if(!laptopRepository.existsById(laptop.getId())) {
            logger.warn("Tratando de actualizar un laptop que no existe!");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(laptopRepository.save(laptop));
    }

    /**
     * Borra el laptop indicado por el id
     * @param id
     * @return
     */
    @DeleteMapping("/api/laptops/{id}")
    @ApiOperation("Borra el laptop del repositorio indicado por su id (Long)")
    public ResponseEntity<Laptop> delete(@ApiParam("id de tipo Long del laptop a eliminar") @PathVariable Long id) {
        if(!laptopRepository.existsById(id)) {
            logger.warn("Tratando de borrar un laptop que no existe!");
            return ResponseEntity.notFound().build();
        }
        laptopRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Borra todos los laptops del repositorio
     * @return
     */
    @DeleteMapping("/api/laptops")
    @ApiOperation("Borra todos los laptops del repositorio")
    public ResponseEntity<Laptop> deleteAll() {
        laptopRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

}
