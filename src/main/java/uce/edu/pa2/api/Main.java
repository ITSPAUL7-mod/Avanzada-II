package uce.edu.pa2.api;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import net.bytebuddy.asm.Advice.This;

@QuarkusMain

public class Main {

    public static void main(String... args) {

        Quarkus.run(App.class, args);

    }

    public static class App implements QuarkusApplication {

        @Inject
        private ProcesadorCompraService procesadorCompraService;
      

        @Override
        public int run(String... args) throws Exception {

            Compra comp1 = new Compra("Paul Aguas", Double.valueOf(200));

            this.procesadorCompraService.procesar(comp1);


            return 0;
        }
    }
}
