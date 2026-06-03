package uce.edu.pa2.api.infraestructure.repository;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

import jakarta.persistence.Query;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import uce.edu.pa2.api.domain.model.Productos;
import uce.edu.pa2.api.domain.repository.ProductosRepository;
import uce.edu.pa2.api.infraestructure.interceptor.Auditable;

@ApplicationScoped
@Transactional
@Auditable
public class ProductosRepositoryImpl implements ProductosRepository {

    @Inject
    private EntityManager em;

    @Override
    public void crearProducto(Productos productos) {
        
        this.em.merge(productos);
    }

    @Override
    public Productos seleccionar(Integer id) {

        return this.em.find(Productos.class, id);
        
    }

    @Override
    public void actualizar(Productos productos) {
        
        this.em.merge(productos);
    }

    @Override
    public void eliminar(Integer id) {
        
        this.em.remove(id);
    }

    //TYPEDQUERY
    @Override
    public List<Productos> seleccionarProductos() {
       TypedQuery<Productos> query = this.em.createQuery("SELECT p FROM Productos p",
        Productos.class);

        return query.getResultList();
        
    }

    @Override
    public List<Productos> selecionarPorPrecio(Double precio) {
        
        TypedQuery<Productos> query = this.em.createQuery("SELECT p FROM Productos p WHERE p.precio > :precio1",
         Productos.class);

        query.setParameter("precio1", precio);

        return query.getResultList();
    }

    @Override
    public List<Productos> selecionarProductosPorCategoria(String categoria) {
        
        TypedQuery<Productos> query = this.em.createNamedQuery("Productos.buscarPorCategoria", Productos.class);
        query.setParameter("categoria1", categoria);
        return query.getResultList();
    }

    @Override
    public Long contarProductos() {
        
        TypedQuery<Long> query = this.em.createNamedQuery("Productos.contar", Long.class);
        return query.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Productos> seleccionarPorCategoriaNative(String categoria) {
        
        Query query =  this.em.createNativeQuery("SELECT * FROM producto WHERE categoria_producto LIKE :categoria", Productos.class);
        query.setParameter("categoria", "%" + categoria + "%");
        return query.getResultList();

    }

    //CriteriaAPIQuery
    @Override
    public List<Productos> seleccionarDinamica(String nombre, Double preciomax, Double preciomin) {
        
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Productos> query = cb.createQuery(Productos.class);
        Root<Productos> root = query.from(Productos.class);

        List<Predicate> condiciones = new ArrayList<>();

        if(nombre != null){
            Predicate p1 = cb.like(root.get("nombre"), "%"+nombre+"%");
            condiciones.add(p1);
        }

        if(preciomax != null && preciomin != null){
            Predicate p2 = cb.between(root.get("precio"), preciomin, preciomax);
            condiciones.add(p2);
        }
        query.select(root).where(condiciones.toArray(new Predicate[0]));

        TypedQuery<Productos> query1 = this.em.createQuery(query);

        return query1.getResultList();

    }   

}
