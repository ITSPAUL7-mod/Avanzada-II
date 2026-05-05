package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PagoEfectivo implements PagoEstrategia{

    @Override
    public void realizar(double valor) {

        System.out.println("Presenta una pantalla Efectivo");
        System.out.println("Realizando Cobro");

    }

}
