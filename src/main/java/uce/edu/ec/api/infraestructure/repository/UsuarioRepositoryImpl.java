package uce.edu.ec.api.infraestructure.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import uce.edu.ec.api.domain.model.Usuario;

@ApplicationScoped
@Transactional
public class UsuarioRepositoryImpl implements PanacheRepositoryBase<Usuario, String> {

}
