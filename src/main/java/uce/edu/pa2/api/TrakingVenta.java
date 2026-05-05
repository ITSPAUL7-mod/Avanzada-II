package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;

@Dependent
public class TrakingVenta {
    private long tiempoInicio = 0;
    private long tiempoFinal = 0;
    private long tiempoTotalAcumulado = 0;
    private int numeroOperaciones = 0;

    public void iniciar() {
        this.tiempoInicio = System.currentTimeMillis();
    }

    public void reiniciar(){
        this.tiempoTotalAcumulado =0;
        this.numeroOperaciones = 0;
    }

    public void finalizar() {
        this.tiempoFinal = System.currentTimeMillis();

        long tiempoEjecucion = tiempoFinal - tiempoInicio;

        this.tiempoTotalAcumulado += tiempoEjecucion;
        this.numeroOperaciones++;
        System.out.println("Tiempo de ejecucion:" + tiempoEjecucion+ " ms");
        System.out.println("Tiempo Acumulado;" + tiempoTotalAcumulado + "ms");
        System.out.println("Cantidad de operaciones: "+ this.numeroOperaciones  );
    }
}
