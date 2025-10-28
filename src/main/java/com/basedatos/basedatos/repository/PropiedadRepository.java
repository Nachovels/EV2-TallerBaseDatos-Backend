package com.basedatos.basedatos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.basedatos.basedatos.model.Propiedad;

import jakarta.transaction.Transactional;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {
    @Modifying
    @Transactional
    @Query(value = "BEGIN " +
            "PKG_PROPIEDADES.sp_actualizar_arriendo(" +
            ":p_nro_propiedad, :p_nuevo_valor" +
            "); END;",
            nativeQuery = true)
    void llamarSpActualizarArriendo(
            @Param("p_nro_propiedad") Long p_nro_propiedad,
            @Param("p_nuevo_valor") Integer p_nuevo_valor
    );
}
