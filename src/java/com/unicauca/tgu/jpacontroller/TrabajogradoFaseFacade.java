/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.TrabajogradoFase;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author seven
 */
@Stateless
public class TrabajogradoFaseFacade extends AbstractFacade<TrabajogradoFase> {
    @PersistenceContext(unitName = "tguPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrabajogradoFaseFacade() {
        super(TrabajogradoFase.class);
    }
    
    public List<TrabajogradoFase> ObtenerTrabajoFrasePor_trabajoID_faseID(int idtrabajo, int idfase){
        try
        {
            String queryString = "SELECT t FROM TrabajogradoFase t " +
            "WHERE t.faseid=" + idfase + " and t.trabajoid=" + idtrabajo;
            Query query = getEntityManager().createQuery(queryString);  
            return query.getResultList();       
        }finally 
        {
        } 
    }
    
    public List<TrabajogradoFase> ObtenerTrabajoFrasePor_trabajoID(int idtrabajo){
        try
        {
            String queryString = "SELECT t FROM TrabajogradoFase t " +
            "WHERE t.trabajoid=" + idtrabajo;
            Query query = getEntityManager().createQuery(queryString);  
            return query.getResultList();       
        }finally 
        {
        }
    }
}
