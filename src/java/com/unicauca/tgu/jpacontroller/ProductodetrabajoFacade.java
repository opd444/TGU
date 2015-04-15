/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.Productodetrabajo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author seven
 */
@Stateless
public class ProductodetrabajoFacade extends AbstractFacade<Productodetrabajo> {
    @PersistenceContext(unitName = "tguPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductodetrabajoFacade() {
        super(Productodetrabajo.class);
    }
    
    public int crearTrabajoGrado(String nombretg)
          {
            try
        {
            String queryString = "INSERT INTO Trabajodegrado(trabajoid,trabajonombre) values(null,'"+nombretg+"')";
            Query query = getEntityManager().createNativeQuery(queryString);  
            int k = query.executeUpdate();
            
            if(k>0)
                 {
                 
                 }
            //System.out.println("ERR"+Long.valueOf(usuid.intValue()+""));
            //query.set("usuid", Long.valueOf(usuid.intValue()+""));
            return 0;     
        }finally 
        {
           // em.close();
        } 
          }   
}
