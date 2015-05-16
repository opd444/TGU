/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.UsuarioRol;
import com.unicauca.tgu.entities.UsuarioRolPK;
import com.unicauca.tgu.jpacontroller.exceptions.NonexistentEntityException;
import com.unicauca.tgu.jpacontroller.exceptions.PreexistingEntityException;
import com.unicauca.tgu.jpacontroller.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author seven
 */
public class UsuarioRolJpaController implements Serializable {

    public UsuarioRolJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioRol usuarioRol) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (usuarioRol.getUsuarioRolPK() == null) {
            usuarioRol.setUsuarioRolPK(new UsuarioRolPK());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(usuarioRol);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsuarioRol(usuarioRol.getUsuarioRolPK()) != null) {
                throw new PreexistingEntityException("UsuarioRol " + usuarioRol + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioRol usuarioRol) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            usuarioRol = em.merge(usuarioRol);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UsuarioRolPK id = usuarioRol.getUsuarioRolPK();
                if (findUsuarioRol(id) == null) {
                    throw new NonexistentEntityException("The usuarioRol with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UsuarioRolPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UsuarioRol usuarioRol;
            try {
                usuarioRol = em.getReference(UsuarioRol.class, id);
                usuarioRol.getUsuarioRolPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioRol with id " + id + " no longer exists.", enfe);
            }
            em.remove(usuarioRol);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioRol> findUsuarioRolEntities() {
        return findUsuarioRolEntities(true, -1, -1);
    }

    public List<UsuarioRol> findUsuarioRolEntities(int maxResults, int firstResult) {
        return findUsuarioRolEntities(false, maxResults, firstResult);
    }

    private List<UsuarioRol> findUsuarioRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioRol.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public UsuarioRol findUsuarioRol(UsuarioRolPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioRol.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioRol> rt = cq.from(UsuarioRol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
