package uce.edu.ec.api.infraestructure.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import uce.edu.ec.api.domain.model.CategoriaVehiculo;

@ApplicationScoped
@Transactional
public class CategoriaVehiculoRepositoryImpl implements PanacheRepositoryBase<CategoriaVehiculo, Integer> {

}
