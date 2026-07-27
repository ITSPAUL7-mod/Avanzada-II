package uce.edu.ec.api.application.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import uce.edu.ec.api.application.service.interceptor.Auditar;
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
@Auditar
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

        Usuario usuario = this.uri.find("cedula", reserva.getUsuario().getCedula()).firstResult();
        if (usuario == null) {
            throw new WebApplicationException("No existe un usuario registrado con la cédula " + reserva.getUsuario().getCedula(), 404);
        }

        Vendedor vendedor = this.vri.find("cedulaVendedor", reserva.getVendedor().getCedulaVendedor()).firstResult();
        if (vendedor == null) {
            throw new WebApplicationException("No existe un vendedor registrado con la cédula " + reserva.getVendedor().getCedulaVendedor(), 404);
        }

        Vehiculo vehiculo = this.vhri.find("placa", reserva.getVehiculo().getPlaca()).firstResult();
        if (vehiculo == null) {
            throw new WebApplicationException("No existe un vehículo registrado con la placa " + reserva.getVehiculo().getPlaca(), 404);
        }

        if (!"DISPONIBLE".equalsIgnoreCase(vehiculo.getEstadoDisponibilidad())) {
            throw new WebApplicationException("El vehículo con placa " + vehiculo.getPlaca() + " no está disponible", 400);
        }

        if (reserva.getFechaInicio() == null || reserva.getFechaFin() == null || !reserva.getFechaFin().isAfter(reserva.getFechaInicio())) {
            throw new WebApplicationException("La fechaFin debe ser posterior a la fechaInicio", 400);
        }

        long dias = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
        dias = (dias <= 0) ? 1 : dias;

        
        reserva.setUsuario(usuario);
        reserva.setVendedor(vendedor);
        reserva.setVehiculo(vehiculo);
        reserva.setFechaReserva(reserva.getFechaReserva() == null ? LocalDate.now() : reserva.getFechaReserva());
        reserva.setEstado("CONFIRMADA");

        this.rri.persist(reserva);
        vehiculo.setEstadoDisponibilidad("RESERVADO");
    }

    public List<ReservaVehiculo> buscarTodos() {
        return this.rri.findAll().list();
    }

    public void actualizarReservaVehiculo(ReservaVehiculo reserva, Integer id) {
        if (reserva == null) throw new WebApplicationException("Datos vacíos", 400);

        ReservaVehiculo base = this.buscarReservaVehiculoId(id);

        if (reserva.getFechaInicio() != null) base.setFechaInicio(reserva.getFechaInicio());
        if (reserva.getFechaFin() != null) base.setFechaFin(reserva.getFechaFin());
        if (reserva.getEstado() != null) base.setEstado(reserva.getEstado());

        long dias = ChronoUnit.DAYS.between(base.getFechaInicio(), base.getFechaFin());
        dias = (dias <= 0) ? 1 : dias;


        if ("CANCELADA".equalsIgnoreCase(base.getEstado()) || "FINALIZADA".equalsIgnoreCase(base.getEstado())) {
            base.getVehiculo().setEstadoDisponibilidad("DISPONIBLE");
        }
    }

    public ReservaVehiculo buscarReservaVehiculoId(Integer id) {
        ReservaVehiculo reserva = this.rri.findById(id);
        if (reserva == null) throw new WebApplicationException("Reserva no encontrada", 404);
        return reserva;
    }

    public void eliminarReservaVehiculoId(Integer id) {
        ReservaVehiculo base = this.buscarReservaVehiculoId(id);
        if (base.getVehiculo() != null) base.getVehiculo().setEstadoDisponibilidad("DISPONIBLE");
        this.rri.deleteById(id);
    }
}