package uce.edu.pa2.video;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

@QuarkusMain
public class Main {

    public static void main(String[] args) {

        Quarkus.run(App.class, args);
    }


    public static class App implements QuarkusApplication {

        @Inject
        VideojuegoService videojuegoService;

        @Override
        public int run(String... args) {

            Videojuego videojuego = new Videojuego("God of War","PS-4",50.0);

            videojuegoService.venderJuego(videojuego);

            return 0;
        }
    }

}
