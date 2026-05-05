package uce.edu.pa2.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;

@ApplicationScoped 
@Default
//esta lclase la va a getionar el contenedor de CDI,
//  es decir, va a ser un bean gestionado por CDI
    public class NotificadorMail implements Notificador {

     public void enviar(String destino, String mensaje) {
        System.out.println("Se envia un mail al correo: " + destino);
        System.out.println("Con el mensaje: " + mensaje);

    }


    
    }
