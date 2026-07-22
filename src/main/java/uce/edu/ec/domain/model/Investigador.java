package uce.edu.ec.domain.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table(name = "investigador")
@Entity
public class Investigador extends PanacheEntityBase{

    @Id
    @SequenceGenerator(name = "seq_investigador_generador", sequenceName = "seq_investigador", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_investigador_generador")
    @Column(name = "inves_id")
    private Integer id;
    @Column(name = "inves_cedula")
    private String cedula;
    @Column(name = "inves_nombre")
    private String nombre;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ficha_medica_id")
    private FichaMedica fichas;

    @ManyToMany(mappedBy = "investigadores")
    @JsonBackReference
    private List<Proyecto> proyectos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public FichaMedica getFichas() {
        return fichas;
    }

    public void setFichas(FichaMedica fichas) {
        this.fichas = fichas;
    }

    



}
