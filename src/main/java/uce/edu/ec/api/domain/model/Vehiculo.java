package uce.edu.ec.api.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table(name = "vehiculo")
@Entity
public class Vehiculo extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "seq_vehiculo_generador", sequenceName = "vehiculo_generador", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_vehiculo_generador")
    @Column(name = "veh_id")
    private Integer id;

    @Column(name = "veh_placa")
    private String placa;

    @Column(name = "veh_marca")
    private String marca;

    @Column(name = "veh_modelo")
    private String modelo;

    @Column(name = "veh_anio")
    private Integer anio;

    @Column(name = "veh_estado_disponibilidad")
    private String estadoDisponibilidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "veh_categoria_id")
    private CategoriaVehiculo categoriaVehiculo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "veh_sucursal_id")
    private Sucursal sucursal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getEstadoDisponibilidad() {
        return estadoDisponibilidad;
    }

    public void setEstadoDisponibilidad(String estadoDisponibilidad) {
        this.estadoDisponibilidad = estadoDisponibilidad;
    }

    public CategoriaVehiculo getCategoriaVehiculo() {
        return categoriaVehiculo;
    }

    public void setCategoriaVehiculo(CategoriaVehiculo categoriaVehiculo) {
        this.categoriaVehiculo = categoriaVehiculo;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    @Override
    public String toString() {
        return "Vehiculo [id=" + id + ", placa=" + placa + ", marca=" + marca + ", modelo=" + modelo + ", anio="
                + anio + ", estadoDisponibilidad=" + estadoDisponibilidad + "]";
    }

}
