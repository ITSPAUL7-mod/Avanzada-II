package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ComprobantePDF implements ComprobanteEstrategia {

    @Override
    public void comprobante(Pedido pedido) {

       
        System.out.println("Enviando el comprobante pdf al destino" + pedido.getDestino());
        System.out.println("Se envio el comprobante");
    }


}
