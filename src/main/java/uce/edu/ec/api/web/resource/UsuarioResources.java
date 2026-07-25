package uce.edu.ec.api.web.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import uce.edu.ec.api.application.service.UsuarioService;
import uce.edu.ec.api.domain.model.Usuario;

@Path("/usuarios")
public class UsuarioResources {

    @Inject
    private UsuarioService us;

    // http://localhost:8080/usuarios/porId/2
    @Path("/porId/{id}")
    @GET
    public Usuario buscarPorId(@PathParam("id") Integer id) {
        return this.us.buscarUsuarioId(id);
    }

    // http://localhost:8080/usuarios/todos
    @Path("/todos")
    @GET
    public List<Usuario> buscarTodos() {
        return this.us.buscarTodos();
    }

    // http://localhost:8080/usuarios/guardar
    @Path("/guardar")
    @POST
    public Usuario guardar(Usuario usuario) {
        this.us.crearUsuario(usuario);
        return usuario;
    }

    // http://localhost:8080/usuarios/actualizar/{id}
    @Path("/actualizar/{id}")
    @PUT
    public String actualizar(Usuario usuarioNuevo, @PathParam("id") Integer id) {
        this.us.actualizarUsuario(usuarioNuevo, id);
        return "Usuario actualizado correctamente";
    }

    // http://localhost:8080/usuarios/eliminar/{id}
    @Path("/eliminar/{id}")
    @DELETE
    public void eliminar(@PathParam("id") Integer id) {
        this.us.eliminarUsuarioId(id);
    }
}