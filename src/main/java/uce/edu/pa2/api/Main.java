package uce.edu.pa2.api;

import java.util.List;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import uce.edu.pa2.api.application.service.ProductoService;
import uce.edu.pa2.api.domain.model.Productos;

@QuarkusMain
public class Main {

    public static void main(String... args) {

        Quarkus.run(App.class, args);

    }

    public static class App implements QuarkusApplication {

        @Inject
        private ProductoService ps;
        @Override
        public int run(String... args) throws Exception {

            System.out.println("Conexion a la base POSTGRES!!!");
/* 
            System.out.println("Obtener Todos los productos");
            List<Productos> p1 = ps.obtenerProductos();
            for(Productos p: p1){
                System.out.println(p);

            }
            System.out.println("Obtener productos por precio > 1000");
            List<Productos> p2 = ps.ObtenerProductosPorPrecio(1000.0);
            for(Productos p : p2){
                System.out.println(p);

            }

            System.out.println("Obtener productos por categoria");
            List<Productos> p3 = ps.ObtenerPorCategoria("Vegetales");
            for(Productos p : p3){
                System.out.println(p);

            }

            System.out.println("Contar Productos");
            Long contar = ps.contarProductos();
            System.out.println(contar);

            System.out.println("Obtener productos por categoria Native");
            List<Productos> p4 = ps.ObtenerPorCategoriaNative("Vege");
            for(Productos p : p4){
                System.out.println(p);

            }

            System.out.println("Consulta Dinamica");
            List<Productos> p5 = ps.consultaDinamica("P",200.0,1000.0);
            for(Productos p: p5){

                System.out.println(p);
            }

            */

            System.out.println("\n🚀 INICIANDO PRUEBA DESDE EL MAIN 🚀\n");

        // 1. Probar el método crearProducto (Escritura)
        Productos nuevo = new Productos();
        nuevo.setNombre("Teclado Mecánico");
        nuevo.setCategoria("Electronica");
        nuevo.setPrecio(75.50);
        
        System.out.println("[Main] Llamando a crearProducto...");
        ps.guardar(nuevo);


        // 2. Probar tu método de consulta Criteria (Lectura Dinámica)
        System.out.println("\n[Main] Llamando a seleccionarDinamica...");
        List<Productos> filtrados = ps.consultaDinamica("P",1000.0,10000.0);
        
        System.out.println("[Main] Productos encontrados en el Main: " + filtrados.size());
            return 0;
        }
    }
}
