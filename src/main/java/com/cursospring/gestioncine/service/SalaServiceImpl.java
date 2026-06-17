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
    public void calcularOcupacionEstimada(String codigoSala) {
        Optional<Sala> salaOptional = salaRepository.findByCodigo(codigoSala);

        if(salaOptional.isEmpty()){
            System.out.println("ERROR: La sala con el codigo: " + codigoSala + "no existe");
            return;
        }

        Sala sala= salaOptional.get();
    
        Long minutosTotalesOperativos = Duration.between(sala.getHoraApertura(), sala.getHoraCierre()).toMinutes();

        List<Pelicula> peliculasAsignadas= sala.getPeliculas();
        Long minutosOcupados = (long) 0;
        for(Pelicula p: peliculasAsignadas){
            minutosOcupados+=p.getDuracionMinutos();
        }

        Double porcentajeOcupacion= ((double) minutosOcupados*100) / minutosTotalesOperativos;

        System.out.println("\n=== REPORTE DE OCUPACIÓN: SALA " + codigoSala + " ===");
        System.out.println("Horario operativo: " + sala.getHoraApertura() + " a " + sala.getHoraCierre());
        System.out.println("Tiempo disponible: " + minutosTotalesOperativos + " minutos.");
        System.out.println("Tiempo ocupado   : " + minutosOcupados + " minutos.");
        System.out.printf("Porcentaje Uso   : %.2f%%\n", porcentajeOcupacion);
        System.out.println("=================================================");
    }

}
