package uce.edu.pa2.api;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@log
@Interceptor
@Priority(1)

public class logInterceptor {

    // le dice a este metodo que se va a ejecutar
    // alrededor de la ejecucion de este metodo
    @AroundInvoke
    public Object medir(InvocationContext context) throws Exception {

        System.out.println("Se ejecuto LOG antes del metodo");
        System.out.println("Interceptado el metodo: " + context.getMethod().getName());

        Object [] args  = context.getParameters();

        for(int i = 0; i < args.length; i++){

            System.out.println("Argumento: " + args[i]);
            Object obj = args[i];
            Venta venta= (Venta) obj;
            System.out.println(venta.getCliente());
            System.out.println(venta.getTotal());
        }
        Object resultado = context.proceed();

        System.out.println("Salida del LOGInteceptor");  
        return resultado;

    }
}