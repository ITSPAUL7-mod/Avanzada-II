package uce.edu.ec.api.application.service;

import java.time.LocalDate;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import uce.edu.ec.api.domain.model.Factura;
import uce.edu.ec.api.domain.model.ReservaVehiculo;
import uce.edu.ec.api.infraestructure.repository.FacturaRepositoryImpl;

@ApplicationScoped
@Transactional
public class FacturaService {

    @Inject
    private FacturaRepositoryImpl fri;

    public void crearFactura(Factura factura) {

        Integer reservaId = factura.getReservaVehiculo().getId();

        ReservaVehiculo reserva = ReservaVehiculo.findById(reservaId);
        if (reserva == null) {
            throw new WebApplicationException("No existe una reserva con id " + reservaId, 404);
        }

        Factura existente = this.fri.find("reservaVehiculo.id", reservaId).firstResult();
        if (existente != null) {
            throw new WebApplicationException("La reserva con id " + reservaId + " ya cuenta con una factura", 400);
        }

        factura.setReservaVehiculo(reserva);
        if (factura.getFechaEmision() == null) {
            factura.setFechaEmision(LocalDate.now());
        }

        this.fri.persist(factura);

    }

    public List<Factura> buscarTodos() {
        return this.fri.findAll().list();
    }

    public void actualizarFactura(Factura factura, Integer id) {

        Factura base = this.buscarFacturaId(id);
        base.setNumeroFactura(factura.getNumeroFactura());
        base.setFechaEmision(factura.getFechaEmision());
        base.setMetodoPago(factura.getMetodoPago());

    }

    public Factura buscarFacturaId(Integer id) {

        return Factura.findById(id);
    }

    public void eliminarFacturaId(Integer id) {

        this.fri.deleteById(id);
    }

}
