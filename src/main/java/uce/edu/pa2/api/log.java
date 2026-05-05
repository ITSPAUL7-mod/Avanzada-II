
package uce.edu.pa2.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.interceptor.InterceptorBinding;
// DECLARACION DE UNA ANOTACION     
//con esta anotacion le indicamos que va a ser un interceptor
@InterceptorBinding
//Define a que esta determinado el interceptor
//TYPE, para clase
//METHOD, metodo
@Target({ElementType.TYPE, ElementType.METHOD})
//La retencion se ejecuta cuando declaras, tiempo de ejecucion o de compilacion
@Retention(RetentionPolicy.RUNTIME)
public @interface log {



}
