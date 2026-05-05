package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NotificadorSelector {

    @Inject
    private NotificadorMail mail;
    @Inject
    private NotificadorSMS sms;
    @Inject
    private NotificadorWhatsapp whats;
   @Inject
   private ComprobantePDF comP;
   @Inject
   private Factura factura;
    public Notificador seleccionar(double total){

         if(total > 120){
            return mail;
         }else if (total < 50) {
            return whats;
         }
          else {
            return sms;
         }
    }
    //EJEMPLOS DE DONDE SE APLIQUE DESCUENTOS EN UNA VENTA

    public ComprobanteEstrategia comprobante(Pedido pedido){

         if(pedido.getDestino()  == null){
            return factura;
         }   else {
            return comP;
         }
    }
    
}
