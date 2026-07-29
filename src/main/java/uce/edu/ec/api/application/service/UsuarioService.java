package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uce.edu.ec.api.application.service.interceptor.Auditar;
import uce.edu.ec.api.domain.model.Usuario;
import uce.edu.ec.api.infraestructure.repository.UsuarioRepositoryImpl;

@ApplicationScoped
@Transactional
@Auditar
public class UsuarioService {

    @Inject
    private UsuarioRepositoryImpl uri;

    public void crearUsuario(Usuario usuario) {

        if (usuario == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El cuerpo de la petición no puede estar vacío")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (usuario.getCedula() == null || usuario.getCedula().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("La cédula del usuario es obligatoria")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El nombre del usuario es obligatorio")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("El correo del usuario es obligatorio")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        Usuario existeCedula = this.uri.find("cedula", usuario.getCedula().trim()).firstResult();
        if (existeCedula != null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Ya existe un usuario registrado con la cédula: " + usuario.getCedula())
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        this.uri.persist(usuario);
    }

    @Transactional
    public void crearUsuarios(List<Usuario> usuarios) {

        if (usuarios == null || usuarios.isEmpty()) {
            throw new WebApplicationException("La lista de usuarios no puede estar vacía", 400);
        }

        for (Usuario usuario : usuarios) {
            crearUsuario(usuario);
        }
    }

    public List<Usuario> buscarTodos() {
        return this.uri.findAll().list();
    }

    public void actualizarUsuario(Usuario usuario, String cedula) {

        if (usuario == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Los datos para actualizar no pueden estar vacíos")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        boolean tieneNombre = usuario.getNombre() != null && !usuario.getNombre().trim().isEmpty();
        boolean tieneCorreo = usuario.getCorreo() != null && !usuario.getCorreo().trim().isEmpty();

        if (!tieneNombre && !tieneCorreo) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Debe proporcionar al menos el nombre o correo para actualizar")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }
        Usuario base = this.buscarPorCedula(cedula);
        if (tieneNombre) {
            base.setNombre(usuario.getNombre().trim());
        }

        if (tieneCorreo) {
            base.setCorreo(usuario.getCorreo().trim());
        }
    }

    public Usuario buscarPorCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("La cédula para buscar no puede estar vacía")
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }

        Usuario usuario = this.uri.find("cedula", cedula.trim()).firstResult();
        if (usuario == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity("No existe un usuario registrado con la cédula " + cedula)
                            .type(MediaType.TEXT_PLAIN)
                            .build());
        }
        return usuario;
    }

    public void eliminarPorCedula(String cedula) {
        this.buscarPorCedula(cedula);
        this.uri.delete("cedula", cedula.trim());
    }
}