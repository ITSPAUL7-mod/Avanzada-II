package uce.edu.pa2.video;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class VideojuegoService {

    @Inject
    private Instance<Descuento> descuentos;

    @Inject
    private Notificador notificador;

    @ValidarVenta
    public void venderJuego(Videojuego videojuego) {
        
        System.out.println("Vendiendo juego" + videojuego.getNombre() + " en la plataforma " + videojuego.getPlataforma()+ " por $" + videojuego.getPrecio());
        
        double precioFinal = videojuego.getPrecio() ;
        // Aplicar descuentos
        for (Descuento descuento : descuentos) {
            
            precioFinal = descuento.aplicar(precioFinal);
        }
        // Generar factura
        System.out.printf("Precio final: %.2f%n", precioFinal);

        // Enviar notificación
        notificador.enviarNotificacion("Venta realizada para " + videojuego.getNombre() + ": " + videojuego.getPlataforma() + " - $" + precioFinal );
    }




}
