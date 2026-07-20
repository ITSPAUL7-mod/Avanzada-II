package uce.edu.ec.api.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table(name = "sucursal")
@Entity
public class Sucursal extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "seq_sucursal_generador", sequenceName = "sucursal_generador", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sucursal_generador")
    @Column(name = "suc_id")
    private Integer id;

    @Column(name = "suc_nombre")
    private String nombre;

    @Column(name = "suc_ciudad")
    private String ciudad;

    @Column(name = "suc_direccion")
    private String direccion;

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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Sucursal [id=" + id + ", nombre=" + nombre + ", ciudad=" + ciudad + ", direccion=" + direccion + "]";
    }

}
