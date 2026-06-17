package com.cursospring.gestioncine.service;

import com.cursospring.gestioncine.entity.Pelicula;

public interface PeliculaService {
    Pelicula guardarPelicula(Pelicula pelicula);
    void mostrarProgramacionPorSala(String codigoSala);
}
