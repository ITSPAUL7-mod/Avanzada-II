package uce.edu.ec.api.web.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import uce.edu.ec.api.application.service.VendedorService;
import uce.edu.ec.api.domain.model.Vendedor;

@Path("/vendedores")
public class VendedorResources {

    @Inject
    private VendedorService vs;

    //http://localhost:8080/vendedores/porId/2
    @Path("/porId/{id}")
    @GET
    public Vendedor buscarPorId(@PathParam("id") Integer id) {

        return vs.buscarVendedorId(id);
    }

    //http://localhost:8080/vendedores/todos
    @Path("/todos")
    @GET
    public List<Vendedor> buscarTodos() {
        return this.vs.buscarTodos();
    }

    //http://localhost:8080/vendedores/guardar
    @Path("/guardar")
    @POST
    public void guardar(Vendedor vendedor) {

        this.vs.crearVendedor(vendedor);

    }

    //http://localhost:8080/vendedores/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    public void actualizar(Vendedor vendedornuevo, @PathParam("id") Integer id) {

        this.vs.actualizarVendedor(vendedornuevo, id);

    }

    //http://localhost:8080/vendedores/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    public void eliminar(@PathParam("id") Integer id) {

        this.vs.eliminarVendedorId(id);
    }

}
