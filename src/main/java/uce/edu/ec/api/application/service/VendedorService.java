package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uce.edu.ec.api.application.service.interceptor.Auditar;
import uce.edu.ec.api.domain.model.Vendedor;
import uce.edu.ec.api.infraestructure.repository.VendedorRepositoryImpl;

@ApplicationScoped
@Transactional
@Auditar
public class VendedorService {

    @Inject
    private VendedorRepositoryImpl vri;

    public void crearVendedor(Vendedor vendedor) {

        if (vendedor == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El cuerpo de la petición no puede estar vacío")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (vendedor.getCedulaVendedor() == null || vendedor.getCedulaVendedor().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("La cédula del vendedor es obligatoria")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (vendedor.getNombre() == null || vendedor.getNombre().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El nombre del vendedor es obligatorio")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (vendedor.getTelefono() == null || vendedor.getTelefono().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El teléfono del vendedor es obligatorio")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        Vendedor existeCedula = this.vri.find("cedulaVendedor", vendedor.getCedulaVendedor().trim()).firstResult();
        if (existeCedula != null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Ya existe un vendedor registrado con la cédula: " + vendedor.getCedulaVendedor())
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        this.vri.persist(vendedor);
    }

    public List<Vendedor> buscarTodos() {
        return this.vri.findAll().list();
    }

    public void actualizarVendedor(Vendedor vendedor, String cedula) {

        if (vendedor == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Los datos para actualizar no pueden estar vacíos")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        Vendedor base = this.buscarPorCedula(cedula);

        if (vendedor.getCedulaVendedor() != null && !vendedor.getCedulaVendedor().trim().isEmpty()) {
            if (!vendedor.getCedulaVendedor().equalsIgnoreCase(base.getCedulaVendedor())) {
                Vendedor existeCedula = this.vri.find("cedulaVendedor", vendedor.getCedulaVendedor().trim()).firstResult();
                if (existeCedula != null) {
                    throw new WebApplicationException(
                            Response.status(Response.Status.BAD_REQUEST)
                                    .entity("La cédula " + vendedor.getCedulaVendedor() + " ya está asignada a otro vendedor")
                                    .type(MediaType.TEXT_PLAIN)
                                    .build());
                }
                base.setCedulaVendedor(vendedor.getCedulaVendedor());
            }
        }

        if (vendedor.getNombre() != null && !vendedor.getNombre().trim().isEmpty()) {
            base.setNombre(vendedor.getNombre());
        }

        if (vendedor.getTelefono() != null && !vendedor.getTelefono().trim().isEmpty()) {
            base.setTelefono(vendedor.getTelefono());
        }
    }

   

//-----------------------------------------------
    public void eliminarPorCedula(String cedula) {
        this.buscarPorCedula(cedula);
        this.vri.delete("cedula", cedula); 
    }

    public Vendedor buscarPorCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("La cédula del vendedor es obligatoria")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        Vendedor vendedor = this.vri.find("cedulaVendedor", cedula.trim()).firstResult();
        if (vendedor == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity("No existe un vendedor registrado con la cédula: " + cedula)
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        return vendedor;
    }
}