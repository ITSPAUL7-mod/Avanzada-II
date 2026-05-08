package uce.edu.pa2.video;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Priority(2)
public class DescuentoVIP implements Descuento {

    @Override
    public double aplicar(Double precio) {
        System.out.println("Aplicando descuento VIP");
        return precio * 0.8; // Aplica un descuento del 20%
    }

}
