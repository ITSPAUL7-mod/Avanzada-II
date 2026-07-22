package uce.edu.ec.web.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import uce.edu.ec.application.service.FichaService;
import uce.edu.ec.domain.model.FichaMedica;

@Path("/fichamedica")
public class FichaResource {

    @Inject
    private FichaService fs;

    @Path("/listartodos")
    @GET
    public List<FichaMedica> listarTodos(){

        return this.fs.listarTodos();

    }

    @Path("/guardar")
    @POST
    public void guardar(FichaMedica fichaMedica){

        this.fs.guardar(fichaMedica);

    }

    @Path("/actualiza/{id}")
    @PUT
    public void actualizar(FichaMedica fichaMedica, @PathParam("id") Integer id){

        this.fs.actualizar(fichaMedica, id);

    }

    @Path("/buscarporid/{id}")
    @GET
    public FichaMedica buscarPorId(@PathParam("id") Integer id){
        return this.fs.buscarPorId(id);

    }

    @Path("/eliminarporid/{id}")
    @GET
    public void eliminarporId(@PathParam("id") Integer id){

        this.fs.eliminarporId(id);

    }

}
