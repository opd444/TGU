/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.Productodetrabajo;
import java.util.List;
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
    
    
     public List<Productodetrabajo>  ObtenerProdsTrabajoPor_trabajoID_formatoID(int idtrabajo, int formato){
        try
        {
            String queryString = "SELECT t FROM Productodetrabajo t "+
            "where t.trabajoid="+idtrabajo+ "and t.formatoid="+formato;
            Query query = getEntityManager().createQuery(queryString);  
            return query.getResultList();       
        }finally 
        {
        } 
    }  
     
     public List<Productodetrabajo>  ObtenerProdsTrabajoPor_formatoID(int formato){
        try
        {
            String queryString = "SELECT t FROM Productodetrabajo t "+
            "where t.formatoid="+formato;
            Query query = getEntityManager().createQuery(queryString);  
            return query.getResultList();       
        }finally 
        {
        } 
    }
     
     public int Eliminar_prod(int prodid){
        try
        {
            String queryString = "DELETE FROM PRODUCTODETRABAJO "+
            " WHERE PRODUCTOID = "+prodid;
            Query query = getEntityManager().createNativeQuery(queryString);  
            return query.executeUpdate();
        }finally 
        {
        } 
    }
}
