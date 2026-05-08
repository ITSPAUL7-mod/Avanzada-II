    package uce.edu.pa2.video;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@ValidarVenta
@Priority(1)
public class ValidarJuegoService {

    @AroundInvoke
    public Object validarJuego(InvocationContext context) throws Exception {
        // Lógica para validar el juego antes de la venta
        System.out.println("Validando venta del juego: " + context.getMethod().getName());

        Object [] args  = context.getParameters();

         for(int i = 0; i < args.length; i++){
            
            Object obj = args[i];
            Videojuego video= (Videojuego) obj;
            System.out.println("Nombre del Videojuego:" + video.getNombre());
            System.out.println("Precio del Videojuego:" + video.getPrecio());
            System.out.println("Plataforma del Videojuego:" + video.getPlataforma());
        }
        Object resultado = context.proceed();

        System.out.println("Juego validado exitosamente");  


        return resultado; // Retorna el juego validado (puede lanzar una excepción si no es válido)
    }
}
