package com.basedatos.basedatos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "EMPLEADO")
public class Empleado {
    
    @Id
    @Column(name = "NUMRUT_EMP")
    private Long numRutEmp;

    @Column(name = "NOMBRE_EMP")
    private String nombreEmp;

    @Column(name = "APPATERNO_EMP")
    private String apPaternoEmp;

    @Column(name = "APMATERNO_EMP")
    private String apMaternoEmp;

    @Column(name = "SUELDO_EMP")
    private Integer sueldoEmp;
}
