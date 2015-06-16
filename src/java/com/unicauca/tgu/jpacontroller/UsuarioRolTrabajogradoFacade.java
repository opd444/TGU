/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.Productodetrabajo;
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

    @PersistenceContext(unitName = "tguPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioRolTrabajogradoFacade() {
        super(UsuarioRolTrabajogrado.class);
    }

    public List<UsuarioRolTrabajogrado> findbyUsuid(int usuid) {
        try {
            String queryString = "SELECT t FROM UsuarioRolTrabajogrado t "
                    + "where t.personacedula=" + usuid;
            Query query = getEntityManager().createQuery(queryString);
            //System.out.println("ERR"+Long.valueOf(usuid.intValue()+""));
            //query.set("usuid", Long.valueOf(usuid.intValue()+""));
            return query.getResultList();
        } finally {
            // em.close();
        }
    }

    public List<UsuarioRolTrabajogrado> findbyRolId_TrabajoId(int idrol, int idtrab) {
        try {
            String queryString = "SELECT t FROM UsuarioRolTrabajogrado t "
                    + "where t.rolid=" + idrol + " and t.trabajoid=" + idtrab;
            Query query = getEntityManager().createQuery(queryString);
            //System.out.println("ERR"+Long.valueOf(usuid.intValue()+""));
            //query.set("usuid", Long.valueOf(usuid.intValue()+""));
            return query.getResultList();
        } catch (Exception e) {
            // em.close();
            return null;
        }
    }

    public List<TrabajogradoFase> findByTrabajogradoId(int tgid) {
        try {
            String queryString = "SELECT t FROM TrabajogradoFase t "
                    + "where t.trabajoid=" + tgid;
            TypedQuery<TrabajogradoFase> query = getEntityManager().createQuery(queryString, TrabajogradoFase.class);
            return query.getResultList();
        } finally {
            // em.close();
            //return null;
        }
    }

    //cepeda
    public List<UsuarioRolTrabajogrado> findbyRolId(int idrol) {
        try {
            String queryString = "SELECT t FROM UsuarioRolTrabajogrado t "
                    + "where t.rolid=" + idrol;
            Query query = getEntityManager().createQuery(queryString);
            //System.out.println("ERR"+Long.valueOf(usuid.intValue()+""));
            //query.set("usuid", Long.valueOf(usuid.intValue()+""));
            return query.getResultList();
        } catch (Exception e) {
            // em.close();
            return null;
        }
    }

    public List<Productodetrabajo> obtenerFormatosporId(int idformato, int aprobado)//0 para obtener todos 1 obtener solo aprobados 2 obtener no aprobados
    {
        try {
            String queryString;
            if (aprobado == 0) {
                queryString = "SELECT * FROM Productodetrabajo t "
                        + "where t.formatoid=" + idformato;
            } else if (aprobado == 1) {
                queryString = "SELECT * FROM Productodetrabajo t "
                        + "where t.formatoid=" + idformato + " and " + "t.productoAprobado= 1";
            } else {
                queryString = "SELECT * FROM Productodetrabajo t "
                        + "where t.formatoid=" + idformato + " and " + "t.productoAprobado= 0";
            }
            Query query = getEntityManager().createNativeQuery(queryString, Productodetrabajo.class);
            //System.out.println("ERR"+Long.valueOf(usuid.intValue()+""));
            //query.set("usuid", Long.valueOf(usuid.intValue()+""));
            return query.getResultList();

        } catch (Exception e) {
            // em.close();
            return null;
        }
    }

    public List<UsuarioRolTrabajogrado> findbytrabajoId(int trabajoid) {
        try {
            String queryString = "SELECT t FROM UsuarioRolTrabajogrado t "
                    + "where t.trabajoid=" + trabajoid;
            Query query = getEntityManager().createQuery(queryString);
            //System.out.println("ERR"+Long.valueOf(usuid.intValue()+""));
            //query.set("usuid", Long.valueOf(usuid.intValue()+""));
            return query.getResultList();
        } catch (Exception e) {
            // em.close();
            return null;
        }
    }

    public List<UsuarioRolTrabajogrado> findByUsuid_Rolid(int usuid, int idrol) {
        try {
            String queryString = "SELECT t FROM UsuarioRolTrabajogrado t "
                    + "where t.personacedula=" + usuid + " and t.rolid=" + idrol;
            Query query = getEntityManager().createQuery(queryString);
            return query.getResultList();
        } finally {
            // em.close();
        }
    }
    
    public List<UsuarioRolTrabajogrado> findByUsuid_Rolid_Trabid(int usuid, int idrol, int idtrab) {
        try {
            String queryString = "SELECT t FROM UsuarioRolTrabajogrado t "
                    + "where t.personacedula=" + usuid + " and t.rolid=" + idrol + " and t.trabajoid=" +idtrab;
            Query query = getEntityManager().createQuery(queryString);
            return query.getResultList();
        } finally {
            // em.close();
        }
    }
    
    public List<UsuarioRolTrabajogrado> findbyRolId_TrabId(int usuid, int trabid) {
        try {
            String queryString = "SELECT t FROM UsuarioRolTrabajogrado t "
                    + "where t.personacedula=" + usuid + " and t.trabajoid=" + trabid;
            Query query = getEntityManager().createQuery(queryString);
            return query.getResultList();
        } finally {
            // em.close();
        }
    }
}
