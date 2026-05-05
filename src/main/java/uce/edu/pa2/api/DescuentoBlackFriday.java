package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DescuentoBlackFriday implements Descuento {


    @Override
    public double aplicar(double valor) {
        System.out.println("Aplicando Descuento por Black Friday");

        double valorADescontar = valor*0.40;
        return valor - valorADescontar;
        //return valor*1.15; 
    }
}
