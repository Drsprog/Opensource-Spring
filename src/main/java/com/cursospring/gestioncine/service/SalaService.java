package com.cursospring.gestioncine.service;

import com.cursospring.gestioncine.entity.Sala;

public interface SalaService {
    Sala guardarSala(Sala sala);
    void calcularOcupacionEstimada(String codigoSala);
}
