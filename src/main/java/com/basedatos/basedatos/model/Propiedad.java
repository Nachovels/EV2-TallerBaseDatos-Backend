package com.basedatos.basedatos.model;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "PROPIEDAD")
public class Propiedad {
    
    @Id
    @Column(name = "NRO_PROPIEDAD")
    private Long nroPropiedad;

    @Column(name = "DIRECCION_PROPIEDAD")
    private String direccionPropiedad;

    @Column(name = "NRO_DORMITORIOS")
    private Integer nroDormitorios;

    @Column(name = "NRO_BANOS")
    private Integer nroBanos;

    @Column(name = "VALOR_ARRIENDO")
    private Integer valorArriendo;

    @Column(name = "VALOR_GASTO_COMUN")
    private Integer valorGastoComun;

    @Formula("PKG_PROPIEDADES.fn_propiedad_esta_arrendada(nro_propiedad)")
    private String estaArrendada;
}
