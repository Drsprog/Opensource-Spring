package com.cursospring.gestiongimnasio.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente")
    private Integer id;

    @Column(name = "dni")
    private String dni;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos") 
    private String apellidos;

    @Column(name = "fechaNacimiento")
    private LocalDate fechaNaciemiento;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "fechaRegistro")
    private LocalDate fechaRegistro;

    @Column(name = "estado")
    private String estado;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL , orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Membresia> membresias;

    public Cliente() {
    }

    public Cliente(String dni, String nombres, String apellidos, LocalDate fechaNaciemiento, String telefono,
            String email, LocalDate fechaRegistro, String estado) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNaciemiento = fechaNaciemiento;
        this.telefono = telefono;
        this.email = email;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getFechaNaciemiento() {
        return fechaNaciemiento;
    }

    public void setFechaNaciemiento(LocalDate fechaNaciemiento) {
        this.fechaNaciemiento = fechaNaciemiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", fechaNaciemiento=" + fechaNaciemiento +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", estado='" + estado + '\'' +
                '}';
    }
}
