/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.jpacontroller.FaseFacade;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class AdministrarFasesController {

    /**
     * Creates a new instance of AdministrarFasesController
     */
    private List<Fase> lstFases;
    @EJB
    private FaseFacade ejbFacadeFase;
    private Fase fase;
    private int faseId;

    public AdministrarFasesController() {
    }

    @PostConstruct
    public void init() {
        lstFases = ejbFacadeFase.faseOrderByOrden();
        fase = new Fase();
    }

    public List<Fase> getFases() {
        return lstFases;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }
    
    public int getFaseId() {
        return faseId;
    }

    public void setFaseId(int faseId) {
        for (Fase item : lstFases) {
            System.out.println("----" + item.getFasenombre());
            if (item.getFaseid().equals(BigDecimal.valueOf(faseId))) {
                fase = item;
            }
        }
        this.faseId = faseId;
    }

    public void crearFase() throws IOException {
        ejbFacadeFase.create(fase);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect("administrar-fases.xhtml");
    }
    public void editarFase() throws IOException {
        ejbFacadeFase.edit(fase);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect("administrar-fases.xhtml");
    }
    public void eliminarFase() throws IOException {
        ejbFacadeFase.remove(fase);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect("administrar-fases.xhtml");
    }
}
