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
import uce.edu.ec.api.application.service.UsuarioService;
import uce.edu.ec.api.domain.model.Usuario;

@Path("/usuarios")
public class UsuarioResources {

    @Inject
    private UsuarioService us;

    // http://localhost:8080/usuarios/porId/2
    @Path("/porId/{id}")
    @GET
    @Blocking
    public Response buscarPorId(@PathParam("id") Integer id) {
        Usuario usuario = this.us.buscarUsuarioId(id);
        return Response.ok(usuario).build();
    }

    // http://localhost:8080/usuarios/todos
    @Path("/todos")
    @GET
    @Blocking
    public Response buscarTodos() {
        List<Usuario> lista = this.us.buscarTodos();
        return Response.ok(lista).build();
    }

    // http://localhost:8080/usuarios/guardar
    @Path("/guardar")
    @POST
    @Blocking
    public Response guardar(Usuario usuario) {
        this.us.crearUsuario(usuario);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }

    // http://localhost:8080/usuarios/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    @Blocking
    public Response actualizar(Usuario usuarioNuevo, @PathParam("id") Integer id) {
        this.us.actualizarUsuario(usuarioNuevo, id);
        return Response.ok().entity("Usuario actualizado correctamente").build();
    }

    // http://localhost:8080/usuarios/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    @Blocking
    public Response eliminar(@PathParam("id") Integer id) {
        this.us.eliminarUsuarioId(id);
        return Response.noContent().build();
    }
}