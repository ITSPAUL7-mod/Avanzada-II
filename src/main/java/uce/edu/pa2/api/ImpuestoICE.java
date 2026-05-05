package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImpuestoICE  implements Impuesto {

    @Override
    public double aplicarImpuesto(double valor) {

        System.out.println("Aplicando Impuesto ICE");

        double valorImpuestoICE = valor*0.30;
        return valor + valorImpuestoICE;
    }



}
