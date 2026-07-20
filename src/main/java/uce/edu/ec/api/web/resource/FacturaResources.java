package uce.edu.ec.api.web.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import uce.edu.ec.api.application.service.FacturaService;
import uce.edu.ec.api.domain.model.Factura;

@Path("/facturas")
public class FacturaResources {

    @Inject
    private FacturaService fs;

    //http://localhost:8080/facturas/porId/2
    @Path("/porId/{id}")
    @GET
    public Factura buscarPorId(@PathParam("id") Integer id) {

        return fs.buscarFacturaId(id);
    }

    //http://localhost:8080/facturas/todos
    @Path("/todos")
    @GET
    public List<Factura> buscarTodos() {
        return this.fs.buscarTodos();
    }

    //http://localhost:8080/facturas/guardar
    @Path("/guardar")
    @POST
    public void guardar(Factura factura) {

        this.fs.crearFactura(factura);

    }

    //http://localhost:8080/facturas/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    public void actualizar(Factura facturanueva, @PathParam("id") Integer id) {

        this.fs.actualizarFactura(facturanueva, id);

    }

    //http://localhost:8080/facturas/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    public void eliminar(@PathParam("id") Integer id) {

        this.fs.eliminarFacturaId(id);
    }

}
