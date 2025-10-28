package com.basedatos.basedatos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basedatos.basedatos.dto.UpdateArriendoRequest;
import com.basedatos.basedatos.model.Propiedad;
import com.basedatos.basedatos.repository.PropiedadRepository;

@RestController
@RequestMapping("/api/propiedades")
public class PropiedadController {
    
    private final PropiedadRepository propiedadRepository;

    public PropiedadController(PropiedadRepository propiedadRepository) {
        this.propiedadRepository = propiedadRepository;
    }

    @GetMapping
    public List<Propiedad> getAllPropiedades(){
        return propiedadRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Propiedad> getPropiedadById(@PathVariable Long id){
        return propiedadRepository.findById(id)
            .map(propiedad -> ResponseEntity.ok(propiedad))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateArriendo(
        @PathVariable Long id,
        @RequestBody UpdateArriendoRequest request
        ){
            try{
                propiedadRepository.llamarSpActualizarArriendo(id, request.getValorArriendo()
                );
                return ResponseEntity.ok("Propiedad "+id+" actualizada.");
            } catch (Exception e){
                return ResponseEntity.status(500).body("Error al actualizar propiedad: " + e.getMessage());
        }
    }
}
