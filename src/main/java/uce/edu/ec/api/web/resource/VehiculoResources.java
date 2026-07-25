package uce.edu.ec.api.web.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import uce.edu.ec.api.application.service.VehiculoService;
import uce.edu.ec.api.domain.model.Vehiculo;

@Path("/vehiculos")
public class VehiculoResources {

    @Inject
    private VehiculoService vs;

    // http://localhost:8080/vehiculos/porId/2
    @Path("/porId/{id}")
    @GET
    public Vehiculo buscarPorId(@PathParam("id") Integer id) {
        return this.vs.buscarVehiculoId(id);
    }

    // http://localhost:8080/vehiculos/porPlaca/PBX-1234
    @Path("/porPlaca/{placa}")
    @GET
    public Vehiculo buscarPorPlaca(@PathParam("placa") String placa) {
        return this.vs.buscarVehiculoPlaca(placa);
    }

    // http://localhost:8080/vehiculos/todos
    @Path("/todos")
    @GET
    public List<Vehiculo> buscarTodos() {
        return this.vs.buscarTodos();
    }

    // http://localhost:8080/vehiculos/guardar
    @Path("/guardar")
    @POST
    public Vehiculo guardar(Vehiculo vehiculo) {
        this.vs.crearVehiculo(vehiculo);
        return vehiculo;
    }

    // http://localhost:8080/vehiculos/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    public String actualizar(Vehiculo vehiculoNuevo, @PathParam("id") Integer id) {
        this.vs.actualizarVehiculo(vehiculoNuevo, id);
        return "Vehículo actualizado correctamente";
    }

    // http://localhost:8080/vehiculos/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    public void eliminar(@PathParam("id") Integer id) {
        this.vs.eliminarVehiculoId(id);
    }
}