package uce.edu.ec.infraestructure.repository;

import java.lang.reflect.Type;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import uce.edu.ec.domain.model.Proyecto;

@ApplicationScoped
@Transactional
public class ProyectoRepositoryImpl implements PanacheRepositoryBase<Proyecto, Integer> {

    @Inject
    private EntityManager em;

    public List<Proyecto> buscaProyectos(String proyecto) {

        TypedQuery<Proyecto> query = em.createQuery(
                "SELECT p FROM Proyecto p WHERE p.nombre LIKE :proyecto ", Proyecto.class);
        query.setParameter("proyecto", "%" + proyecto + "%");

        return query.getResultList();

    }

    public Long contarProyectos() {

        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(p) FROM Proyecto p ", Long.class);

        return query.getSingleResult();
    }

    public List<Proyecto> obtenerTodos(){

        TypedQuery<Proyecto> query = em.createQuery(
            "SELECT p FROM Proyecto p", Proyecto.class);
        return query.getResultList();
    }
}
