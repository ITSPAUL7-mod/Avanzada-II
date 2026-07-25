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
import uce.edu.ec.api.application.service.ReservaVehiculoService;
import uce.edu.ec.api.domain.model.ReservaVehiculo;

@Path("/reservas")
public class ReservaVehiculoResources {

    @Inject
    private ReservaVehiculoService rs;

    // http://localhost:8080/reservas/porId/2
    @Path("/porId/{id}")
    @GET
    @Blocking
    public Response buscarPorId(@PathParam("id") Integer id) {
        ReservaVehiculo reserva = this.rs.buscarReservaVehiculoId(id);
        return Response.ok(reserva).build();
    }

    // http://localhost:8080/reservas/todos
    @Path("/todos")
    @GET
    @Blocking
    public Response buscarTodos() {
        List<ReservaVehiculo> lista = this.rs.buscarTodos();
        return Response.ok(lista).build();
    }

    // http://localhost:8080/reservas/guardar
    @Path("/guardar")
    @POST
    @Blocking
    public Response guardar(ReservaVehiculo reserva) {
        this.rs.crearReservaVehiculo(reserva);
        return Response.status(Response.Status.CREATED).entity(reserva).build();
    }

    // http://localhost:8080/reservas/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    @Blocking
    public Response actualizar(ReservaVehiculo reservaNueva, @PathParam("id") Integer id) {
        this.rs.actualizarReservaVehiculo(reservaNueva, id);
        return Response.ok().entity("Reserva de vehículo actualizada correctamente").build();
    }

    // http://localhost:8080/reservas/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    @Blocking
    public Response eliminar(@PathParam("id") Integer id) {
        this.rs.eliminarReservaVehiculoId(id);
        return Response.noContent().build();
    }
}