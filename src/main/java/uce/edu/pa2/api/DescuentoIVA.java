package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DescuentoIVA implements Descuento {

    
    @Override
    public double aplicar(double valor) {
        System.out.println("Aplicando DescuentoIVA");

        double valorADescontar = valor*0.15;
        return valor - valorADescontar;
        //return valor*1.15; 
    }

}
