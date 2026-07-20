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
import uce.edu.ec.api.application.service.FacturaService;
import uce.edu.ec.api.domain.model.Factura;

@Path("/facturas")

public class FacturaResources {

    @Inject
    private FacturaService fs;

    // http://localhost:8080/facturas/porId/2
    @Path("/porId/{id}")
    @GET
    @Blocking
    public Response buscarPorId(@PathParam("id") Integer id) {
        Factura factura = this.fs.buscarFacturaId(id);
        return Response.ok(factura).build();
    }

    // http://localhost:8080/facturas/todos
    @Path("/todos")
    @GET
    @Blocking
    public Response buscarTodos() {
        List<Factura> lista = this.fs.buscarTodos();
        return Response.ok(lista).build();
    }

    // http://localhost:8080/facturas/guardar
    @Path("/guardar")
    @POST
    @Blocking
    public Response guardar(Factura factura) {
        this.fs.crearFactura(factura);
        return Response.status(Response.Status.CREATED).entity(factura).build();
    }

    // http://localhost:8080/facturas/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    @Blocking
    public Response actualizar(Factura facturaNueva, @PathParam("id") Integer id) {
        this.fs.actualizarFactura(facturaNueva, id);
        return Response.ok().entity("Factura actualizada correctamente").build();
    }

    // http://localhost:8080/facturas/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    @Blocking
    public Response eliminar(@PathParam("id") Integer id) {
        this.fs.eliminarFacturaId(id);
        return Response.noContent().build();
    }
}