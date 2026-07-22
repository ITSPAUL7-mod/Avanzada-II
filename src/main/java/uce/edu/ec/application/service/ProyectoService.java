package uce.edu.ec.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.ec.domain.model.Proyecto;
import uce.edu.ec.infraestructure.repository.ProyectoRepositoryImpl;

@ApplicationScoped
@Transactional
public class ProyectoService {

    @Inject
    private ProyectoRepositoryImpl pri;

    public List<String> procesarNombreProyectos(List<Proyecto> lista) {

        return lista.parallelStream()
                .filter(p -> p.getPresupuesto() > 5000.0)
                .map(p -> {
                    System.out.println(
                            "Procesando: " + p.getNombre() +
                                    " en hilo " + Thread.currentThread().getName());
                    return p.getNombre();
                })
                .toList();
    }

    public List<Proyecto> listarTodos() {

        return this.pri.findAll().list();

    }

    public void guardar(Proyecto proyecto) {

        this.pri.persist(proyecto);

    }

    public void actualizar(Proyecto proyecto, Integer id) {

        Proyecto base = this.buscarPorId(proyecto.getId());
        base.setCodigo(proyecto.getCodigo());
        base.setNombre(proyecto.getNombre());
        base.setInvestigadores(proyecto.getInvestigadores());
        base.setPresupuesto(proyecto.getPresupuesto());

    }

    public Proyecto buscarPorId(Integer id) {

        return this.pri.findById(id);

    }

    public void eliminarPorId(Integer id) {

        this.pri.deleteById(id);

    }

    public List<Proyecto> procesarProyectosEnParalelo() {

        // 1. Obtenemos la lista de proyectos desde la BD
        List<Proyecto> listaProyectos = pri.findAll().list();

        // 2. Procesamos la lista utilizando un ParallelStream
        List<Proyecto> resultado = listaProyectos.parallelStream()
                // Solo proyectos con presupuesto mayor a 5000
                .filter(p -> p.getPresupuesto() > 5000.0)

                // Procesamiento paralelo
                .map(p -> {
                    System.out.println(
                            "Procesando '" + p.getNombre() +
                                    "' en el Hilo: " +
                                    Thread.currentThread().getName());

                    // Cálculo del presupuesto con IVA
                    double presupuestoConIva = p.getPresupuesto() * 1.15;

                    // Mostrar el resultado del cálculo
                    System.out.println(
                            "Presupuesto con IVA: " + presupuestoConIva);

                    // Retornamos el proyecto original
                    return p;
                })

                // Convertimos el resultado en una List
                .toList();

        return resultado;
    }

    public List<String> obtenerPorMayoreo(Double mayoreo){

        List<Proyecto> lista = pri.obtenerTodos();

        return lista.parallelStream()
        .filter(p-> p.getPresupuesto() > mayoreo)
        .map(Proyecto::getNombre).toList();


    }

}
