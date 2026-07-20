package uce.edu.ec.api.application.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import uce.edu.ec.api.domain.model.ReservaVehiculo;
import uce.edu.ec.api.domain.model.Usuario;
import uce.edu.ec.api.domain.model.Vehiculo;
import uce.edu.ec.api.domain.model.Vendedor;
import uce.edu.ec.api.infraestructure.repository.ReservaVehiculoRepositoryImpl;
import uce.edu.ec.api.infraestructure.repository.UsuarioRepositoryImpl;
import uce.edu.ec.api.infraestructure.repository.VehiculoRepositoryImpl;
import uce.edu.ec.api.infraestructure.repository.VendedorRepositoryImpl;

@ApplicationScoped
@Transactional
public class ReservaVehiculoService {

    @Inject
    private ReservaVehiculoRepositoryImpl rri;

    @Inject
    private UsuarioRepositoryImpl uri;

    @Inject
    private VendedorRepositoryImpl vri;

    @Inject
    private VehiculoRepositoryImpl vhri;

    public void crearReservaVehiculo(ReservaVehiculo reserva) {

        if (reserva == null) {
            throw new WebApplicationException("El cuerpo de la petición no puede estar vacío", 400);
        }
        if (reserva.getUsuario() == null || reserva.getUsuario().getCedula() == null) {
            throw new WebApplicationException("La cédula del usuario es obligatoria", 400);
        }
        if (reserva.getVendedor() == null || reserva.getVendedor().getCedulaVendedor() == null) {
            throw new WebApplicationException("La cédula del vendedor es obligatoria", 400);
        }
        if (reserva.getVehiculo() == null || reserva.getVehiculo().getPlaca() == null) {
            throw new WebApplicationException("La placa del vehículo es obligatoria", 400);
        }

        String cedula = reserva.getUsuario().getCedula();
        String cedulaVendedor = reserva.getVendedor().getCedulaVendedor();
        String placa = reserva.getVehiculo().getPlaca();

        Usuario usuario = this.uri.find("cedula", cedula).firstResult();
        if (usuario == null) {
            throw new WebApplicationException("No existe un usuario registrado con la cedula " + cedula, 404);
        }

        Vendedor vendedor = this.vri.find("cedulaVendedor", cedulaVendedor).firstResult();
        if (vendedor == null) {
            throw new WebApplicationException(
                    "No existe un vendedor registrado con la cedula " + cedulaVendedor, 404);
        }

        Vehiculo vehiculo = this.vhri.find("placa", placa).firstResult();
        if (vehiculo == null) {
            throw new WebApplicationException("No existe un vehiculo registrado con la placa " + placa, 404);
        }

        if (!"DISPONIBLE".equalsIgnoreCase(vehiculo.getEstadoDisponibilidad())) {
            throw new WebApplicationException(
                    "El vehiculo con placa " + placa + " no se encuentra disponible para reservar", 400);
        }

        if (reserva.getFechaInicio() == null || reserva.getFechaFin() == null
                || !reserva.getFechaFin().isAfter(reserva.getFechaInicio())) {
            throw new WebApplicationException("La fechaFin debe ser posterior a la fechaInicio", 400);
        }

        long dias = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
        if (dias <= 0) {
            dias = 1;
        }

        if (vehiculo.getCategoriaVehiculo() == null || vehiculo.getCategoriaVehiculo().getPrecioPorDia() == null) {
            throw new WebApplicationException("El vehículo no tiene asignado un precio por día válido", 400);
        }

        BigDecimal total = vehiculo.getCategoriaVehiculo().getPrecioPorDia().multiply(BigDecimal.valueOf(dias));

        reserva.setUsuario(usuario);
        reserva.setVendedor(vendedor);
        reserva.setVehiculo(vehiculo);
        if (reserva.getFechaReserva() == null) {
            reserva.setFechaReserva(LocalDate.now());
        }
        reserva.setEstado("CONFIRMADA");
        reserva.setTotal(total);

        this.rri.persist(reserva);

        vehiculo.setEstadoDisponibilidad("RESERVADO");
    }

    public List<ReservaVehiculo> buscarTodos() {
        return this.rri.findAll().list();
    }

    public void actualizarReservaVehiculo(ReservaVehiculo reserva, Integer id) {

        if (reserva == null) {
            throw new WebApplicationException("Los datos para actualizar no pueden estar vacíos", 400);
        }

        ReservaVehiculo base = this.buscarReservaVehiculoId(id);

        if (reserva.getFechaInicio() != null) {
            base.setFechaInicio(reserva.getFechaInicio());
        }
        if (reserva.getFechaFin() != null) {
            base.setFechaFin(reserva.getFechaFin());
        }
        if (reserva.getEstado() != null) {
            base.setEstado(reserva.getEstado());
        }

        if (!base.getFechaFin().isAfter(base.getFechaInicio())) {
            throw new WebApplicationException("La fechaFin debe ser posterior a la fechaInicio", 400);
        }

        long dias = ChronoUnit.DAYS.between(base.getFechaInicio(), base.getFechaFin());
        if (dias <= 0) {
            dias = 1;
        }

        if (base.getVehiculo() == null || base.getVehiculo().getCategoriaVehiculo() == null
                || base.getVehiculo().getCategoriaVehiculo().getPrecioPorDia() == null) {
            throw new WebApplicationException("Error al calcular el total: Categoría de vehículo no encontrada", 400);
        }

        BigDecimal total = base.getVehiculo().getCategoriaVehiculo().getPrecioPorDia()
                .multiply(BigDecimal.valueOf(dias));
        base.setTotal(total);

        if ("CANCELADA".equalsIgnoreCase(base.getEstado()) || "FINALIZADA".equalsIgnoreCase(base.getEstado())) {
            base.getVehiculo().setEstadoDisponibilidad("DISPONIBLE");
        }
    }

    public ReservaVehiculo buscarReservaVehiculoId(Integer id) {
        if (id == null) {
            throw new WebApplicationException("El ID de la reserva es obligatorio", 400);
        }

        ReservaVehiculo reserva = this.rri.findById(id);
        if (reserva == null) {
            throw new WebApplicationException("No existe una reserva registrada con el ID: " + id, 404);
        }

        return reserva;
    }

    public void eliminarReservaVehiculoId(Integer id) {
        ReservaVehiculo base = this.buscarReservaVehiculoId(id);
        if (base.getVehiculo() != null) {
            base.getVehiculo().setEstadoDisponibilidad("DISPONIBLE");
        }
        this.rri.deleteById(id);
    }
}