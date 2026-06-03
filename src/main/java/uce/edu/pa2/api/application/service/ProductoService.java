package uce.edu.pa2.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import uce.edu.pa2.api.domain.model.Productos;
import uce.edu.pa2.api.domain.repository.ProductosRepository;

@ApplicationScoped
public class ProductoService {

    @Inject
    private ProductosRepository pr;

    public void guardar(Productos producto){
        this.pr.crearProducto(producto);
    }

    public void eliminar(Integer id){

        this.pr.eliminar(id);
    }

    public Productos obtenerProductos(Integer id){

        return this.pr.seleccionar(id);
    
    }

    public void actualizarProducto(Productos productos){

        this.pr.actualizar(productos);

    }

    //Consultas querys
    //TypedQuery
    public List<Productos> obtenerProductos(){
        return this.pr.seleccionarProductos();
    }

    public List<Productos> ObtenerProductosPorPrecio(Double precio){
        return this.pr.selecionarPorPrecio(precio);
    }

    //NamedQuery
    public List<Productos> ObtenerPorCategoria(String categoria){
        return this.pr.selecionarProductosPorCategoria(categoria);
    }

    public Long contarProductos(){
        return this.pr.contarProductos();
    }

    public List<Productos> ObtenerPorCategoriaNative(String categoria){
        return this.pr.seleccionarPorCategoriaNative(categoria);
    }

    //Criteri API Query

    public List<Productos> consultaDinamica(String nombre, Double preciomin, Double preciomax){
        return this.pr.seleccionarDinamica(nombre, preciomax, preciomin);

    }
}
