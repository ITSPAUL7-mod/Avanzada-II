package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
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
            throw new WebApplicationException("El cuerpo de la petición no puede estar vacío", 400);
        }

        if (vendedor.getCedulaVendedor() == null || vendedor.getCedulaVendedor().trim().isEmpty()) {
            throw new WebApplicationException("La cédula del vendedor es obligatoria", 400);
        }

        if (vendedor.getNombre() == null || vendedor.getNombre().trim().isEmpty()) {
            throw new WebApplicationException("El nombre del vendedor es obligatorio", 400);
        }

        if (vendedor.getTelefono() == null || vendedor.getTelefono().trim().isEmpty()) {
            throw new WebApplicationException("El teléfono del vendedor es obligatorio", 400);
        }

        Vendedor existeCedula = this.vri.find("cedulaVendedor", vendedor.getCedulaVendedor().trim()).firstResult();
        if (existeCedula != null) {
            throw new WebApplicationException("Ya existe un vendedor registrado con la cédula: " + vendedor.getCedulaVendedor(), 400);
        }

        this.vri.persist(vendedor);
    }

    public List<Vendedor> buscarTodos() {
        return this.vri.findAll().list();
    }

    public void actualizarVendedor(Vendedor vendedor, Integer id) {

        if (vendedor == null) {
            throw new WebApplicationException("Los datos para actualizar no pueden estar vacíos", 400);
        }

        Vendedor base = this.buscarVendedorId(id);

        if (vendedor.getCedulaVendedor() != null && !vendedor.getCedulaVendedor().trim().isEmpty()) {
            if (!vendedor.getCedulaVendedor().equalsIgnoreCase(base.getCedulaVendedor())) {
                Vendedor existeCedula = this.vri.find("cedulaVendedor", vendedor.getCedulaVendedor().trim()).firstResult();
                if (existeCedula != null) {
                    throw new WebApplicationException("La cédula " + vendedor.getCedulaVendedor() + " ya está asignada a otro vendedor", 400);
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

    public Vendedor buscarVendedorId(Integer id) {

        if (id == null) {
            throw new WebApplicationException("El ID del vendedor es obligatorio", 400);
        }

        Vendedor vendedor = this.vri.findById(id); 
        if (vendedor == null) {
            throw new WebApplicationException("No existe un vendedor registrado con el ID: " + id, 404);
        }

        return vendedor;
    }

    public void eliminarVendedorId(Integer id) {

        this.buscarVendedorId(id);

        this.vri.deleteById(id); 
    }
}