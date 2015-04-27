/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author seven
 */
@ManagedBean
@SessionScoped
public class TrabajogradoFaseController {
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTraFase;
    /**
     * Creates a new instance of TrabajogradoFaseController
     */
    public TrabajogradoFaseController() {
        
    }
    public void btnAvalar() { // TODO Refactorizar con cosulta directa en el facade
        List<TrabajogradoFase> lst = ejbFacadeTraFase.findAll();
        
        for(int i = 0; i < lst.size(); i++) {
            if(lst.get(i).getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                if(lst.get(i).getFaseid().getFaseid().equals(BigDecimal.valueOf(3))) {
                    lst.get(i).setEstado(BigInteger.ONE);
                    ejbFacadeTraFase.edit(lst.get(i));
                    return;
                }
            }
        }        
    }
}
