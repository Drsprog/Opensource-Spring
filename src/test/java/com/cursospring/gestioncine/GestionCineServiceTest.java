package com.cursospring.gestioncine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cursospring.gestioncine.entity.Pelicula;
import com.cursospring.gestioncine.entity.Sala;
import com.cursospring.gestioncine.repository.PeliculaRepository;
import com.cursospring.gestioncine.repository.SalaRepository;
import com.cursospring.gestioncine.service.PeliculaServiceImpl;
import com.cursospring.gestioncine.service.SalaServiceImpl;

class GestionCineServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private PeliculaRepository peliculaRepository;

    @InjectMocks
    private PeliculaServiceImpl peliculaService;

    @InjectMocks
    private SalaServiceImpl salaService;

    private Sala salaTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        salaTest = new Sala();
        salaTest.setId(1);
        salaTest.setCodigo("SALAA1");
        salaTest.setCapacidad(100);
        salaTest.setTipoPantalla("IMAX");
        salaTest.setHoraApertura(LocalTime.of(8, 0));
        salaTest.setHoraCierre(LocalTime.of(22, 0));
    }

    @Test
    void testGuardarPeliculaExito() {
        Pelicula peli = new Pelicula();
        peli.setTitulo("Inception");
        peli.setDuracionMinutos(120);
        peli.setHoraProyeccion(LocalDateTime.of(2026, 6, 18, 10, 0));
        peli.setSala(salaTest);

        when(salaRepository.findByCodigo("SALAA1")).thenReturn(Optional.of(salaTest));
        when(peliculaRepository.findBySalaCodigoOrderByHoraProyeccionAsc("SALAA1")).thenReturn(new ArrayList<>());
        when(peliculaRepository.save(peli)).thenReturn(peli);

        Pelicula guardada = peliculaService.guardarPelicula(peli);
        assertNotNull(guardada);
        assertEquals("Inception", guardada.getTitulo());
    }

    @Test
    void testGuardarPeliculaAntesDeApertura() {
        Pelicula peli = new Pelicula();
        peli.setTitulo("Inception");
        peli.setDuracionMinutos(120);
        peli.setHoraProyeccion(LocalDateTime.of(2026, 6, 18, 7, 0)); // Antes de 8:00
        peli.setSala(salaTest);

        when(salaRepository.findByCodigo("SALAA1")).thenReturn(Optional.of(salaTest));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            peliculaService.guardarPelicula(peli);
        });

        assertTrue(ex.getMessage().contains("anterior a la hora de apertura"));
    }

    @Test
    void testGuardarPeliculaExcedeCierre() {
        Pelicula peli = new Pelicula();
        peli.setTitulo("Inception");
        peli.setDuracionMinutos(180); // 3 horas
        peli.setHoraProyeccion(LocalDateTime.of(2026, 6, 18, 20, 0)); // 20:00 + 3h = 23:00 (Excede 22:00)
        peli.setSala(salaTest);

        when(salaRepository.findByCodigo("SALAA1")).thenReturn(Optional.of(salaTest));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            peliculaService.guardarPelicula(peli);
        });

        assertTrue(ex.getMessage().contains("excede la hora de cierre"));
    }

    @Test
    void testGuardarPeliculaSolapamiento() {
        Pelicula existente = new Pelicula();
        existente.setTitulo("Pelicula Existente");
        existente.setDuracionMinutos(120);
        existente.setHoraProyeccion(LocalDateTime.of(2026, 6, 18, 10, 0)); // 10:00 - 12:00
        existente.setSala(salaTest);

        Pelicula nueva = new Pelicula();
        nueva.setTitulo("Inception");
        nueva.setDuracionMinutos(120);
        nueva.setHoraProyeccion(LocalDateTime.of(2026, 6, 18, 11, 0)); // 11:00 - 13:00 (Solapada)
        nueva.setSala(salaTest);

        when(salaRepository.findByCodigo("SALAA1")).thenReturn(Optional.of(salaTest));
        when(peliculaRepository.findBySalaCodigoOrderByHoraProyeccionAsc("SALAA1")).thenReturn(Arrays.asList(existente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            peliculaService.guardarPelicula(nueva);
        });

        assertTrue(ex.getMessage().contains("Conflicto de solapamiento"));
    }

    @Test
    void testCalcularOcupacionEstimada() {
        Pelicula peli1 = new Pelicula();
        peli1.setDuracionMinutos(120);
        
        Pelicula peli2 = new Pelicula();
        peli2.setDuracionMinutos(180);

        salaTest.setPeliculas(Arrays.asList(peli1, peli2)); // Total ocupado: 300 minutos

        // Horas operativas: 8:00 a 22:00 = 14 horas = 840 minutos
        // Porcentaje esperado: 300 * 100 / 840 = 35.714%

        when(salaRepository.findByCodigo("SALAA1")).thenReturn(Optional.of(salaTest));

        Double ocupacion = salaService.calcularOcupacionEstimada("SALAA1");
        assertNotNull(ocupacion);
        assertEquals(35.71, ocupacion, 0.01);
    }
}
