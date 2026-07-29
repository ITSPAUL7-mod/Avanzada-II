package uce.edu.ec.api.web.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import uce.edu.ec.api.application.service.VendedorService;
import uce.edu.ec.api.domain.model.Vendedor;

@Path("/vendedores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VendedorResources {

    @Inject
    private VendedorService vs;

    // http://localhost:8080/vendedores/porCedula/1234567890
    @Path("/porCedula/{cedula}")
    @GET
    public Vendedor buscarPorCedula(@PathParam("cedula") String cedula) {
        return this.vs.buscarPorCedula(cedula);
    }

    // http://localhost:8080/vendedores/todos
    @Path("/todos")
    @GET
    public List<Vendedor> buscarTodos() {
        return this.vs.buscarTodos();
    }

    // http://localhost:8080/vendedores/guardar
    @Path("/guardar")
    @POST
    public Vendedor guardar(Vendedor vendedor) {
        this.vs.crearVendedor(vendedor);
        return vendedor;
    }

    // http://localhost:8080/vendedores/guardarlist
    @Path("/guardarlist")
    @POST
    public List<Vendedor> crearMuchos(List<Vendedor> vendedores) {
        vs.crearVendedores(vendedores);
        return vendedores;
    }

    // http://localhost:8080/vendedores/actualizar/{cedula}
    @Path("/actualizar/{cedula}")
    @PUT
    public String actualizar(Vendedor vendedorNuevo, @PathParam("cedula") String cedula) {
        this.vs.actualizarVendedor(vendedorNuevo, cedula);
        return "Vendedor actualizado correctamente";
    }

    // http://localhost:8080/vendedores/eliminar/{cedula}
    @Path("/eliminar/{cedula}")
    @DELETE
    public void eliminarPorCedula(@PathParam("cedula") String cedula) {
        this.vs.eliminarPorCedula(cedula);
    }
}