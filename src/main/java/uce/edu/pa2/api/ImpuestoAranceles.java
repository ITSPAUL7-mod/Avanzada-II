package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImpuestoAranceles  implements Impuesto {

    @Override
    public double aplicarImpuesto(double valor) {
        System.out.println("Aplicando Impuesto de Aranceles");

        double valorImpuestoAranceles = valor*0.10;
        return valor + valorImpuestoAranceles;
    }

}
