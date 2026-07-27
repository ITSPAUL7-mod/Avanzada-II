package uce.edu.ec.api.application.service;

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

    private Auditoria obtenerAuditoria() {

        Auditoria auditoria = repository.findAll().firstResult();

        if (auditoria == null) {
            auditoria = new Auditoria();
            auditoria.setSelects(0);
            auditoria.setInserts(0);
            auditoria.setUpdates(0);
            auditoria.setDeletes(0);

            repository.persist(auditoria);
        }

        return auditoria;
    }

    public void incrementarSelect() {
        Auditoria a = obtenerAuditoria();
        a.setSelects((a.getSelects() == null ? 0 : a.getSelects()) + 1);
    }

    public void incrementarInsert() {
        Auditoria a = obtenerAuditoria();
        a.setInserts((a.getInserts() == null ? 0 : a.getInserts()) + 1);
    }

    public void incrementarUpdate() {
        Auditoria a = obtenerAuditoria();
        a.setUpdates((a.getUpdates() == null ? 0 : a.getUpdates()) + 1);
    }

    public void incrementarDelete() {
        Auditoria a = obtenerAuditoria();
        a.setDeletes((a.getDeletes() == null ? 0 : a.getDeletes()) + 1);
    }
}