package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uce.edu.ec.api.application.service.interceptor.Auditar;
import uce.edu.ec.api.domain.model.Sucursal;
import uce.edu.ec.api.infraestructure.repository.SucursalRepositoryImpl;

@ApplicationScoped
@Transactional
@Auditar
public class SucursalService {

    @Inject
    private SucursalRepositoryImpl sri;

    public void crearSucursal(Sucursal sucursal) {

        if (sucursal == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El cuerpo de la petición no puede estar vacío")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (sucursal.getNombre() == null || sucursal.getNombre().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El nombre de la sucursal es obligatorio")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (sucursal.getCiudad() == null || sucursal.getCiudad().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("La ciudad de la sucursal es obligatoria")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (sucursal.getDireccion() == null || sucursal.getDireccion().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("La dirección de la sucursal es obligatoria")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        this.sri.persist(sucursal);
    }

    public List<Sucursal> buscarTodos() {
        return this.sri.findAll().list();
    }

    public void actualizarSucursal(Sucursal sucursal, Integer id) {

        if (sucursal == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Los datos para actualizar no pueden estar vacíos")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (sucursal.getNombre() != null && sucursal.getNombre().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El nombre no puede ser un texto vacío")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (sucursal.getCiudad() != null && sucursal.getCiudad().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("La ciudad no puede ser un texto vacío")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (sucursal.getDireccion() != null && sucursal.getDireccion().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("La dirección no puede ser un texto vacío")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        Sucursal base = this.buscarSucursalId(id);

        if (sucursal.getNombre() != null) {
            base.setNombre(sucursal.getNombre().trim());
        }
        if (sucursal.getCiudad() != null) {
            base.setCiudad(sucursal.getCiudad().trim());
        }
        if (sucursal.getDireccion() != null) {
            base.setDireccion(sucursal.getDireccion().trim());
        }
    }

    public Sucursal buscarSucursalId(Integer id) {

        if (id == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El ID de la sucursal es obligatorio")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        Sucursal sucursal = this.sri.findById(id);
        if (sucursal == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("No existe una sucursal registrada con el ID: " + id)
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        return sucursal;
    }

    public void eliminarSucursalId(Integer id) {

        this.buscarSucursalId(id);

        this.sri.deleteById(id);
    }

}