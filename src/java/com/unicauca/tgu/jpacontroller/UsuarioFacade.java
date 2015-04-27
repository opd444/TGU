/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.Usuario;
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
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "tguPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public List<Usuario> buscarEstudiantesDisponibles(int iddirector, String consulta) {
        try {
            String queryString = "SELECT * FROM Usuario "
                    + "where personacedula!=" + iddirector + " and upper(personanombres)||' '||upper(personaapellidos)"
                    + " like '%" + consulta + "%'";
            Query query = getEntityManager().createNativeQuery(queryString, Usuario.class);
            //System.out.println("ERR"+Long.valueOf(usuid.intValue()+""));
            //query.set("usuid", Long.valueOf(usuid.intValue()+""));
            return query.getResultList();
        } finally {
            // em.close();
        }
    }

    public List<Usuario> buscarporUsuid(int usuid) {
        try {
            String queryString = "SELECT t FROM Usuario t "
                    + "where t.personacedula=" + usuid;
            Query query = getEntityManager().createQuery(queryString);
            //System.out.println("ERR"+Long.valueOf(usuid.intValue()+""));
            //query.set("usuid", Long.valueOf(usuid.intValue()+""));
            return query.getResultList();
        } finally {
            // em.close();
        }
    }
    public Usuario buscarPorUsuarionombre(String usuarionombre) {
        try {
            String queryString = "SELECT t FROM Usuario t "
                    + "where t.usuarionombre='" + usuarionombre+"'";
            TypedQuery<Usuario> query = getEntityManager().createQuery(queryString,Usuario.class);  
            return query.getSingleResult();
        } finally {
            // em.close();
        }        
    }
}
