package uce.edu.ec.api.web.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import uce.edu.ec.api.application.service.CategoriaVehiculoService;
import uce.edu.ec.api.domain.model.CategoriaVehiculo;

@Path("/categorias-vehiculo")
public class CategoriaVehiculoResources {

    @Inject
    private CategoriaVehiculoService cs;

    //http://localhost:8080/categorias-vehiculo/porId/2
    @Path("/porId/{id}")
    @GET
    public CategoriaVehiculo buscarPorId(@PathParam("id") Integer id) {

        return cs.buscarCategoriaVehiculoId(id);
    }

    //http://localhost:8080/categorias-vehiculo/todos
    @Path("/todos")
    @GET
    public List<CategoriaVehiculo> buscarTodos() {
        return this.cs.buscarTodos();
    }

    //http://localhost:8080/categorias-vehiculo/guardar
    @Path("/guardar")
    @POST
    public void guardar(CategoriaVehiculo categoriaVehiculo) {

        this.cs.crearCategoriaVehiculo(categoriaVehiculo);

    }

    //http://localhost:8080/categorias-vehiculo/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    public void actualizar(CategoriaVehiculo categoriaVehiculonueva, @PathParam("id") Integer id) {

        this.cs.actualizarCategoriaVehiculo(categoriaVehiculonueva, id);

    }

    //http://localhost:8080/categorias-vehiculo/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    public void eliminar(@PathParam("id") Integer id) {

        this.cs.eliminarCategoriaVehiculoId(id);
    }

}
