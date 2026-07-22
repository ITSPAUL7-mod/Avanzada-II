package uce.edu.ec.application.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@tiempo
@Interceptor
public class TiempoEjecucionInterceptor {

    @AroundInvoke
    public Object medirtiempo(InvocationContext context ) throws Exception{

        long inicio = System.currentTimeMillis();
        String metodo = context.getMethod().getName();
        System.out.println("Iniciando interceptor: " + metodo);
        Object resultado = context.proceed();
        long fin = System.currentTimeMillis() - inicio; 
        System.out.println("Tiempo de ejecucion del interceptor: " + fin + "ms" +" del metodo" + metodo);
        return resultado;

    }

}
