package com.basedatos.basedatos.dto;

import lombok.Data;

@Data
public class UpdateArriendoRequest {
    
    private Integer valorArriendo;
    
    public void setValorArriendo(Integer valorArriendo) {
        this.valorArriendo = valorArriendo;
    }
}
