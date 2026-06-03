package uce.edu.pa2.api.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name= "producto")
@NamedQuery(name ="Productos.buscarPorCategoria", query = "SELECT p FROM Productos p WHERE p.categoria = :categoria1")
@NamedQuery(name = "Productos.contar", query ="SELECT COUNT(p) FROM Productos p")
public class Productos {

    @Id
    @SequenceGenerator(name = "seq_producto_generador", sequenceName = "seq_producto", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_producto_generador")
    @Column(name = "id_producto")
    private Integer id;

    @Column(name = "nombre_producto")
    private String nombre;
    
    @Column(name = "categoria_producto")
    private String categoria;

    @Column(name = "precio_producto")
    private Double precio;

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Productos [id=" + id + ", nombre=" + nombre + ", categoria=" + categoria + ", precio=" + precio + "]";
    }

}
