package uce.edu.pa2.video;

import jakarta.enterprise.context.Dependent;

@Dependent
public class GenerarFactura {

    public void generarFactura(String juego, String plataforma, double precio) {
        // Lógica para generar la factura
        System.out.println("Factura generada " + "/nJuego:"+ juego + ": " + plataforma + " - $" + precio);
    }
}
