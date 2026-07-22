package uce.edu.ec.application.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.ec.application.interceptor.tiempo;
import uce.edu.ec.domain.model.FichaMedica;
import uce.edu.ec.infraestructure.repository.FichaMedicaRepositoryImpl;

@ApplicationScoped
@Transactional
public class FichaService {

    @Inject
    private FichaMedicaRepositoryImpl fmri;

    public List<FichaMedica> listarTodos(){

        return this.fmri.findAll().list();

    }

    @tiempo
    public void guardar(FichaMedica ficha){

        this.fmri.persist(ficha);

    }

    @tiempo
    public void actualizar(FichaMedica ficha, Integer id){

        FichaMedica base = this.buscarPorId(ficha.getId());
        base.setSanguineo(ficha.getSanguineo());
        base.setAlergias(ficha.getAlergias());


    }

    public FichaMedica buscarPorId(Integer id){

        return this.fmri.findById(id);

    }

    public void eliminarporId(Integer id){

        this.fmri.deleteById(id);

    }
 }
