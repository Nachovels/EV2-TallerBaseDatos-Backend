package com.basedatos.basedatos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "CLIENTE")
public class Cliente {
    @Id
    @Column(name = "NUMRUT_CLI")
    private Long numRutCli;

    @Column(name = "DVRUT_CLI")
    private String dvRutCli;

    @Column(name = "APPATERNO_CLI")
    private String apPaternoCli;
    
    @Column(name = "APMATERNO_CLI")
    private String apMaternoCli;
    
    @Column(name = "NOMBRE_CLI")
    private String nombreCli;
    
    @Column(name = "DIRECCION_CLI")
    private String direccionCli;
    
    @Column(name = "ID_ESTCIVIL")
    private Integer idEstCivil;
    
    @Column(name = "FONOFIJO_CLI")
    private Long fonoFijoCli;
    
    @Column(name = "CELULAR_CLI")
    private Long celularCli;
    
    @Column(name = "RENTA_CLI")
    private Integer rentaCli;
}
