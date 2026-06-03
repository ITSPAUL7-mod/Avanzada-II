package uce.edu.pa2.api.domain.repository;

import java.util.List;

import uce.edu.pa2.api.domain.model.Productos;

public interface ProductosRepository {

    void crearProducto(Productos productos);

    Productos seleccionar(Integer id);

    void actualizar(Productos productos);

    void eliminar(Integer id);

    List<Productos> seleccionarProductos();

    List<Productos> selecionarPorPrecio(Double precio);

    List<Productos> selecionarProductosPorCategoria(String categoria);

    Long contarProductos();

    List<Productos> seleccionarPorCategoriaNative(String categoria);

    List<Productos> seleccionarDinamica(String nombre, Double preciomax, Double preciomin);

    


}
