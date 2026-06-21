package com.cursospring.gestiongimnasio.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Membresias")
public class Membresia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMembresia")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @Column(name = "tipoPlan")
    private String tipoPlan;

    @Column(name = "fechaInicio")
    private LocalDate fechaInicio;

    @Column(name = "fechaVencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "precioMensual")
    private BigDecimal precioMensual;

    @Column(name = "estadoMemmbresia")
    private String estadoMembresia;

    @Column(name = "metodoPago")
    private String metodoPago;

    @Column(name = "diasCongelado")
    private Integer diasCongelados=0;

    public Membresia() {
    }

    public Membresia(Cliente cliente, String tipoPlan, LocalDate fechaInicio, LocalDate fechaVencimiento,
            BigDecimal precioMensual, String estadoMembresia, String metodoPago, Integer diasCongelados) {
        this.cliente = cliente;
        this.tipoPlan = tipoPlan;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.precioMensual = precioMensual;
        this.estadoMembresia = estadoMembresia;
        this.metodoPago = metodoPago;
        this.diasCongelados = diasCongelados;
    }

    public Integer getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTipoPlan() {
        return tipoPlan;
    }

    public void setTipoPlan(String tipoPlan) {
        this.tipoPlan = tipoPlan;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getPrecioMensual() {
        return precioMensual;
    }

    public void setPrecioMensual(BigDecimal precioMensual) {
        this.precioMensual = precioMensual;
    }

    public String getEstadoMembresia() {
        return estadoMembresia;
    }

    public void setEstadoMembresia(String estadoMembresia) {
        this.estadoMembresia = estadoMembresia;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Integer getDiasCongelados() {
        return diasCongelados;
    }

    public void setDiasCongelados(Integer diasCongelados) {
        this.diasCongelados = diasCongelados;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Membresia membresia = (Membresia) o;
        return Objects.equals(id, membresia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Membresia{" +
                "id=" + id +
                ", cliente=" + (cliente != null ? cliente.getId() : "null") +
                ", tipoPlan='" + tipoPlan + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaVencimiento=" + fechaVencimiento +
                ", precioMensual=" + precioMensual +
                ", estadoMembresia='" + estadoMembresia + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                ", diasCongelados=" + diasCongelados +
                '}';
    }
}
