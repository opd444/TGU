/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.Trabajodegrado;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author seven
 */
@Stateless
public class TrabajodegradoFacade extends AbstractFacade<Trabajodegrado> {
    @PersistenceContext(unitName = "jpawebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrabajodegradoFacade() {
        super(Trabajodegrado.class);
    }
    
}
