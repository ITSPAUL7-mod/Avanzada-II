package uce.edu.pa2.video;

import jakarta.inject.Singleton;

@Singleton
public class Notificador {

        public void enviarNotificacion(String mensaje) {
            // Lógica para enviar una notificación (por ejemplo, por correo electrónico)
            System.out.println("Notificación enviada: " + mensaje);
        }
        
}
