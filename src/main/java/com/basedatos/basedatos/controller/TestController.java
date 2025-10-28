package com.basedatos.basedatos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/test")
    public String testEndpoint() {
        return "La conexión a la base de datos fue exitosa!";
    }
}
