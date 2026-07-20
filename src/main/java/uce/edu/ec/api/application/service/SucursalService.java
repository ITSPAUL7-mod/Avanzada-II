package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.ec.api.domain.model.Sucursal;
import uce.edu.ec.api.infraestructure.repository.SucursalRepositoryImpl;

@ApplicationScoped
@Transactional
public class SucursalService {

    @Inject
    private SucursalRepositoryImpl sri;

    public void crearSucursal(Sucursal sucursal) {

        this.sri.persist(sucursal);

    }

    public List<Sucursal> buscarTodos() {
        return this.sri.findAll().list();
    }

    public void actualizarSucursal(Sucursal sucursal, Integer id) {

        Sucursal base = this.buscarSucursalId(id);
        base.setNombre(sucursal.getNombre());
        base.setCiudad(sucursal.getCiudad());
        base.setDireccion(sucursal.getDireccion());

    }

    public Sucursal buscarSucursalId(Integer id) {

        return Sucursal.findById(id);
    }

    public void eliminarSucursalId(Integer id) {

        this.sri.deleteById(id);
    }

}
