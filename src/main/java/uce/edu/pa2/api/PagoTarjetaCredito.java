package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PagoTarjetaCredito implements PagoEstrategia{

    @Override
    public void realizar(double valor) {

        System.out.println("Presenta la pantalla donde piden los datos de la tarjeta");
        System.out.println("Realizando cobro");
    
    }

}
