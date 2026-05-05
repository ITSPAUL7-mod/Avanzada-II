package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
//sin la anotacion @Unremovable
@ApplicationScoped
public class PedidoService1 {

    //!) diferentes formas de pago efectivo y tarjeta
    //2) tipo comprobante va a ser en pdf digital, un papel fisico y un xml  

    //Aqui va la inyeccion de dependencias,
    //es decir, el contenedor de CDI va a inyectar una instancia de NotificadorMail

/* 

    //DI por un constructor, tiene menor acoplamiento
    @Inject
    public PedidoService(NotificadorMail notificadorMail){
        this.notificadorMail = notificadorMail;
    }
*/
 /* 
    //DI por atributo es simple, se oculta, y ademas basico
    @Inject
    private NotificadorMail notificadorMail;

/*
    //DI por metodo, por ser metodos publicos que pueden acceder desde cualquier clase
    private NotificadorMail notificadorMail;
    @Inject
    public void setNotificador(NotificadorMail notificadorMail){
        this.notificadorMail = notificadorMail;

    }
 */

    //Cuando hay una logica es mejor construir una clase
    @Inject
    private NotificadorSelector selector; 
    //PeronCuando no hay una logica en este caso para cobro
    //debe de haber una forma de que se pueda implementar los pagos

    //private PagoEstrategia pago;


     public void registrar(Pedido pedido, PagoEstrategia pago, ComprobanteEstrategia ce){

        System.out.println("Registrando pedido");
        System.out.println("Cliente:" + pedido.getCliente());
        System.out.println("Total: " + pedido.getTotal());
        System.out.println("Guardando en la base de datos");

        pago.realizar(pedido.getTotal());
        
        ce.comprobante(pedido);;
        
        
         //sin DI
        //NotificadorMail n1 = new NotificadorMail();
        
        //notificamos al cliente que se ha creado el pedido
        //Con DI por el contenedor
        Notificador notificador = this.selector.seleccionar(pedido.getTotal());

        notificador.enviar(pedido.getDestino(), "Pedido registrado");

    }

}
