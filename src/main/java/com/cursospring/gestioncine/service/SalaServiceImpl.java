package com.cursospring.gestioncine.service;

import java.time.Duration;
import java.util.*;

import org.springframework.stereotype.Service;

import com.cursospring.gestioncine.entity.Pelicula;
import com.cursospring.gestioncine.entity.Sala;
import com.cursospring.gestioncine.repository.SalaRepository;

@Service
public class SalaServiceImpl implements SalaService {

    private final SalaRepository salaRepository;

    public SalaServiceImpl(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    @Override
    public Sala guardarSala(Sala sala) {
        return salaRepository.save(sala);
    }

    @Override
    public Double calcularOcupacionEstimada(String codigoSala) {
        Optional<Sala> salaOptional = salaRepository.findByCodigo(codigoSala);

        if (salaOptional.isEmpty()) {
            throw new IllegalArgumentException("La sala con el código '" + codigoSala + "' no existe.");
        }

        Sala sala = salaOptional.get();
        if (sala.getHoraApertura() == null || sala.getHoraCierre() == null) {
            throw new IllegalArgumentException("La sala no tiene horario de apertura o cierre configurado.");
        }

        long minutosTotalesOperativos = Duration.between(sala.getHoraApertura(), sala.getHoraCierre()).toMinutes();
        if (minutosTotalesOperativos < 0) {
            minutosTotalesOperativos += 24 * 60;
        }

        if (minutosTotalesOperativos <= 0) {
            return 0.0;
        }

        List<Pelicula> peliculasAsignadas = sala.getPeliculas();
        long minutosOcupados = 0;
        if (peliculasAsignadas != null) {
            for (Pelicula p : peliculasAsignadas) {
                if (p.getDuracionMinutos() != null) {
                    minutosOcupados += p.getDuracionMinutos();
                }
            }
        }

        return ((double) minutosOcupados * 100.0) / minutosTotalesOperativos;
    }

}
