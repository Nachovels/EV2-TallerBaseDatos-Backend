package com.basedatos.basedatos.controller;

import java.sql.SQLException; // Importa SQLException
import java.util.List;
import java.util.Optional; // Importa Optional si usas findById

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// Asegúrate de importar tu DTO y Modelo
import com.basedatos.basedatos.dto.UpdateArriendoRequest;
import com.basedatos.basedatos.model.Propiedad;
import com.basedatos.basedatos.repository.PropiedadRepository;

@RestController
@RequestMapping("/api/propiedades")
// NO necesitas @CrossOrigin aquí si usas WebConfig.java
public class PropiedadController {

    private final PropiedadRepository propiedadRepository;

    public PropiedadController(PropiedadRepository propiedadRepository) {
        this.propiedadRepository = propiedadRepository;
    }

    @GetMapping
    public List<Propiedad> getAllPropiedades(){
        return propiedadRepository.findAll();
    }

    // (Opcional) Endpoint para obtener una propiedad por ID
    @GetMapping("/{id}")
    public ResponseEntity<Propiedad> getPropiedadById(@PathVariable Long id){
        Optional<Propiedad> propiedadOpt = propiedadRepository.findById(id);
        // Forma más explícita de devolver 404 si no se encuentra
        if (propiedadOpt.isPresent()) {
            return ResponseEntity.ok(propiedadOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateArriendo(
        @PathVariable Long id,
        @RequestBody UpdateArriendoRequest request
        ){
            try{
                propiedadRepository.llamarSpActualizarArriendo(
                    id,
                    request.getValorArriendo()
                );
                // Mensaje de éxito más específico
                return ResponseEntity.ok("Valor de arriendo para propiedad "+id+" actualizado con éxito.");

            } catch (Exception e){
                // --- INICIO: Manejo de error mejorado ---
                String errorMessage = findOracleErrorMessage(e);
                System.err.println("Error completo al actualizar arriendo: " + e.getMessage()); // Log completo en backend
                // Determinar código de estado HTTP (404 si no se encuentra, 500 para otros)
                int statusCode = (errorMessage.contains("no fue encontrada")) ? 404 : 500;
                return ResponseEntity.status(statusCode).body(errorMessage);
                // --- FIN: Manejo de error mejorado ---
        }
    }

    // --- FUNCIÓN AUXILIAR PARA EXTRAER ERROR DE ORACLE ---
    private String findOracleErrorMessage(Throwable throwable) {
        Throwable cause = throwable;
        String specificOracleMessage = null;

        while (cause != null) {
            if (cause instanceof java.sql.SQLException) {
                java.sql.SQLException sqlEx = (java.sql.SQLException) cause;
                if (sqlEx.getErrorCode() >= 20000 && sqlEx.getErrorCode() <= 20999) {
                    String message = sqlEx.getMessage();
                    int firstColonIndex = message.indexOf(':');
                    int secondColonIndex = -1;
                    if(firstColonIndex != -1) {
                         secondColonIndex = message.indexOf(':', firstColonIndex + 1);
                    }
                    if (secondColonIndex != -1) {
                       specificOracleMessage = message.substring(secondColonIndex + 1).trim();
                       break;
                    } else {
                        specificOracleMessage = message;
                        break;
                    }
                } else if (sqlEx.getMessage() != null && sqlEx.getMessage().toUpperCase().startsWith("ORA-")) {
                     specificOracleMessage = sqlEx.getMessage();
                }
            }
            cause = cause.getCause();
        }
        if (specificOracleMessage != null) {
            return specificOracleMessage;
        }
        return throwable.getMessage() != null ? throwable.getMessage() : "Error inesperado en el servidor.";
    }
    // --- FIN FUNCIÓN AUXILIAR ---
}