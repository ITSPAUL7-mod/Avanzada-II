package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.ec.api.domain.model.CategoriaVehiculo;
import uce.edu.ec.api.infraestructure.repository.CategoriaVehiculoRepositoryImpl;

@ApplicationScoped
@Transactional
public class CategoriaVehiculoService {

    @Inject
    private CategoriaVehiculoRepositoryImpl cri;

    public void crearCategoriaVehiculo(CategoriaVehiculo categoriaVehiculo) {

        this.cri.persist(categoriaVehiculo);

    }

    public List<CategoriaVehiculo> buscarTodos() {
        return this.cri.findAll().list();
    }

    public void actualizarCategoriaVehiculo(CategoriaVehiculo categoriaVehiculo, Integer id) {

        CategoriaVehiculo base = this.buscarCategoriaVehiculoId(id);
        base.setNombre(categoriaVehiculo.getNombre());
        base.setPrecioPorDia(categoriaVehiculo.getPrecioPorDia());

    }

    public CategoriaVehiculo buscarCategoriaVehiculoId(Integer id) {

        return CategoriaVehiculo.findById(id);
    }

    public void eliminarCategoriaVehiculoId(Integer id) {

        this.cri.deleteById(id);
    }

}
