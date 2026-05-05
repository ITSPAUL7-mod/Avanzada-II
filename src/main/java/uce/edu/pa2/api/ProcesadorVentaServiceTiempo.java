package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProcesadorVentaServiceTiempo {
    @Inject
    private EstadisticasVentasGobales estadisticasVentasGobales;

    @MedirTiempo
    @log
    public void procesar(Venta venta) {
        
        System.out.println("Entro al metodo con los siguientes valores: " + venta.getCliente());
        // Inicia la venta
        System.out.println("procesando pedido.....");
        System.out.println("Cliente: "+ venta.getCliente());
        // Consultando el stock de cada item
        // Consultando en la base de datos
        // Finaliza la venta
        // voy a iniciar
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //registra estadisticas
        this.estadisticasVentasGobales.registrarVenta(venta.getTotal());
        //un interceptor sin ensuciar la logica de negocio 

        System.out.println("Se ejecuto el pedido");
        //this.REprocesar(venta);
        //ESTO NO SE VA A EJECUTAR 
        //EL INTERCEPTOR DEL METODO POR QUE ES UNA LLAMADA INTERNA
    }   

    @MedirTiempo
    public void REprocesar(Venta venta) {
        
        // Inicia la venta
        System.out.println("RE-procesando pedido.....");
        System.out.println("Cliente: "+venta.getCliente());
        // Consultando el stock de cada item
        // Consultando en la base de datos
        // Finaliza la venta
        // voy a iniciar
        
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //registra estadisticas
        this.estadisticasVentasGobales.registrarVenta(venta.getTotal());
        //un interceptor sin ensuciar la logica de negocio 
        
        System.out.println("Se ejecuto el RE-proceso del pedido");
    
    }

    
}
