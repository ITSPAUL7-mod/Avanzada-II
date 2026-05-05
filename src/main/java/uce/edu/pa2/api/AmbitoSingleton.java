package uce.edu.pa2.api;

import jakarta.inject.Singleton;

@Singleton
public class AmbitoSingleton {

     private int contador = 0;

     public int incrementar(){
        return ++contador;
    }
    
}
