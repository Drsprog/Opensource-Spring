package com.cursospring.gestioncine.service;

import java.util.List;
import com.cursospring.gestioncine.entity.Pelicula;

public interface PeliculaService {
    Pelicula guardarPelicula(Pelicula pelicula);
    List<Pelicula> obtenerProgramacionPorSala(String codigoSala);
}
