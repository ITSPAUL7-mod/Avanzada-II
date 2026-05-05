package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImpuestoIR  implements Impuesto {

    @Override
    public double aplicarImpuesto(double valor) {
        System.out.println("Aplicando Impuesto a la Renta");

        double valorImpuestoIR = valor*0.20;
        return valor + valorImpuestoIR;
    }

}
