package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import uce.edu.ec.api.domain.model.Sucursal;
import uce.edu.ec.api.infraestructure.repository.SucursalRepositoryImpl;

@ApplicationScoped
@Transactional
public class SucursalService {

    @Inject
    private SucursalRepositoryImpl sri;

    public void crearSucursal(Sucursal sucursal) {

        if (sucursal == null) {
            throw new WebApplicationException("El cuerpo de la petición no puede estar vacío", 400);
        }

        if (sucursal.getNombre() == null || sucursal.getNombre().trim().isEmpty()) {
            throw new WebApplicationException("El nombre de la sucursal es obligatorio", 400);
        }

        if (sucursal.getCiudad() == null || sucursal.getCiudad().trim().isEmpty()) {
            throw new WebApplicationException("La ciudad de la sucursal es obligatoria", 400);
        }

        if (sucursal.getDireccion() == null || sucursal.getDireccion().trim().isEmpty()) {
            throw new WebApplicationException("La dirección de la sucursal es obligatoria", 400);
        }

        this.sri.persist(sucursal);
    }

    public List<Sucursal> buscarTodos() {
        return this.sri.findAll().list();
    }

    public void actualizarSucursal(Sucursal sucursal, Integer id) {

        if (sucursal == null) {
            throw new WebApplicationException("Los datos para actualizar no pueden estar vacíos", 400);
        }

        Sucursal base = this.buscarSucursalId(id);

        if (sucursal.getNombre() != null && !sucursal.getNombre().trim().isEmpty()) {
            base.setNombre(sucursal.getNombre());
        }

        if (sucursal.getCiudad() != null && !sucursal.getCiudad().trim().isEmpty()) {
            base.setCiudad(sucursal.getCiudad());
        }

        if (sucursal.getDireccion() != null && !sucursal.getDireccion().trim().isEmpty()) {
            base.setDireccion(sucursal.getDireccion());
        }
    }

    public Sucursal buscarSucursalId(Integer id) {

        if (id == null) {
            throw new WebApplicationException("El ID de la sucursal es obligatorio", 400);
        }

        Sucursal sucursal = this.sri.findById(id);

        if (sucursal == null) {
            throw new WebApplicationException("No existe una sucursal registrada con el ID: " + id, 404);
        }

        return sucursal;
    }

    public void eliminarSucursalId(Integer id) {

        this.buscarSucursalId(id);

        this.sri.deleteById(id);
    }

}