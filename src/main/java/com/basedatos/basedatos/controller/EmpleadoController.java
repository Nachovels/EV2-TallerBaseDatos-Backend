package com.basedatos.basedatos.controller;

import java.sql.SQLException; // Importa SQLException
import java.util.List; // Importa List
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importa GetMapping también

// Asegúrate de importar tu DTO, Modelo y Repositorio
import com.basedatos.basedatos.dto.UpdateSueldoRequest;
import com.basedatos.basedatos.model.Empleado;
import com.basedatos.basedatos.repository.EmpleadoRepository;

@RestController
@RequestMapping("/api/empleados")
// NO necesitas @CrossOrigin aquí si usas WebConfig.java
public class EmpleadoController {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoController(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    // --- Endpoint GET para listar empleados ---
    @GetMapping
    public List<Empleado> getAllEmpleados() {
        return empleadoRepository.findAll();
    }
    // --- Fin Endpoint GET ---

    @PutMapping("/{rut}/sueldo") // Cambiado {id} a {rut} para claridad
    public ResponseEntity<String> actualizarSueldo(
        @PathVariable Long rut, // Cambiado id a rut
        @RequestBody UpdateSueldoRequest request
    ) {
        try{
            Optional<Empleado> empleadoOpt = empleadoRepository.findById(rut); // Usa rut

            if (empleadoOpt.isEmpty()){
                // Mensaje de error más específico
                return ResponseEntity.status(404).body("Empleado con RUT " + rut + " no encontrado.");
            }

            Empleado empleado = empleadoOpt.get();
            Integer sueldoAntiguo = empleado.getSueldoEmp(); // Guarda el sueldo antiguo para el log si falla
            empleado.setSueldoEmp(request.getNuevoSueldo());

            // Aquí es donde el TRIGGER se activará
            empleadoRepository.save(empleado);

            // Mensaje de éxito más específico
            return ResponseEntity.ok("Sueldo actualizado para empleado " + rut + ".");

        } catch (Exception e){
            // --- INICIO: Manejo de error mejorado ---
            String errorMessage = findOracleErrorMessage(e);
            System.err.println("Error completo al actualizar sueldo: " + e.getMessage()); // Log completo en backend
             // Determinar código de estado HTTP (400 para error del trigger, 500 para otros)
            int statusCode = (errorMessage.contains("No se puede reducir el sueldo")) ? 400 : 500;
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