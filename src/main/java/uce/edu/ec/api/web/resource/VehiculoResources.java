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
import uce.edu.ec.api.application.service.VehiculoService;
import uce.edu.ec.api.domain.model.Vehiculo;

@Path("/vehiculos")
public class VehiculoResources {

    @Inject
    private VehiculoService vs;

    // http://localhost:8080/vehiculos/porId/2
    @Path("/porId/{id}")
    @GET
    @Blocking
    public Response buscarPorId(@PathParam("id") Integer id) {
        Vehiculo vehiculo = this.vs.buscarVehiculoId(id);
        return Response.ok(vehiculo).build();
    }

    // http://localhost:8080/vehiculos/porPlaca/PBX-1234
    @Path("/porPlaca/{placa}")
    @GET
    @Blocking
    public Response buscarPorPlaca(@PathParam("placa") String placa) {
        Vehiculo vehiculo = this.vs.buscarVehiculoPlaca(placa);
        return Response.ok(vehiculo).build();
    }

    // http://localhost:8080/vehiculos/todos
    @Path("/todos")
    @GET
    @Blocking
    public Response buscarTodos() {
        List<Vehiculo> lista = this.vs.buscarTodos();
        return Response.ok(lista).build();
    }

    // http://localhost:8080/vehiculos/guardar
    @Path("/guardar")
    @POST
    @Blocking
    public Response guardar(Vehiculo vehiculo) {
        this.vs.crearVehiculo(vehiculo);
        return Response.status(Response.Status.CREATED).entity(vehiculo).build();
    }

    // http://localhost:8080/vehiculos/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    @Blocking
    public Response actualizar(Vehiculo vehiculoNuevo, @PathParam("id") Integer id) {
        this.vs.actualizarVehiculo(vehiculoNuevo, id);
        return Response.ok().entity("Vehículo actualizado correctamente").build();
    }

    // http://localhost:8080/vehiculos/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    @Blocking
    public Response eliminar(@PathParam("id") Integer id) {
        this.vs.eliminarVehiculoId(id);
        return Response.noContent().build();
    }
}