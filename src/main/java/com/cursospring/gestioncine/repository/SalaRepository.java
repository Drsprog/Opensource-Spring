package com.cursospring.gestioncine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursospring.gestioncine.entity.Sala;


public interface SalaRepository extends JpaRepository<Sala, Integer>{
    Optional<Sala> findByCodigo(String codigo);
}
