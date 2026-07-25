package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import uce.edu.ec.api.domain.model.Sucursal;
import uce.edu.ec.api.domain.model.Vehiculo;
import uce.edu.ec.api.infraestructure.repository.SucursalRepositoryImpl;
import uce.edu.ec.api.infraestructure.repository.VehiculoRepositoryImpl;

@ApplicationScoped
@Transactional
public class VehiculoService {

    @Inject
    private VehiculoRepositoryImpl vri;

    @Inject
    private SucursalRepositoryImpl sri;

    public void crearVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null) {
            throw new WebApplicationException("El cuerpo de la petición no puede estar vacío", 400);
        }

        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            throw new WebApplicationException("La placa del vehículo es obligatoria", 400);
        }

        Vehiculo placaExistente = this.vri.find("placa", vehiculo.getPlaca().trim()).firstResult();
        if (placaExistente != null) {
            throw new WebApplicationException("Ya existe un vehículo registrado con la placa: " + vehiculo.getPlaca(), 400);
        }

        if (vehiculo.getSucursal() == null || vehiculo.getSucursal().getId() == null) {
            throw new WebApplicationException("El ID de la sucursal es obligatorio", 400);
        }

        Integer sucId = vehiculo.getSucursal().getId();
        Sucursal sucursal = this.sri.findById(sucId); 
        if (sucursal == null) {
            throw new WebApplicationException("No existe la sucursal indicada con ID: " + sucId, 404);
        }

        vehiculo.setSucursal(sucursal);

        if (vehiculo.getEstadoDisponibilidad() == null || vehiculo.getEstadoDisponibilidad().trim().isEmpty()) {
            vehiculo.setEstadoDisponibilidad("DISPONIBLE");
        }

        this.vri.persist(vehiculo);
    }

    public List<Vehiculo> buscarTodos() {
        return this.vri.findAll().list();
    }

    public Vehiculo buscarVehiculoPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new WebApplicationException("La placa para la búsqueda no puede estar vacía", 400);
        }

        Vehiculo vehiculo = this.vri.find("placa", placa.trim()).firstResult();
        if (vehiculo == null) {
            throw new WebApplicationException("No existe un vehículo registrado con la placa: " + placa, 404);
        }

        return vehiculo;
    }

    public void actualizarVehiculo(Vehiculo vehiculo, Integer id) {

        if (vehiculo == null) {
            throw new WebApplicationException("Los datos para actualizar no pueden estar vacíos", 400);
        }

        Vehiculo base = this.buscarVehiculoId(id);

        if (vehiculo.getPlaca() != null && !vehiculo.getPlaca().trim().isEmpty()) {
            if (!vehiculo.getPlaca().equalsIgnoreCase(base.getPlaca())) {
                Vehiculo placaExistente = this.vri.find("placa", vehiculo.getPlaca().trim()).firstResult();
                if (placaExistente != null) {
                    throw new WebApplicationException("La placa " + vehiculo.getPlaca() + " ya pertenece a otro vehículo", 400);
                }
                base.setPlaca(vehiculo.getPlaca());
            }
        }

        if (vehiculo.getMarca() != null && !vehiculo.getMarca().trim().isEmpty()) {
            base.setMarca(vehiculo.getMarca());
        }

        if (vehiculo.getModelo() != null && !vehiculo.getModelo().trim().isEmpty()) {
            base.setModelo(vehiculo.getModelo());
        }

        if (vehiculo.getAnio() != null) {
            base.setAnio(vehiculo.getAnio());
        }


        if (vehiculo.getEstadoDisponibilidad() != null && !vehiculo.getEstadoDisponibilidad().trim().isEmpty()) {
            base.setEstadoDisponibilidad(vehiculo.getEstadoDisponibilidad());
        }

        if (vehiculo.getSucursal() != null && vehiculo.getSucursal().getId() != null) {
            Integer sucId = vehiculo.getSucursal().getId();
            Sucursal sucursal = this.sri.findById(sucId);
            if (sucursal == null) {
                throw new WebApplicationException("No existe la sucursal con ID: " + sucId, 404);
            }
            base.setSucursal(sucursal);
        }
    }

    public Vehiculo buscarVehiculoId(Integer id) {

        if (id == null) {
            throw new WebApplicationException("El ID del vehículo es obligatorio", 400);
        }

        Vehiculo vehiculo = this.vri.findById(id); 
        if (vehiculo == null) {
            throw new WebApplicationException("No existe un vehículo registrado con el ID: " + id, 404);
        }

        return vehiculo;
    }

    public void eliminarVehiculoId(Integer id) {

        this.buscarVehiculoId(id);

        this.vri.deleteById(id); 
    }
}