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
import uce.edu.ec.api.application.service.SucursalService;
import uce.edu.ec.api.domain.model.Sucursal;

@Path("/sucursales")

public class SucursalResources {

    @Inject
    private SucursalService ss;

    // http://localhost:8080/sucursales/porId/2
    @Path("/porId/{id}")
    @GET
    @Blocking
    public Response buscarPorId(@PathParam("id") Integer id) {
        Sucursal sucursal = this.ss.buscarSucursalId(id);
        return Response.ok(sucursal).build();
    }

    // http://localhost:8080/sucursales/todos
    @Path("/todos")
    @GET
    @Blocking
    public Response buscarTodos() {
        List<Sucursal> lista = this.ss.buscarTodos();
        return Response.ok(lista).build();
    }

    // http://localhost:8080/sucursales/guardar
    @Path("/guardar")
    @POST
    @Blocking
    public Response guardar(Sucursal sucursal) {
        this.ss.crearSucursal(sucursal);
        return Response.status(Response.Status.CREATED).entity(sucursal).build();
    }

    // http://localhost:8080/sucursales/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    @Blocking
    public Response actualizar(Sucursal sucursalNueva, @PathParam("id") Integer id) {
        this.ss.actualizarSucursal(sucursalNueva, id);
        return Response.ok().entity("Sucursal actualizada correctamente").build();
    }

    // http://localhost:8080/sucursales/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    @Blocking
    public Response eliminar(@PathParam("id") Integer id) {
        this.ss.eliminarSucursalId(id);
        return Response.noContent().build();
    }
}