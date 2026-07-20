package uce.edu.ec.api.domain.model;

import java.math.BigDecimal;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table(name = "categoria_vehiculo")
@Entity
public class CategoriaVehiculo extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "seq_categoria_generador", sequenceName = "categoria_generador", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_categoria_generador")
    @Column(name = "cat_id")
    private Integer id;

    @Column(name = "cat_nombre")
    private String nombre;

    @Column(name = "cat_precio_por_dia")
    private BigDecimal precioPorDia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecioPorDia() {
        return precioPorDia;
    }

    public void setPrecioPorDia(BigDecimal precioPorDia) {
        this.precioPorDia = precioPorDia;
    }

    @Override
    public String toString() {
        return "CategoriaVehiculo [id=" + id + ", nombre=" + nombre + ", precioPorDia=" + precioPorDia + "]";
    }

}
