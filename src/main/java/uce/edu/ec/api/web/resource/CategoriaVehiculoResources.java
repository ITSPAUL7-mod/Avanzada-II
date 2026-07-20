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
import uce.edu.ec.api.application.service.CategoriaVehiculoService;
import uce.edu.ec.api.domain.model.CategoriaVehiculo;

@Path("/categorias-vehiculo")
public class CategoriaVehiculoResources {

    @Inject
    private CategoriaVehiculoService cs;

    // http://localhost:8080/categorias-vehiculo/porId/2
    @Path("/porId/{id}")
    @GET
    @Blocking
    public Response buscarPorId(@PathParam("id") Integer id) {
        CategoriaVehiculo categoria = this.cs.buscarCategoriaVehiculoId(id);
        return Response.ok(categoria).build();
    }

    // http://localhost:8080/categorias-vehiculo/todos
    @Path("/todos")
    @GET
    @Blocking
    public Response buscarTodos() {
        List<CategoriaVehiculo> lista = this.cs.buscarTodos();
        return Response.ok(lista).build();
    }

    // http://localhost:8080/categorias-vehiculo/guardar
    @Path("/guardar")
    @POST
    @Blocking
    public Response guardar(CategoriaVehiculo categoriaVehiculo) {
        this.cs.crearCategoriaVehiculo(categoriaVehiculo);
        return Response.status(Response.Status.CREATED).entity(categoriaVehiculo).build();
    }

    // http://localhost:8080/categorias-vehiculo/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    @Blocking
    public Response actualizar(CategoriaVehiculo categoriaVehiculonueva, @PathParam("id") Integer id) {
        this.cs.actualizarCategoriaVehiculo(categoriaVehiculonueva, id);
        return Response.ok().entity("Categoría de vehículo actualizada correctamente").build();
    }

    // http://localhost:8080/categorias-vehiculo/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    @Blocking
    public Response eliminar(@PathParam("id") Integer id) {
        this.cs.eliminarCategoriaVehiculoId(id);
        return Response.noContent().build();
    }
}