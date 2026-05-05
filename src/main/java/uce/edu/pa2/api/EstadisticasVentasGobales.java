package uce.edu.pa2.api;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Singleton;

@ApplicationScoped
public class EstadisticasVentasGobales {

    private int totalVentas=0;
    private double montoTotalVendido =0;
    
    public void registrarVenta(double totalVentaIndividual){
        totalVentas++;
        montoTotalVendido += totalVentaIndividual;
    }

    public void mostrarEstadisticasGlobales(){
        System.out.println("ESTADISTICAS GLOBALES");
        System.out.println("Cantidad de ventas: "+ this.totalVentas);
        System.out.println("Valor Total Vendido: "+ this.montoTotalVendido);


    }
}
