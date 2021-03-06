/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.UsuarioRol;
import java.math.BigInteger;
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
public class UsuarioRolFacade extends AbstractFacade<UsuarioRol> {
    @PersistenceContext(unitName = "tguPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioRolFacade() {
        super(UsuarioRol.class);
    }
    
    public List<UsuarioRol> findByUsuid_Rolid(int usuid, int rolid) {
        try {
            String queryString = "SELECT u FROM UsuarioRol u "
                    + "where u.personacedula=" + usuid + " and u.rolid=" + rolid;
            Query query = getEntityManager().createQuery(queryString);
            return query.getResultList();
        } finally {
            // em.close();
        }
    }
    
    public List<UsuarioRol> findByRolid(BigInteger rolid) {
        Query query = getEntityManager().createNamedQuery("UsuarioRol.findByRolid");
        query.setParameter("rolid", rolid);
        return query.getResultList();
    }
}
