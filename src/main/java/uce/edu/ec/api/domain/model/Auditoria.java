package uce.edu.ec.api.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Auditoria {

    @Id
    @SequenceGenerator(name = "seq_auditoria_generador", sequenceName = "seq_auditoria", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_auditoria_generador")
    private Integer id;

    @Column(name = "audi_selects")
    private Integer selects;
    @Column(name = "audi_inserts")
    private Integer inserts;
    @Column(name = "audi_updates")
    private Integer updates;
    @Column(name = "audi_deletes")
    private Integer deletes;
    
    public Auditoria() {
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getSelects() {
        return selects;
    }
    public void setSelects(Integer selects) {
        this.selects = selects;
    }
    public Integer getInserts() {
        return inserts;
    }
    public void setInserts(Integer inserts) {
        this.inserts = inserts;
    }
    public Integer getUpdates() {
        return updates;
    }
    public void setUpdates(Integer updates) {
        this.updates = updates;
    }
    public Integer getDeletes() {
        return deletes;
    }
    public void setDeletes(Integer deletes) {
        this.deletes = deletes;
    }

    
}