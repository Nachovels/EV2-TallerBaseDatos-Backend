package com.basedatos.basedatos.dto;

import lombok.Data;

@Data
public class UpdateSueldoRequest {
    private Integer nuevoSueldo;

    public void setNuevoSueldo(Integer nuevoSueldo) {
        this.nuevoSueldo = nuevoSueldo;
    }
}
