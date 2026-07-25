package uce.edu.ec.api.web.resource;

import java.util.List;

import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import uce.edu.ec.api.application.service.VendedorService;
import uce.edu.ec.api.domain.model.Vendedor;

@Path("/vendedores")
public class VendedorResources {

    @Inject
    private VendedorService vs;

    // http://localhost:8080/vendedores/porId/2
    @Path("/porId/{id}")
    @GET
    @Blocking
    public Response buscarPorId(@PathParam("id") Integer id) {
        Vendedor vendedor = this.vs.buscarVendedorId(id);
        return Response.ok(vendedor).build();
    }

    // http://localhost:8080/vendedores/todos
    @Path("/todos")
    @GET
    @Blocking
    public Response buscarTodos() {
        List<Vendedor> lista = this.vs.buscarTodos();
        return Response.ok(lista).build();
    }

    // http://localhost:8080/vendedores/guardar
    @Path("/guardar")
    @POST
    @Blocking
    public Response guardar(Vendedor vendedor) {
        this.vs.crearVendedor(vendedor);
        return Response.status(Response.Status.CREATED).entity(vendedor).build();
    }

    // http://localhost:8080/vendedores/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    @Blocking
    public Response actualizar(Vendedor vendedorNuevo, @PathParam("id") Integer id) {
        this.vs.actualizarVendedor(vendedorNuevo, id);
        return Response.ok().entity("Vendedor actualizado correctamente").build();
    }

    // http://localhost:8080/vendedores/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    @Blocking
    public Response eliminar(@PathParam("id") Integer id) {
        this.vs.eliminarVendedorId(id);
        return Response.noContent().build();
    }
}