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

    // http://localhost:8080/usuarios/porCedula/1234567890
    @Path("/porCedula/{cedula}")
    @GET
    public Usuario buscarPorCedula(@PathParam("cedula") String cedula) {
        return this.us.buscarPorCedula(cedula);
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


    // http://localhost:8080/usuarios/guardarlist
    @Path("/guardarlist")
    @POST
    public List<Usuario> crearMuchos(List<Usuario> usuarios) {
        us.crearUsuarios(usuarios);
        return usuarios;
    }

    // http://localhost:8080/usuarios/actualizar/{cedula}
    @Path("/actualizar/{cedula}")
    @PUT
    public String actualizar(Usuario usuarioNuevo, @PathParam("cedula") String cedula) {
        this.us.actualizarUsuario(usuarioNuevo, cedula);
        return "Usuario actualizado correctamente";
    }

    // http://localhost:8080/usuarios/eliminar/{cedula}
    @Path("/eliminar/{cedula}")
    @DELETE
    public String eliminar(@PathParam("cedula") String cedula) {
        this.us.eliminarPorCedula(cedula);
        return "Usuario eliminado correctamente";
    }
}