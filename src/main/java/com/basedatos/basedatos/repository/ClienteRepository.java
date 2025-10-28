package com.basedatos.basedatos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.basedatos.basedatos.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
        @Modifying
        @Transactional
        @Query(value = "BEGIN " +
                "sp_agregar_cliente(" +
                ":p_rut, :p_dv, :p_paterno, :p_materno, :p_nombre, " +
                ":p_direccion, :p_estcivil, :p_fono, :p_celular, :p_renta" +
                "); END;",
                nativeQuery = true) // 'nativeQuery = true' es vital
        void llamarSpAgregarCliente(
                @Param("p_rut") Long p_rut,
                @Param("p_dv") String p_dv,
                @Param("p_paterno") String p_paterno,
                @Param("p_materno") String p_materno,
                @Param("p_nombre") String p_nombre,
                @Param("p_direccion") String p_direccion,
                @Param("p_estcivil") Integer p_estcivil,
                @Param("p_fono") Long p_fono,
                @Param("p_celular") Long p_celular,
                @Param("p_renta") Integer p_renta
        );

        @Modifying
        @Transactional
        @Query(value = "BEGIN sp_borrar_cliente(:p_rut); END;", nativeQuery = true)
        void llamarSpBorrarCliente(@Param("p_rut") Long p_rut);
}
