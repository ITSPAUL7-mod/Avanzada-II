package uce.edu.pa2.video;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Priority(1)
public class DescuentoPreventa implements Descuento {   

    @Override
    public double aplicar(Double precio) {
        System.out.println("Aplicando descuento de preventa");
        return precio * 0.9; // Aplica un descuento del 10%
    }

}
