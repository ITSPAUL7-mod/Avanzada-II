package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.interceptor.InvocationContext;

@ApplicationScoped
public class InventarioService {
//un nuevo bena un nuevo metodo registrar inventario 
//simulen que se demora 500s y con el inteceptor lo intercepte

    @Inject
    private EstadisticasVentasGobales estadisticasVentasGobales;

    @MedirTiempo
    public void Registrar(Pedido pedido) {
        
        System.out.println("Registrando Inventario...");
        // Inicia la venta
        System.out.println("Producto: "+ pedido.getProducto());
        // Consultando el stock de cada item
        // Consultando en la base de datos
        // Finaliza la venta
        // voy a iniciar
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //registra estadisticas
        this.estadisticasVentasGobales.registrarVenta(pedido.getTotal());
        //un interceptor sin ensuciar la logica de negocio 

        System.out.println("Se añadio al inventario");

        
        

    }
}
