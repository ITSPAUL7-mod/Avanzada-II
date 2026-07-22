package uce.edu.ec.domain.model;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table(name = "proyecto")
@Entity
public class Proyecto extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "seq_proyecto_generador", sequenceName = "seq_proyecto", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proyecto_generador")
    @Column(name = "pro_id")
    private Integer id;
    @Column(name = "pro_codigo")
    private String codigo;
    @Column(name = "pro_nombre")
    private String nombre;
    @Column(name = "pro_presupuesto")
    private Double presupuesto;

    public Double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Double presupuesto) {
        this.presupuesto = presupuesto;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "proyecto_investigador", joinColumns = @JoinColumn(name = "proyecto_id"), inverseJoinColumns = @JoinColumn(name = "investigador_id"))
    @JsonBackReference
    private List<Investigador> investigadores;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Investigador> getInvestigadores() {
        return investigadores;
    }

    public void setInvestigadores(List<Investigador> investigadores) {
        this.investigadores = investigadores;
    }

}
