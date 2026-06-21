package com.cursospring.gestioncine.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cursospring.gestioncine.entity.Pelicula;
import com.cursospring.gestioncine.entity.Sala;
import com.cursospring.gestioncine.repository.PeliculaRepository;
import com.cursospring.gestioncine.repository.SalaRepository;

@Service
public class PeliculaServiceImpl implements PeliculaService{

    private final SalaRepository salaRepository;
    private final PeliculaRepository peliculaRepository;


    public PeliculaServiceImpl(SalaRepository salaRepository, PeliculaRepository peliculaRepository) {
        this.salaRepository = salaRepository;
        this.peliculaRepository = peliculaRepository;
    }

    @Override
    public Pelicula guardarPelicula(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("La película no puede ser nula.");
        }

        if (pelicula.getSala() != null && pelicula.getSala().getCodigo() != null) {
            Optional<Sala> salaOpt = salaRepository.findByCodigo(pelicula.getSala().getCodigo());
            if (salaOpt.isPresent()) {
                Sala sala = salaOpt.get();
                pelicula.setSala(sala);
                
                // Ejecutar validaciones
                validarHorarioSala(pelicula, sala);
                validarSolapamiento(pelicula);
            } else {
                throw new IllegalArgumentException("La sala con el código '" + pelicula.getSala().getCodigo() + "' no existe.");
            }
        } else {
            throw new IllegalArgumentException("La película debe estar asignada a una sala válida.");
        }

        return peliculaRepository.save(pelicula);
    }

    public void validarHorarioSala(Pelicula nuevaPelicula, Sala sala){
        if (nuevaPelicula.getHoraProyeccion() == null) {
            throw new IllegalArgumentException("La hora de proyección es requerida.");
        }
        if (nuevaPelicula.getDuracionMinutos() == null) {
            throw new IllegalArgumentException("La duración de la película es requerida.");
        }

        LocalTime inicioPeli = nuevaPelicula.getHoraProyeccion().toLocalTime();
        LocalTime finPeli = inicioPeli.plusMinutes(nuevaPelicula.getDuracionMinutos());

        LocalTime aperturaSala = sala.getHoraApertura();
        LocalTime cierreSala = sala.getHoraCierre();

        if (aperturaSala == null || cierreSala == null) {
            throw new IllegalArgumentException("La sala debe tener horario de apertura y cierre configurados.");
        }

        if (inicioPeli.isBefore(aperturaSala)) {
            throw new IllegalArgumentException("La hora de proyeccion (" + inicioPeli
                + ") es anterior a la hora de apertura de la sala " + sala.getCodigo() + " (" + aperturaSala + ").");
        }

        boolean cruzaMedianocheOperativa = cierreSala.isBefore(aperturaSala);
        boolean cruzaMedianochePelicula = finPeli.isBefore(inicioPeli);

        if (cruzaMedianocheOperativa) {
            if (inicioPeli.isBefore(aperturaSala) && finPeli.isAfter(cierreSala)) {
                throw new IllegalArgumentException("La película termina a las " + finPeli 
                    + ", lo cual excede la hora de cierre de la sala " + sala.getCodigo() + " (" + cierreSala + ").");
            }
            if (cruzaMedianochePelicula && finPeli.isAfter(cierreSala)) {
                throw new IllegalArgumentException("La película termina a las " + finPeli 
                    + ", lo cual excede la hora de cierre de la sala " + sala.getCodigo() + " (" + cierreSala + ").");
            }
        } else {
            if (cruzaMedianochePelicula || finPeli.isAfter(cierreSala)) {
                throw new IllegalArgumentException("La película termina a las " + finPeli 
                    + ", lo cual excede la hora de cierre de la sala " + sala.getCodigo() + " (" + cierreSala + ").");
            }
        }
    }


    public void validarSolapamiento(Pelicula nuevaPelicula){
        if (nuevaPelicula.getSala() == null || nuevaPelicula.getSala().getCodigo() == null) {
            return;
        }

        List<Pelicula> peliculasExistentes = peliculaRepository.findBySalaCodigoOrderByHoraProyeccionAsc(nuevaPelicula.getSala().getCodigo());

        LocalDateTime inicioNueva = nuevaPelicula.getHoraProyeccion();
        LocalDateTime finNueva = inicioNueva.plusMinutes(nuevaPelicula.getDuracionMinutos());

        for (Pelicula existente : peliculasExistentes) {
            if (nuevaPelicula.getId() != null && nuevaPelicula.getId().equals(existente.getId())) {
                continue;
            }

            LocalDateTime inicioExistente = existente.getHoraProyeccion();
            LocalDateTime finExistente = inicioExistente.plusMinutes(existente.getDuracionMinutos());

            if (inicioNueva.isBefore(finExistente) && finNueva.isAfter(inicioExistente)) {
                throw new IllegalArgumentException("¡Conflicto de solapamiento en la sala " + nuevaPelicula.getSala().getCodigo() 
                        + "! La nueva película '" + nuevaPelicula.getTitulo() + "' se cruza con '" 
                        + existente.getTitulo() + "' (Horario ocupado: " 
                        + inicioExistente.toLocalTime() + " - " + finExistente.toLocalTime() + ").");
            }
        }
    }

    @Override
    public List<Pelicula> obtenerProgramacionPorSala(String codigoSala) {
        Optional<Sala> salaOpt = salaRepository.findByCodigo(codigoSala);
        if (salaOpt.isEmpty()) {
            throw new IllegalArgumentException("La sala con el código '" + codigoSala + "' no existe.");
        }
        return peliculaRepository.findBySalaCodigoOrderByHoraProyeccionAsc(codigoSala);
    }
}
