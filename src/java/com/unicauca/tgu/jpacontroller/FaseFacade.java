/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.Fase;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author seven
 */
@Stateless
public class FaseFacade extends AbstractFacade<Fase> {
    @PersistenceContext(unitName = "tguPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FaseFacade() {
        super(Fase.class);
    }
    
    public List<Fase> faseOrderByOrden() {
        try {
            String queryString = "SELECT f FROM Fase f ORDER BY f.faseorden ASC";

            TypedQuery<Fase> query = getEntityManager().createQuery(queryString, Fase.class);
            return query.getResultList();
        } finally {
            // em.close();
            //return null;
        }
    }
}
