package com.basedatos.basedatos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basedatos.basedatos.dto.UpdateSueldoRequest;
import com.basedatos.basedatos.model.Empleado;
import com.basedatos.basedatos.repository.EmpleadoRepository;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    
    private final EmpleadoRepository empleadoRepository;

    public EmpleadoController(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @PutMapping("/{rut}/sueldo")
    public ResponseEntity<String> actualizarSueldo(
        @PathVariable Long rut,
        @RequestBody UpdateSueldoRequest request
    ) {try{
        Optional<Empleado> empleadoOpt = empleadoRepository.findById(rut);

        if (empleadoOpt.isEmpty()){
            return ResponseEntity.status(404).body("Empleado no encontrado");
        }

        Empleado empleado = empleadoOpt.get();

        empleado.setSueldoEmp(request.getNuevoSueldo());

        empleadoRepository.save(empleado);

        return ResponseEntity.ok("Sueldo actualizado para empleado " + rut);

    } catch (Exception e){
        return ResponseEntity.status(500).body("Error al actualizar sueldo: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Empleado> getAllEmpleados() {
    return empleadoRepository.findAll();
}
}
