package uce.edu.pa2.api.infraestructure.interceptor;


import java.util.Arrays;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Auditable
@Interceptor
@Priority(1) // Ejecución prioritaria

public class Interceptora {

    @AroundInvoke
    public Object interceptarMetodo(InvocationContext context) throws Exception {
        // 1. Acciones ANTES de que se ejecute el método del repositorio
        String nombreMetodo = context.getMethod().getName();
        String parametros = Arrays.toString(context.getParameters());
        System.out.println("====== [AUDITORÍA INTERCEPTOR] ======");
        System.out.println("-> Ejecutando método: " + nombreMetodo);
        System.out.println("-> Parámetros enviados: " + parametros);
        
        long tiempoInicio = System.currentTimeMillis();

        try {
            // 2. Deja que el método original del repositorio continúe su flujo
            Object resultado = context.proceed(); 
            
            // 3. Acciones DESPUÉS de que el método termina con éxito
            long tiempoFin = System.currentTimeMillis();
            System.out.println("-> Método '" + nombreMetodo + "' finalizó con éxito en " + (tiempoFin - tiempoInicio) + " ms");
            return resultado;

        } catch (Exception e) {
            // Acciones si el método falla (ej. error de base de datos)
            System.out.println("❌ ERROR detectado en método '" + nombreMetodo + "': " + e.getMessage());
            throw e; // No te olvides de volver a lanzar la excepción
        } finally {
            System.out.println("=====================================");
        }
    }
}
