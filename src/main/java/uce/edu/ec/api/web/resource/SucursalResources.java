package uce.edu.ec.api.web.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import uce.edu.ec.api.application.service.SucursalService;
import uce.edu.ec.api.domain.model.Sucursal;

@Path("/sucursales")
public class SucursalResources {

    @Inject
    private SucursalService ss;

    //http://localhost:8080/sucursales/porId/2
    @Path("/porId/{id}")
    @GET
    public Sucursal buscarPorId(@PathParam("id") Integer id) {

        return ss.buscarSucursalId(id);
    }

    //http://localhost:8080/sucursales/todos
    @Path("/todos")
    @GET
    public List<Sucursal> buscarTodos() {
        return this.ss.buscarTodos();
    }

    //http://localhost:8080/sucursales/guardar
    @Path("/guardar")
    @POST
    public void guardar(Sucursal sucursal) {

        this.ss.crearSucursal(sucursal);

    }

    //http://localhost:8080/sucursales/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    public void actualizar(Sucursal sucursalnueva, @PathParam("id") Integer id) {

        this.ss.actualizarSucursal(sucursalnueva, id);

    }

    //http://localhost:8080/sucursales/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    public void eliminar(@PathParam("id") Integer id) {

        this.ss.eliminarSucursalId(id);
    }

}
