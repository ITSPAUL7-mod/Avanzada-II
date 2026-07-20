package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import uce.edu.ec.api.domain.model.CategoriaVehiculo;
import uce.edu.ec.api.domain.model.Sucursal;
import uce.edu.ec.api.domain.model.Vehiculo;
import uce.edu.ec.api.infraestructure.repository.VehiculoRepositoryImpl;

@ApplicationScoped
@Transactional
public class VehiculoService {

    @Inject
    private VehiculoRepositoryImpl vri;

    public void crearVehiculo(Vehiculo vehiculo) {

        CategoriaVehiculo categoria = CategoriaVehiculo.findById(vehiculo.getCategoriaVehiculo().getId());
        if (categoria == null) {
            throw new WebApplicationException("No existe la categoria de vehiculo indicada", 404);
        }

        Sucursal sucursal = Sucursal.findById(vehiculo.getSucursal().getId());
        if (sucursal == null) {
            throw new WebApplicationException("No existe la sucursal indicada", 404);
        }

        vehiculo.setCategoriaVehiculo(categoria);
        vehiculo.setSucursal(sucursal);

        if (vehiculo.getEstadoDisponibilidad() == null) {
            vehiculo.setEstadoDisponibilidad("DISPONIBLE");
        }

        this.vri.persist(vehiculo);

    }

    public List<Vehiculo> buscarTodos() {
        return this.vri.findAll().list();
    }

    public Vehiculo buscarVehiculoPlaca(String placa) {

        return this.vri.find("placa", placa).firstResult();
    }

    public void actualizarVehiculo(Vehiculo vehiculo, Integer id) {

        Vehiculo base = this.buscarVehiculoId(id);
        base.setPlaca(vehiculo.getPlaca());
        base.setMarca(vehiculo.getMarca());
        base.setModelo(vehiculo.getModelo());
        base.setAnio(vehiculo.getAnio());
        base.setEstadoDisponibilidad(vehiculo.getEstadoDisponibilidad());

        if (vehiculo.getCategoriaVehiculo() != null && vehiculo.getCategoriaVehiculo().getId() != null) {
            base.setCategoriaVehiculo(CategoriaVehiculo.findById(vehiculo.getCategoriaVehiculo().getId()));
        }
        if (vehiculo.getSucursal() != null && vehiculo.getSucursal().getId() != null) {
            base.setSucursal(Sucursal.findById(vehiculo.getSucursal().getId()));
        }

    }

    public Vehiculo buscarVehiculoId(Integer id) {

        return Vehiculo.findById(id);
    }

    public void eliminarVehiculoId(Integer id) {

        this.vri.deleteById(id);
    }

}
