package uce.edu.ec.api.infraestructure.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import uce.edu.ec.api.domain.model.Sucursal;

@ApplicationScoped
@Transactional
public class SucursalRepositoryImpl implements PanacheRepositoryBase<Sucursal, Integer> {

}
