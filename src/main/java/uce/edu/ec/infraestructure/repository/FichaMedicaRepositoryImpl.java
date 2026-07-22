package uce.edu.ec.infraestructure.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import uce.edu.ec.domain.model.FichaMedica;

@ApplicationScoped
@Transactional
public class FichaMedicaRepositoryImpl implements PanacheRepositoryBase<FichaMedica, Integer> {

}
