package uce.edu.pa2.api;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@MedirTiempo
@Interceptor
@Priority(2)
public class MedirTiempoInterceptor {

    // le dice a este metodo que se va a ejecutar
    // alrededor de la ejecucion de este metodo
    @AroundInvoke
    public Object medir(InvocationContext context) throws Exception {

        System.out.println("Se ejecuto antes del metodo");
        System.out.println("Metodo Interceptado: " + context.getMethod().getName());
        long inicio = System.currentTimeMillis();
        //inicia la ejeucion del metodo, si no se lo llama al preceed nunca se ejecuta el metodo, pero si el interceptor
        Object resultado =context.proceed();
        //pasa a la linea siguiente una vez que se termine de ejecutar el metodo de negocio
        long fin = System.currentTimeMillis();

        long tiempoTranscurrido = fin - inicio;
        System.out.println("Tiempo transcurrido: " + tiempoTranscurrido);


        return resultado;

    }
}