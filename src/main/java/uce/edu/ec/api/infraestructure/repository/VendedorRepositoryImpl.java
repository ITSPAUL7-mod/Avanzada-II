package uce.edu.ec.api.infraestructure.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import uce.edu.ec.api.domain.model.Vendedor;

@ApplicationScoped
@Transactional
public class VendedorRepositoryImpl implements PanacheRepositoryBase<Vendedor, String> {

}
