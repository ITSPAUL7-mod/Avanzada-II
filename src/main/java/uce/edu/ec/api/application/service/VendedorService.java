package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.ec.api.domain.model.Vendedor;
import uce.edu.ec.api.infraestructure.repository.VendedorRepositoryImpl;

@ApplicationScoped
@Transactional
public class VendedorService {

    @Inject
    private VendedorRepositoryImpl vri;

    public void crearVendedor(Vendedor vendedor) {

        this.vri.persist(vendedor);

    }

    public List<Vendedor> buscarTodos() {
        return this.vri.findAll().list();
    }

    public void actualizarVendedor(Vendedor vendedor, Integer id) {

        Vendedor base = this.buscarVendedorId(id);
        base.setCedulaVendedor(vendedor.getCedulaVendedor());
        base.setNombre(vendedor.getNombre());
        base.setTelefono(vendedor.getTelefono());

    }

    public Vendedor buscarVendedorId(Integer id) {

        return Vendedor.findById(id);
    }

    public void eliminarVendedorId(Integer id) {

        this.vri.deleteById(id);
    }

}
