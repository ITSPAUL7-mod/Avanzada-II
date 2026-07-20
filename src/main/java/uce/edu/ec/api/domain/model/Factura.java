package uce.edu.ec.api.domain.model;

import java.time.LocalDate;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table(name = "factura")
@Entity
public class Factura extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "seq_factura_generador", sequenceName = "factura_generador", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_factura_generador")
    @Column(name = "fac_id")
    private Integer id;

    @Column(name = "fac_numero_factura")
    private String numeroFactura;

    @Column(name = "fac_fecha_emision")
    private LocalDate fechaEmision;

    @Column(name = "fac_metodo_pago")
    private String metodoPago;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fac_reserva_id")
    private ReservaVehiculo reservaVehiculo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public ReservaVehiculo getReservaVehiculo() {
        return reservaVehiculo;
    }

    public void setReservaVehiculo(ReservaVehiculo reservaVehiculo) {
        this.reservaVehiculo = reservaVehiculo;
    }

    @Override
    public String toString() {
        return "Factura [id=" + id + ", numeroFactura=" + numeroFactura + ", fechaEmision=" + fechaEmision
                + ", metodoPago=" + metodoPago + "]";
    }

}
