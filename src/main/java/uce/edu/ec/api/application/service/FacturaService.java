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
import uce.edu.ec.api.infraestructure.repository.ReservaVehiculoRepositoryImpl;

@ApplicationScoped
@Transactional
public class FacturaService {

    @Inject
    private FacturaRepositoryImpl fri;

    @Inject
    private ReservaVehiculoRepositoryImpl rri;

    public void crearFactura(Factura factura) {

        if (factura == null) {
            throw new WebApplicationException("El cuerpo de la petición no puede estar vacío", 400);
        }

        if (factura.getReservaVehiculo() == null || factura.getReservaVehiculo().getId() == null) {
            throw new WebApplicationException("El ID de la reserva de vehículo es obligatorio", 400);
        }

        Integer reservaId = factura.getReservaVehiculo().getId();

        ReservaVehiculo reserva = this.rri.findById(reservaId);
        if (reserva == null) {
            throw new WebApplicationException("No existe una reserva registrada con el ID: " + reservaId, 404);
        }

        Factura existente = this.fri.find("reservaVehiculo.id", reservaId).firstResult();
        if (existente != null) {
            throw new WebApplicationException("La reserva con ID " + reservaId + " ya cuenta con una factura asignada",
                    400);
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

        if (factura == null) {
            throw new WebApplicationException("Los datos para actualizar no pueden estar vacíos", 400);
        }

        Factura base = this.buscarFacturaId(id);

        if (factura.getNumeroFactura() != null && !factura.getNumeroFactura().trim().isEmpty()) {
            base.setNumeroFactura(factura.getNumeroFactura());
        }

        if (factura.getFechaEmision() != null) {
            base.setFechaEmision(factura.getFechaEmision());
        }

        if (factura.getMetodoPago() != null && !factura.getMetodoPago().trim().isEmpty()) {
            base.setMetodoPago(factura.getMetodoPago());
        }
    }

    public Factura buscarFacturaId(Integer id) {

        if (id == null) {
            throw new WebApplicationException("El ID de la factura es obligatorio", 400);
        }

        Factura factura = this.fri.findById(id);
        if (factura == null) {
            throw new WebApplicationException("No existe una factura registrada con el ID: " + id, 404);
        }

        return factura;
    }

    public void eliminarFacturaId(Integer id) {

        this.buscarFacturaId(id);

        this.fri.deleteById(id); 
    }
}