package com.basedatos.basedatos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.basedatos.basedatos.model.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    
}
