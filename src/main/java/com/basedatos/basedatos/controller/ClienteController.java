package com.basedatos.basedatos.controller;

import com.basedatos.basedatos.model.Cliente;
import com.basedatos.basedatos.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException; // Importa SQLException
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
// NO necesitas @CrossOrigin aquí si usas WebConfig.java
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<Cliente> getAllClientes(){
        // Nota: findAll() también podría lanzar excepciones, podrías añadir try-catch aquí si es necesario
        return clienteRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> createCliente(@RequestBody Cliente cliente){
        try{
            clienteRepository.llamarSpAgregarCliente(
                cliente.getNumRutCli(),
                cliente.getDvRutCli(),
                cliente.getApPaternoCli(),
                cliente.getApMaternoCli(),
                cliente.getNombreCli(),
                cliente.getDireccionCli(),
                cliente.getIdEstCivil(),
                cliente.getFonoFijoCli(),
                cliente.getCelularCli(),
                cliente.getRentaCli()
            );
            // Mensaje de éxito más específico
            return ResponseEntity.ok("Cliente " + cliente.getNumRutCli() + " agregado con éxito.");

        } catch (Exception e){
            // --- INICIO: Manejo de error mejorado ---
            String errorMessage = findOracleErrorMessage(e);
            System.err.println("Error completo al agregar cliente: " + e.getMessage()); // Log completo en backend
            // Determinar código de estado HTTP (400 para error de cliente como RUT duplicado)
            int statusCode = (errorMessage.contains("RUT") && errorMessage.contains("ya existe")) ? 400 : 500;
            return ResponseEntity.status(statusCode).body(errorMessage);
            // --- FIN: Manejo de error mejorado ---
        }
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<String> deleteCliente(@PathVariable Long rut){
        try{
            clienteRepository.llamarSpBorrarCliente(rut);
            // Mensaje de éxito más específico
            return ResponseEntity.ok("Cliente "+rut+ " borrado con éxito.");

        } catch (Exception e){
            // --- INICIO: Manejo de error mejorado ---
            String errorMessage = findOracleErrorMessage(e);
            System.err.println("Error completo al borrar cliente: " + e.getMessage()); // Log completo en backend
            // Determinar código de estado HTTP (400/404 para errores esperados, 500 para otros)
            int statusCode = 500;
            if (errorMessage.contains("tiene arriendos registrados")) {
                statusCode = 400; // Bad Request - Lógica de negocio
            } else if (errorMessage.contains("No se encontró un cliente")) {
                statusCode = 404; // Not Found
            }
            return ResponseEntity.status(statusCode).body(errorMessage);
            // --- FIN: Manejo de error mejorado ---
        }
    }

    // --- FUNCIÓN AUXILIAR PARA EXTRAER ERROR DE ORACLE ---
    private String findOracleErrorMessage(Throwable throwable) {
        Throwable cause = throwable;
        String specificOracleMessage = null; // Para guardar el mensaje ORA específico si lo encontramos

        while (cause != null) {
            if (cause instanceof java.sql.SQLException) {
                java.sql.SQLException sqlEx = (java.sql.SQLException) cause;
                // Los errores ORA- personalizados (-20000 a -20999)
                if (sqlEx.getErrorCode() >= 20000 && sqlEx.getErrorCode() <= 20999) {
                    // Extraer solo el mensaje descriptivo
                    String message = sqlEx.getMessage();
                    // Busca el primer ":" después de "ORA-xxxxx:"
                    int firstColonIndex = message.indexOf(':');
                    int secondColonIndex = -1;
                    if(firstColonIndex != -1) {
                         secondColonIndex = message.indexOf(':', firstColonIndex + 1);
                    }

                    if (secondColonIndex != -1) {
                       specificOracleMessage = message.substring(secondColonIndex + 1).trim();
                       break; // Encontramos el mensaje que queríamos
                    } else {
                        specificOracleMessage = message; // Devolver mensaje SQL completo si el formato no es el esperado
                        break;
                    }
                } else if (sqlEx.getMessage() != null && sqlEx.getMessage().toUpperCase().startsWith("ORA-")) {
                     // Para otros errores ORA- estándar, guardar el mensaje completo
                     specificOracleMessage = sqlEx.getMessage();
                     // No rompemos el bucle aquí por si hay una causa más específica anidada
                }
            }
            cause = cause.getCause(); // Avanza a la siguiente causa anidada
        }

        // Si encontramos un mensaje ORA específico, lo devolvemos
        if (specificOracleMessage != null) {
            return specificOracleMessage;
        }

        // Si no, devolvemos un mensaje genérico o el de la excepción original
        return throwable.getMessage() != null ? throwable.getMessage() : "Error inesperado en el servidor.";
    }
    // --- FIN FUNCIÓN AUXILIAR ---
}