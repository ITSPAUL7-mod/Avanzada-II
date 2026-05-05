package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DescuentoSOLCA implements Descuento {

    @Override
    public double aplicar(double valor) {
            System.out.println("Aplicando DescuentoSOLCA");

        double valorADescontar = valor*0.20;
        return valor - valorADescontar;
        //return valor*1.15; 
    }

}
