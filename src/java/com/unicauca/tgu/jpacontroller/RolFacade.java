/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.Rol;
import java.math.BigInteger;
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
public class RolFacade extends AbstractFacade<Rol> {

    @PersistenceContext(unitName = "tguPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RolFacade() {
        super(Rol.class);
    }

    public Rol buscarPorRolId(BigInteger rolid) {
        try {
            String queryString = ""
                    + "SELECT r "
                    + "FROM rol r "
                    + "where r.rolid = " + rolid;
            
            TypedQuery<Rol> query = getEntityManager().createQuery(queryString,Rol.class);  
            return query.getSingleResult();
        } finally {
            // em.close();
        }        
    }
}
