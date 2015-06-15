/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.unicauca.tgu.FormatosTablas.TablaPerfil;
import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.FaseFacade;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class VerTGController {

    private List<TablaPerfil> tblPerfil;

    private List<VerTGController> trabajosDeGrado;

    @EJB
    private TrabajodegradoFacade ejbFacadeTG;
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuRolTG;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTGFase;
    @EJB
    private FaseFacade ejbFacadeFase;

    public VerTGController() {
        tblPerfil = new ArrayList();
    }

    @PostConstruct
    public void init() {
        trabajosDeGrado = new ArrayList();
    }

    public List<TablaPerfil> getTrabajosDeGrado() {

        tblPerfil = this.llenarTabla();

        return tblPerfil;
    }

    public List<TablaPerfil> llenarTabla() {

        List<UsuarioRolTrabajogrado> usuRolTG_lst = ejbFacadeUsuRolTG.findAll();
        List<Trabajodegrado> tg_lst = ejbFacadeTG.findAll();
        List<TrabajogradoFase> tg_fase = ejbFacadeTGFase.findAll();

        List<TablaPerfil> trabs;
        trabs = new ArrayList();
        int cont = 0;

        TablaPerfil iesimaTblPerfil = new TablaPerfil();

        for (Trabajodegrado tg_item : tg_lst) {
            iesimaTblPerfil = new TablaPerfil();
            //
            for (UsuarioRolTrabajogrado l : usuRolTG_lst) {
                if (tg_item.getTrabajoid().equals(l.getTrabajoid().getTrabajoid())) {
                    if (l.getRolid().getRolid().intValue() == 0) //director
                    {
                        iesimaTblPerfil.setFecha(l.getFechaasignacion());
                        iesimaTblPerfil.setTrabajoGradoId(l.getTrabajoid().getTrabajoid().intValue());
                        iesimaTblPerfil.setTrabajoGrado(l.getTrabajoid().getTrabajonombre());

                        iesimaTblPerfil.setDirector(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                        iesimaTblPerfil.setDirectorId(l.getPersonacedula().getPersonacedula().intValue());
                    } else if (l.getRolid().getRolid().intValue() == 1 && cont == 0) //Estudiante 1
                    {
                        iesimaTblPerfil.setEst1(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                        iesimaTblPerfil.setEst1Id(l.getPersonacedula().getPersonacedula().intValue());
                        cont++;
                    } else if (l.getRolid().getRolid().intValue() == 1 && cont == 1) //estudiante 2
                    {
                        iesimaTblPerfil.setEst2(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                        iesimaTblPerfil.setEst2Id(l.getPersonacedula().getPersonacedula().intValue());
                    }
                    int x = 999;
                    for (TrabajogradoFase tg : tg_fase) {
                        if (tg.getTrabajoid().getTrabajoid().equals(tg_item.getTrabajoid()) && 
                                tg.getEstado().intValue() == 0 && tg.getFaseid().getFaseorden().intValue() < x) {
                            iesimaTblPerfil.setEstado(tg.getFaseid());
                            x = tg.getFaseid().getFaseorden().intValue();
                        }
                    }
                }
            }
            //
            trabs.add(iesimaTblPerfil);
        }

        return trabs;
    }

    public List<TablaPerfil> getTblPerfil() {
        return tblPerfil;
    }

    public void setTblPerfil(List<TablaPerfil> tblPerfil) {
        this.tblPerfil = tblPerfil;
    }
    
    public List<String> getFases() {
        List<String> fases = new ArrayList();
        
        List<Fase> fasesLst = ejbFacadeFase.faseOrderByOrden();
        
        for(Fase faseItem : fasesLst) {
            fases.add(faseItem.getFasenombre());
        }
        return fases;
    }
}
