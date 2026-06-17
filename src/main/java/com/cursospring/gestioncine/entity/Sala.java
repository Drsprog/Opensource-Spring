package com.cursospring.gestioncine.entity;

import java.time.LocalTime;
import java.util.*;

import jakarta.persistence.*;

@Entity
@Table(name="salas")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String codigo;
    private Integer capacidad;
    private String tipoPantalla;
    private LocalTime horaApertura;
    private LocalTime horaCierre;

    @OneToMany(mappedBy = "sala")
    private List<Pelicula> peliculas;
    
    //Constructores
    public Sala() {
        this.peliculas= new ArrayList<>();
    }

    public Sala(String codigo, Integer capacidad, String tipoPantalla, LocalTime horaApertura, LocalTime horaCierre) {
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.tipoPantalla = tipoPantalla;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
    }

    //Getters and setters
    public Integer getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getTipoPantalla() {
        return tipoPantalla;
    }

    public void setTipoPantalla(String tipoPantalla) {
        this.tipoPantalla = tipoPantalla;
    }

    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura;
    }

    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(LocalTime horaCierre) {
        this.horaCierre = horaCierre;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(List<Pelicula> peliculas) {
        this.peliculas = peliculas;
    } 
    
}
