package uce.edu.ec.web.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import uce.edu.ec.application.service.ProyectoService;
import uce.edu.ec.domain.model.Proyecto;

@Path("/proyectos")
public class ProyectosResources {

    @Inject
    private ProyectoService ps;

    //http://localhost:8080/proyectos/listado
    @Path("/listado")
    @GET
    public List<String> listadoProyectos(){
        return List.of("IA", "ROBOTICA", "5G");
    }
    

    //http://localhost:8080/proyectos/registrado
    @Path("/registrados")
    @POST
    public void guardar(Proyecto proyecto){

        this.ps.guardar(proyecto);

    }

    @Path("/porId/{id}")
    @GET
    public Proyecto buscarPorId(@PathParam("id") Integer id){

        return this.ps.buscarPorId(id);

    }

    @Path("/actualizar/{id}")
    @PUT
    public void actualizar(Proyecto proyecto, @PathParam("id") Integer id){

        this.ps.actualizar(proyecto, id);

    }

    @Path("/listartodos")
    @GET
    public List<Proyecto> listarTodos(){

        return this.ps.listarTodos();

    }

    @Path("/eliminarporid/{id}")
    @DELETE
    public void eliminar(@PathParam("id") Integer id){

        this.ps.eliminarPorId(id);

    }
   
    @Path("/procesados")
    @GET
    public List<Proyecto> obtenerProcesados() {

        return ps.procesarProyectosEnParalelo();

    }

    @Path("/mayoreo/{costo}")
    @GET
    public List<String> obtenerMayoreo(@PathParam("costo")Double costo){
        return this.ps.obtenerPorMayoreo(costo);

    }

}
