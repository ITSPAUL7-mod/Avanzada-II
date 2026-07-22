package uce.edu.ec.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table(name = "ficha_medica")
@Entity
public class FichaMedica extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "seq_ficha_generador", sequenceName = "seq_ficha", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ficha_generador")
    @Column(name = "fi_id")
    private Integer id;
    @Column(name = "fi_sanguineo")
    private String sanguineo;
    @Column(name = "fi_alergias")

    
    private String alergias;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getSanguineo() {
        return sanguineo;
    }
    public void setSanguineo(String sanguineo) {
        this.sanguineo = sanguineo;
    }
    public String getAlergias() {
        return alergias;
    }
    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    

}
