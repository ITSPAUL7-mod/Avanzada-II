package uce.edu.ec.infraestructure.repository;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import uce.edu.ec.domain.model.Investigador;

@ApplicationScoped
@Transactional
public class InvestigadorRepositoryImpl implements PanacheRepositoryBase<Investigador, Integer>{

    @Inject
    private EntityManager em;

    public List<Investigador> buscarporSanguineo(String sanguineo){

        TypedQuery<Investigador> query = em.createQuery(
            "SELECT i FROM Investigador i WHERE i.fichaMedica.grupoSanguineo :sanguineo",
             Investigador.class
        );

        query.setParameter("sanguineo", sanguineo);

        return query.getResultList();

    }



    
}
