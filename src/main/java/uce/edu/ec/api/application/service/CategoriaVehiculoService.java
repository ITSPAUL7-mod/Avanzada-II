package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import uce.edu.ec.api.domain.model.CategoriaVehiculo;
import uce.edu.ec.api.infraestructure.repository.CategoriaVehiculoRepositoryImpl;

@ApplicationScoped
@Transactional
public class CategoriaVehiculoService {

    @Inject
    private CategoriaVehiculoRepositoryImpl cri;

    public void crearCategoriaVehiculo(CategoriaVehiculo categoriaVehiculo) {

        if (categoriaVehiculo == null) {
            throw new WebApplicationException("El cuerpo de la petición no puede estar vacío", 400);
        }

        if (categoriaVehiculo.getNombre() == null || categoriaVehiculo.getNombre().trim().isEmpty()) {
            throw new WebApplicationException("El nombre de la categoría es obligatorio", 400);
        }

        if (categoriaVehiculo.getPrecioPorDia() == null) {
            throw new WebApplicationException("El precio por día es obligatorio", 400);
        }

        this.cri.persist(categoriaVehiculo);
    }

    public List<CategoriaVehiculo> buscarTodos() {
        return this.cri.findAll().list();
    }

    public void actualizarCategoriaVehiculo(CategoriaVehiculo categoriaVehiculo, Integer id) {
        if (categoriaVehiculo == null) {
            throw new WebApplicationException("Los datos para actualizar no pueden estar vacíos", 400);
        }

        CategoriaVehiculo base = this.buscarCategoriaVehiculoId(id);

        if (categoriaVehiculo.getNombre() != null && !categoriaVehiculo.getNombre().trim().isEmpty()) {
            base.setNombre(categoriaVehiculo.getNombre());
        }

        if (categoriaVehiculo.getPrecioPorDia() != null) {
            base.setPrecioPorDia(categoriaVehiculo.getPrecioPorDia());
        }
    }

    public CategoriaVehiculo buscarCategoriaVehiculoId(Integer id) {
        
        if (id == null) {
            throw new WebApplicationException("El ID de la categoría es obligatorio", 400);
        }

        CategoriaVehiculo categoria = this.cri.findById(id); 
        
        if (categoria == null) {
            throw new WebApplicationException("No existe una categoría de vehículo con el ID: " + id, 404);
        }

        return categoria;
    }

    public void eliminarCategoriaVehiculoId(Integer id) {
        this.buscarCategoriaVehiculoId(id);
        
        this.cri.deleteById(id); 
    }
}