/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


/**
 *
 * @author seven
 */
@Stateless
public class UsuarioRolTrabajogradoFacade extends AbstractFacade<UsuarioRolTrabajogrado> {
    @PersistenceContext(unitName = "jpawebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioRolTrabajogradoFacade() {
        super(UsuarioRolTrabajogrado.class);
    }
    
    public List<UsuarioRolTrabajogrado> findbyUsuid(int usuid)
       {      
        try
        {
            String queryString = "SELECT t FROM UsuarioRolTrabajogrado t "+
            "where t.personacedula="+usuid;
            Query query = getEntityManager().createQuery(queryString);  
            //System.out.println("ERR"+Long.valueOf(usuid.intValue()+""));
            //query.set("usuid", Long.valueOf(usuid.intValue()+""));
            return query.getResultList();       
        }finally 
        {
           // em.close();
        } 
       }
    
    public List<UsuarioRolTrabajogrado> findbyRolId_TrabajoId(int idrol,int idtrab)
       {      
        try
        {
            String queryString = "SELECT t FROM UsuarioRolTrabajogrado t "+
            "where t.rolid="+idrol+" and t.trabajoid="+idtrab;
            Query query = getEntityManager().createQuery(queryString);  
            //System.out.println("ERR"+Long.valueOf(usuid.intValue()+""));
            //query.set("usuid", Long.valueOf(usuid.intValue()+""));
            return query.getResultList();       
        }catch(Exception e) 
        {
           // em.close();
            return null;
        } 
       }
    
    public List<TrabajogradoFase> findByTrabajogradoId(int tgid) {
       try
        {         
            String queryString = "SELECT t FROM TrabajogradoFase t "+
            "where t.trabajoid="+tgid;
            TypedQuery<TrabajogradoFase> query = getEntityManager().createQuery(queryString,TrabajogradoFase.class);  
            return query.getResultList();       
        }finally
        {
           // em.close();
            //return null;
        }
    }
}
