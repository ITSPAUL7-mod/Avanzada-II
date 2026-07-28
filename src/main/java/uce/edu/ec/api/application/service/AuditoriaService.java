package uce.edu.ec.api.application.service;

import java.time.LocalDateTime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.ec.api.domain.model.Auditoria;
import uce.edu.ec.api.infraestructure.repository.AuditoriaRepositoryImpl;

@ApplicationScoped
@Transactional
public class AuditoriaService {

    @Inject
    AuditoriaRepositoryImpl repository;

    public void guardarAuditoria(String metodo, String entidad) {

        Auditoria auditoria = new Auditoria();

        auditoria.setMetodo(metodo);
        auditoria.setEntidad(entidad);
        auditoria.setFecha(LocalDateTime.now());

        if (metodo.startsWith("buscar")) {
            auditoria.setOperacion("SELECT");
            auditoria.setDescripcion("Se realizó una consulta.");
        } else if (metodo.startsWith("crear")) {
            auditoria.setOperacion("INSERT");
            auditoria.setDescripcion("Se creó un registro.");
        } else if (metodo.startsWith("actualizar")) {
            auditoria.setOperacion("UPDATE");
            auditoria.setDescripcion("Se actualizó un registro.");
        } else if (metodo.startsWith("eliminar")) {
            auditoria.setOperacion("DELETE");
            auditoria.setDescripcion("Se eliminó un registro.");
        }

        repository.persist(auditoria);
    }

}