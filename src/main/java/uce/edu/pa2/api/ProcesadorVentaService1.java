package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProcesadorVentaService1 {
    @Inject
    private EstadisticasVentasGobales estadisticasVentasGobales;

    //Aqui se injecto el trackingventa se crea un objeto
    @Inject
    private TrakingVenta trakingVenta;

    public void procesar(Venta venta) {
        // Inicia la venta
        trakingVenta.iniciar();
        System.out.println("procesando pedido.....");
        System.out.println("Cliente: "+venta.getCliente());
        // Consultando el stock de cada item
        // Consultando en la base de datos
        // Finaliza la venta
        // voy a iniciar
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.trakingVenta.finalizar();
        //registra estadisticas
        this.estadisticasVentasGobales.registrarVenta(venta.getTotal());
        //un interceptor sin ensuciar la logica de negocio 
    }
    
}
