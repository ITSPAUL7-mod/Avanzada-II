package uce.edu.ec.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.ec.domain.model.Investigador;
import uce.edu.ec.infraestructure.repository.InvestigadorRepositoryImpl;

@ApplicationScoped
@Transactional
public class InvestigadorService {

    @Inject
    private InvestigadorRepositoryImpl iri;

    public List<Investigador> listarTodos(){

        return this.iri.findAll().list();
    }

    public void guardar(Investigador investigador){

        this.iri.persist(investigador);

    }

    public void actualizar(Investigador investigador, Integer id){

        Investigador base = this.buscarPorId(investigador.getId());
        base.setNombre(investigador.getNombre());
        base.setCedula(investigador.getCedula());
        base.setFichas(investigador.getFichas());
        base.setProyectos(investigador.getProyectos());

    }

    public Investigador buscarPorId(Integer id){

        return this.iri.findById(id);

    }

    public void eliminarPorId(Integer id){

        this.iri.deleteById(id);

    }

    public List<Investigador> grupoSanguineo(String sanguineo){

        return this.iri.buscarporSanguineo(sanguineo);

    }

}
