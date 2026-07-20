package uce.edu.ec.api.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.ec.api.domain.model.Usuario;
import uce.edu.ec.api.infraestructure.repository.UsuarioRepositoryImpl;

@ApplicationScoped
@Transactional
public class UsuarioService {

    @Inject
    private UsuarioRepositoryImpl uri;

    public void crearUsuario(Usuario usuario) {

        this.uri.persist(usuario);

    }

    public List<Usuario> buscarTodos() {
        return this.uri.findAll().list();
    }

    public void actualizarUsuario(Usuario usuario, Integer id) {

        Usuario base = this.buscarUsuarioId(id);
        base.setCedula(usuario.getCedula());
        base.setNombre(usuario.getNombre());
        base.setCorreo(usuario.getCorreo());

    }

    public Usuario buscarUsuarioId(Integer id) {

        return Usuario.findById(id);
    }

    public void eliminarUsuarioId(Integer id) {

        this.uri.deleteById(id);
    }

}
