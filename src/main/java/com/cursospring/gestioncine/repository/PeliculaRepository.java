package com.cursospring.gestioncine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursospring.gestioncine.entity.Pelicula;
import java.util.List;


public interface PeliculaRepository extends JpaRepository<Pelicula, Integer>{
    List<Pelicula> findBySalaCodigoOrderByHoraProyeccionAsc(String codigoSala);
}
