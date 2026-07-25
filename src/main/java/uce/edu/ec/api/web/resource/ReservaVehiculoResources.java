package uce.edu.ec.api.web.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import uce.edu.ec.api.application.service.ReservaVehiculoService;
import uce.edu.ec.api.domain.model.ReservaVehiculo;

@Path("/reservas")
public class ReservaVehiculoResources {

    @Inject
    private ReservaVehiculoService rs;

    // http://localhost:8080/reservas/porId/2
    @Path("/porId/{id}")
    @GET
    public ReservaVehiculo buscarPorId(@PathParam("id") Integer id) {
        return this.rs.buscarReservaVehiculoId(id);
    }

    // http://localhost:8080/reservas/todos
    @Path("/todos")
    @GET
    public List<ReservaVehiculo> buscarTodos() {
        return this.rs.buscarTodos();
    }

    // http://localhost:8080/reservas/guardar
    @Path("/guardar")
    @POST
    public ReservaVehiculo guardar(ReservaVehiculo reserva) {
        this.rs.crearReservaVehiculo(reserva);
        return reserva;
    }

    // http://localhost:8080/reservas/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    public String actualizar(ReservaVehiculo reservaNueva, @PathParam("id") Integer id) {
        this.rs.actualizarReservaVehiculo(reservaNueva, id);
        return "Reserva de vehículo actualizada correctamente";
    }

    // http://localhost:8080/reservas/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    public void eliminar(@PathParam("id") Integer id) {
        this.rs.eliminarReservaVehiculoId(id);
    }
}