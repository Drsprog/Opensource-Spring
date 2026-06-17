package com.cursospring.gestioncine.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.cursospring.gestioncine.entity.Pelicula;
import com.cursospring.gestioncine.entity.Sala;
import com.cursospring.gestioncine.repository.PeliculaRepository;
import com.cursospring.gestioncine.repository.SalaRepository;

public class PeliculaServiceImpl implements PeliculaService{

    private final SalaRepository salaRepository;
    private final PeliculaRepository peliculaRepository;


    public PeliculaServiceImpl(SalaRepository salaRepository, PeliculaRepository peliculaRepository) {
        this.salaRepository = salaRepository;
        this.peliculaRepository = peliculaRepository;
    }

    @Override
    public Pelicula guardarPelicula(Pelicula pelicula) {
        try {
            if(pelicula.getSala()!=null && pelicula.getSala().getCodigo()!=null){
                Optional<Sala> salaOpt = salaRepository.findByCodigo(pelicula.getSala().getCodigo());
                if (salaOpt.isPresent()) {
                pelicula.setSala(salaOpt.get());
                }
            }
            return peliculaRepository.save(pelicula);
        } catch (Exception e) {
            System.out.println("Error al guardar la película '" + pelicula.getTitulo() + "': " + e.getMessage());
            return null;
        }
    }

    public void validarHorarioSala(Pelicula nuevaPelicula, Sala sala){
        LocalTime inicioPeli= nuevaPelicula.getHoraProyeccion().toLocalTime();
        LocalTime finPeli= inicioPeli.plusMinutes(nuevaPelicula.getDuracionMinutos());

        LocalTime aperturaSala= sala.getHoraApertura();
        LocalTime cierreSala=sala.getHoraCierre();

        if(inicioPeli.isBefore(aperturaSala)){
            throw new IllegalArgumentException("La hora de proyeccion ("+ inicioPeli
                + ") es anterior a la hora de apertura de la sala" + sala.getCodigo() + " (" + aperturaSala + ").");
        }

        if (cierreSala.isAfter(cierreSala)){
            throw new IllegalArgumentException("La película termina a las " + finPeli 
                + ", lo cual excede la hora de cierre de la sala " + sala.getCodigo() + " (" + cierreSala + ").");
        }
    }


    public void validarSolapamiento(Pelicula nuevaPelicula){
        // Traemos todas las películas que ya están registradas en esa misma sala
        List<Pelicula> peliculasExistentes = peliculaRepository.findBySalaCodigoOrderByHoraProyeccionAsc(nuevaPelicula.getSala().getCodigo());

        LocalDateTime inicioNueva = nuevaPelicula.getHoraProyeccion();
        LocalDateTime finNueva = inicioNueva.plusMinutes(nuevaPelicula.getDuracionMinutos());

        for (Pelicula existente : peliculasExistentes) {
            LocalDateTime inicioExistente = existente.getHoraProyeccion();
            LocalDateTime finExistente = inicioExistente.plusMinutes(existente.getDuracionMinutos());

            // Fórmula matemática de solapamiento de intervalos temporales:
            // (InicioA < FinB) Y (FinA > InicioB)
            if (inicioNueva.isBefore(finExistente) && finNueva.isAfter(inicioExistente)) {
                throw new IllegalArgumentException("¡Conflicto de solapamiento en la sala " + nuevaPelicula.getSala().getCodigo() 
                        + "! La nueva película '" + nuevaPelicula.getTitulo() + "' se cruza con '" 
                        + existente.getTitulo() + "' (Horario ocupado: " 
                        + inicioExistente.toLocalTime() + " - " + finExistente.toLocalTime() + ").");
            }
        }
    }

    @Override
    public void mostrarProgramacionPorSala(String codigoSala) {

        try {
            List<Pelicula> peliculas= peliculaRepository.findBySalaCodigoOrderByHoraProyeccionAsc(codigoSala);

            if(peliculas.isEmpty()) {
                System.out.println("No hay peliculas programadas para la sala: " + codigoSala);
                return;
            }

            System.out.println("\n=== PROGRAMACION DE LA SALA: " + codigoSala + "===");

            for (Pelicula p: peliculas) {
                LocalDateTime horaInicio=p.getHoraProyeccion();
                LocalDateTime horaFin=horaInicio.plusMinutes(p.getDuracionMinutos());

                System.out.println("---------------------------");
                System.out.println("Titulo: " + p.getTitulo());
                System.out.println("Director: " + p.getTitulo());
                System.out.println("Genero: " + p.getGenero());
                System.out.println("Inicio: " + horaInicio.toLocalTime() + " | Fin (Calculado): " + horaFin.toLocalTime());

            }

            System.out.println("===========================================");
            
        } catch (NullPointerException e) {
            System.out.println("Error de datos: Alguna de las películas en la sala " + codigoSala + " tiene la fecha de proyección o la duración en blanco (null).");
        } catch (Exception e) {
            System.out.println("Error inesperado al cargar la programación de la sala " + codigoSala + ": " + e.getMessage());
        }        
    }
}
