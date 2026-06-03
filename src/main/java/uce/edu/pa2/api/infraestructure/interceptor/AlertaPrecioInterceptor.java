package uce.edu.pa2.api.infraestructure.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import uce.edu.pa2.api.domain.model.Productos;

@VerificarPrecio
@Interceptor
@Priority(2) // Ejecución prioritaria
public class AlertaPrecioInterceptor {

    @AroundInvoke
    public Object verificar(InvocationContext context) throws Exception {
        // 1. Extraemos los parámetros que se le enviaron al método (ej: crearProducto)
        Object[] parametros = context.getParameters();

        for (Object param : parametros) {
            // Revisa si el parámetro enviado es un Producto
            if (param instanceof Productos) {
                Productos prod = (Productos) param;
                
                // Aplicamos tu condición: precio mayor a 1000
                if (prod.getPrecio() != null && prod.getPrecio() > 1000.0) {
                    System.out.println("\n🚨 [ALERTA DE SEGURIDAD/NEGOCIO] 🚨");
                    System.out.println("⚠️ NOTIFICACIÓN: Se está intentando registrar un producto de ALTO VALOR.");
                    System.out.println("⚠️ Detalle: " + prod.getNombre() + " - Precio: $" + prod.getPrecio());
                    System.out.println("📨 [Mensaje enviado al Administrador de Finanzas]\n");
                    
                    // TIP PARA NOTA EXTRA: Si quisieras denegar el guardado por seguridad, 
                    // podrías lanzar una excepción aquí mismo y el flujo nunca llegaría a la BD:
                    // throw new IllegalArgumentException("No tienes permisos para registrar montos tan altos");
                }
            }
        }

        // 2. Si todo está bien o solo querías enviar el mensaje, dejas continuar el método original
        return context.proceed();
    }
}