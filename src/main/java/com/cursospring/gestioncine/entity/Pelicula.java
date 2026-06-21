package com.cursospring.gestioncine.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "peliculas")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPelicula")
    private Integer id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "director")
    private String director;

    @Column(name = "duracionMinutos")
    private Integer duracionMinutos;

    @Column(name = "genero")
    private String genero;

    @Column(name = "anioEstreno")
    private Integer anioEstreno;

    @Column(name = "paisOrigen")
    private String paisOrigen;

    @Column(name = "horaProyeccion")
    private LocalDateTime horaProyeccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id")
    private Sala sala;

    public Pelicula() {
    }

    public Pelicula(String titulo, String director, Integer duracionMinutos, String genero, Integer anioEstreno,
            String paisOrigen, LocalDateTime horaProyeccion, Sala sala) {
        this.titulo = titulo;
        this.director = director;
        this.duracionMinutos = duracionMinutos;
        this.genero = genero;
        this.anioEstreno = anioEstreno;
        this.paisOrigen = paisOrigen;
        this.horaProyeccion = horaProyeccion;
        this.sala = sala;
    }

    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAnioEstreno() {
        return anioEstreno;
    }

    public void setAnioEstreno(Integer anioEstreno) {
        this.anioEstreno = anioEstreno;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public LocalDateTime getHoraProyeccion() {
        return horaProyeccion;
    }

    public void setHoraProyeccion(LocalDateTime horaProyeccion) {
        this.horaProyeccion = horaProyeccion;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    @Transient
    public LocalDateTime getHoraFinCalculada() {
        if (this.horaProyeccion == null || this.duracionMinutos == null) {
            return null;
        }
        return this.horaProyeccion.plusMinutes(this.duracionMinutos);
    }
}
