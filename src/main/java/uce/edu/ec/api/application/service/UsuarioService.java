package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import uce.edu.ec.api.domain.model.Usuario;
import uce.edu.ec.api.infraestructure.repository.UsuarioRepositoryImpl;

@ApplicationScoped
@Transactional
public class UsuarioService {

    @Inject
    private UsuarioRepositoryImpl uri;

    public void crearUsuario(Usuario usuario) {

        if (usuario == null) {
            throw new WebApplicationException("El cuerpo de la petición no puede estar vacío", 400);
        }

        if (usuario.getCedula() == null || usuario.getCedula().trim().isEmpty()) {
            throw new WebApplicationException("La cédula del usuario es obligatoria", 400);
        }

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new WebApplicationException("El nombre del usuario es obligatorio", 400);
        }

        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new WebApplicationException("El correo del usuario es obligatorio", 400);
        }

        Usuario existeCedula = this.uri.find("cedula", usuario.getCedula().trim()).firstResult();
        if (existeCedula != null) {
            throw new WebApplicationException("Ya existe un usuario registrado con la cédula: " + usuario.getCedula(),
                    400);
        }

        this.uri.persist(usuario);
    }

    public List<Usuario> buscarTodos() {
        return this.uri.findAll().list();
    }

    public void actualizarUsuario(Usuario usuario, Integer id) {

        if (usuario == null) {
            throw new WebApplicationException("Los datos para actualizar no pueden estar vacíos", 400);
        }

        Usuario base = this.buscarUsuarioId(id);

        if (usuario.getCedula() != null && !usuario.getCedula().trim().isEmpty()) {
            if (!usuario.getCedula().equals(base.getCedula())) {
                Usuario existeCedula = this.uri.find("cedula", usuario.getCedula().trim()).firstResult();
                if (existeCedula != null) {
                    throw new WebApplicationException(
                            "La cédula " + usuario.getCedula() + " ya está asignada a otro usuario", 400);
                }
                base.setCedula(usuario.getCedula());
            }
        }

        if (usuario.getNombre() != null && !usuario.getNombre().trim().isEmpty()) {
            base.setNombre(usuario.getNombre());
        }

        if (usuario.getCorreo() != null && !usuario.getCorreo().trim().isEmpty()) {
            base.setCorreo(usuario.getCorreo());
        }
    }

    public Usuario buscarUsuarioId(Integer id) {

        if (id == null) {
            throw new WebApplicationException("El ID del usuario es obligatorio", 400);
        }

        Usuario usuario = this.uri.findById(id);

        if (usuario == null) {
            throw new WebApplicationException("No existe un usuario registrado con el ID: " + id, 404);
        }

        return usuario;
    }

    public void eliminarUsuarioId(Integer id) {

        this.buscarUsuarioId(id);

        this.uri.deleteById(id); 
    }
}