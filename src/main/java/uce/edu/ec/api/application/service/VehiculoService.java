package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uce.edu.ec.api.application.service.interceptor.Auditar;
import uce.edu.ec.api.domain.model.EstadoDisponibilidad;
import uce.edu.ec.api.domain.model.Sucursal;
import uce.edu.ec.api.domain.model.Vehiculo;
import uce.edu.ec.api.infraestructure.repository.SucursalRepositoryImpl;
import uce.edu.ec.api.infraestructure.repository.VehiculoRepositoryImpl;

@ApplicationScoped
@Transactional
@Auditar
public class VehiculoService {

    @Inject
    private VehiculoRepositoryImpl vri;

    @Inject
    private SucursalRepositoryImpl sri;

    public void crearVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El cuerpo de la petición no puede estar vacío")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("La placa del vehículo es obligatoria")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        Vehiculo placaExistente = this.vri.find("placa", vehiculo.getPlaca().trim()).firstResult();
        if (placaExistente != null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Ya existe un vehículo registrado con la placa: " + vehiculo.getPlaca())
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (vehiculo.getSucursal() == null || vehiculo.getSucursal().getId() == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El ID de la sucursal es obligatorio")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        Integer sucId = vehiculo.getSucursal().getId();
        Sucursal sucursal = this.sri.findById(sucId);
        if (sucursal == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity("No existe la sucursal indicada con ID: " + sucId)
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        vehiculo.setSucursal(sucursal);

        if (vehiculo.getEstadoDisponibilidad() == null) {
            vehiculo.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
        }

        this.vri.persist(vehiculo);
    }
    
    @Transactional
    public void crearVehiculos(List<Vehiculo> vehiculos) {

        if (vehiculos == null || vehiculos.isEmpty()) {
            throw new WebApplicationException("La lista de vehiculos no puede estar vacía", 400);
        }

        for (Vehiculo vehiculo : vehiculos) {
            crearVehiculo(vehiculo);
        }
    }

    public List<Vehiculo> buscarTodos() {
        return this.vri.findAll().list();
    }

    public void actualizarVehiculo(Vehiculo vehiculo, Integer id) {

        if (vehiculo == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Los datos para actualizar no pueden estar vacíos")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        Vehiculo base = this.buscarVehiculoId(id);

        if (vehiculo.getPlaca() != null && !vehiculo.getPlaca().trim().isEmpty()) {
            if (!vehiculo.getPlaca().equalsIgnoreCase(base.getPlaca())) {
                Vehiculo placaExistente = this.vri.find("placa", vehiculo.getPlaca().trim()).firstResult();
                if (placaExistente != null) {
                    throw new WebApplicationException(
                            Response.status(Response.Status.BAD_REQUEST)
                                    .entity("La placa " + vehiculo.getPlaca() + " ya pertenece a otro vehículo")
                                    .type(MediaType.TEXT_PLAIN)
                                    .build());
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

        if (vehiculo.getEstadoDisponibilidad() != null) {
            base.setEstadoDisponibilidad(vehiculo.getEstadoDisponibilidad());
        }

        if (vehiculo.getSucursal() != null && vehiculo.getSucursal().getId() != null) {
            Integer sucId = vehiculo.getSucursal().getId();
            Sucursal sucursal = this.sri.findById(sucId);
            if (sucursal == null) {
                throw new WebApplicationException(
                        Response.status(Response.Status.NOT_FOUND)
                                .entity("No existe la sucursal con ID: " + sucId)
                                .type(MediaType.TEXT_PLAIN)
                                .build());
            }
            base.setSucursal(sucursal);
        }
    }

    public Vehiculo buscarVehiculoId(Integer id) {

        if (id == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El ID del vehículo es obligatorio")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        Vehiculo vehiculo = this.vri.findById(id);
        if (vehiculo == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity("No existe un vehículo registrado con el ID: " + id)
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        return vehiculo;
    }

    public void eliminarVehiculoId(Integer id) {

        this.buscarVehiculoId(id);

        this.vri.deleteById(id);
    }

    public Vehiculo buscarVehiculoPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("La placa para la búsqueda no puede estar vacía")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        Vehiculo vehiculo = this.vri.find("placa", placa.trim()).firstResult();
        if (vehiculo == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity("No existe un vehículo registrado con la placa: " + placa)
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        return vehiculo;
    }

    public List<Vehiculo> buscarMarcayModelo(String marca, String modelo) {

        if (marca == null || marca.trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("La marca para la búsqueda no puede estar vacía")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (modelo == null || modelo.trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El modelo para la búsqueda no puede estar vacío")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        List<Vehiculo> vehiculos = this.vri.find("marca = ?1 and modelo = ?2", marca.trim(), modelo.trim()).list();

        if (vehiculos.isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity("No existen vehículos registrados con la marca: " + marca + " y el modelo: "
                                    + modelo)
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        return vehiculos;
    }

}