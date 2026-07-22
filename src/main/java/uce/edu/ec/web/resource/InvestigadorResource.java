package uce.edu.ec.web.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import uce.edu.ec.application.service.InvestigadorService;
import uce.edu.ec.domain.model.Investigador;

@Path("/investigadores")
public class InvestigadorResource {

    @Inject
    private InvestigadorService is;

    @Path("/listatodos")
    @GET
    public List<Investigador> listarTodos(){
        return this.is.listarTodos();
    }

    @Path("/guardar")
    @POST
    public void guardar(Investigador investigador){

        this.is.guardar(investigador);

    }

    @Path("/actualizar/{id}")
    @PUT
    public void actualizar(Investigador investigador, @PathParam("id") Integer id){

        this.is.actualizar(investigador, id);

        
    }

    @Path("/buscarporid/{id}")
    @GET
    public Investigador buscarPorId(@PathParam("id") Integer id){

        return this.is.buscarPorId(id);

    }

    @Path("/eliminar/{id}")
    @DELETE
    public void eliminarPorid(@PathParam("id") Integer id){

        this.is.eliminarPorId(id);

    }

    @Path("/gruposanguineo/{tipo}")
    @GET
    public List<Investigador> listarGrupoSanguineo(@PathParam("tipo") String tipo){

        return this.is.grupoSanguineo(tipo);

    }
}
