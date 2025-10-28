package com.basedatos.basedatos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basedatos.basedatos.repository.ClienteRepository;
import com.basedatos.basedatos.model.Cliente;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<Cliente> getAllClientes(){
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
            return ResponseEntity.ok("Cliente agregado");
        } catch (Exception e){
            return ResponseEntity.status(500).body("Error al agregar cliente: " + e.getMessage());
        }
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<String> deleteCliente(@PathVariable Long rut){
        try{
            clienteRepository.llamarSpBorrarCliente(rut);

            return ResponseEntity.ok("Cliente "+rut+ " borrado");
        } catch (Exception e){
            return ResponseEntity.status(500).body("Error al borrar cliente: " + e.getMessage());
        }
    }
}
