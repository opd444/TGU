/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author seven
 */
@Stateless
public class TrabajodegradoFacade extends AbstractFacade<Trabajodegrado> {
    
    @EJB
    UsuarioRolTrabajogradoFacade ejbFacade;

    
    @PersistenceContext(unitName = "tguPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrabajodegradoFacade() {
        super(Trabajodegrado.class);
    }

    public Trabajodegrado findbyNombreTg(String nombretg) {
        try {
            String queryString = "SELECT t FROM Trabajodegrado t "
                    + "where t.trabajonombre=:nombretg";
            TypedQuery<Trabajodegrado> query = getEntityManager().createQuery(queryString, Trabajodegrado.class);
            query.setParameter("nombretg", nombretg);
            return query.getSingleResult();
        } finally {
            // em.close();
            //return null;
        }
    }

   

    public List<Trabajodegrado> getTrabajosTerminadosporDirectorId(int idusu) {
        return getTrabajosTerminadosoEnCursoporRolId(idusu, 1, 0);
    }

    public List<Trabajodegrado> getTrabajosEnCursoPorDirectorId(int idusu) {
        return getTrabajosTerminadosoEnCursoporRolId(idusu, 0, 0);
    }
    
    public List<Trabajodegrado> getTrabajosTerminadosporJuradoId(int idusu) {
        return getTrabajosTerminadosoEnCursoporRolId(idusu, 1, 7);
    }

    public List<Trabajodegrado> getTrabajosEnCursoPorJuradoId(int idusu) {
        return getTrabajosTerminadosoEnCursoporRolId(idusu, 0, 7);
    }
    
    public List<Trabajodegrado> getTrabajosTerminadosporEvaluadorId(int idusu) {
        return getTrabajosTerminadosoEnCursoporRolId(idusu, 1, 4);
    }

    public List<Trabajodegrado> getTrabajosEnCursoPorEvaluadorId(int idusu) {
        return getTrabajosTerminadosoEnCursoporRolId(idusu, 0, 4);
    }
    
    public List<Trabajodegrado> getTrabajosTerminadosporSecretariaId(int idusu) {
        return getTrabajosTerminadosoEnCursoporRolId(idusu, 1, 6);
    }  
    
    public List<Trabajodegrado> getTrabajosTerminadosoEnCursoporRolId(int idusu, int modo,int rol) {
        List<Trabajodegrado> lst;
        if (modo == 1) {
            lst = getTrabajosTerminados();
        } else {
            lst = getTrabajosEnCurso();
        }

        List<Trabajodegrado> ret = new ArrayList();

        for (Trabajodegrado t : lst) {
            for (UsuarioRolTrabajogrado u : t.getUsuarioRolTrabajogradoList()) {
                if (u.getPersonacedula().getPersonacedula().intValue() == idusu && u.getRolid().getRolid().intValue() == rol) {                               //filtramos trabajos de grado terminados por un director(IDrol = 0)
                    ret.add(t);
                    break;
                }
            }
        }
        return ret;
    }
    
     public List<Trabajodegrado> getTrabajosTerminados() {
        return getTrabajosTerminadosoEncurso(1);
    }

    public List<Trabajodegrado> getTrabajosEnCurso() {
        return getTrabajosTerminadosoEncurso(0);
    }

    public List<Trabajodegrado> getTrabajosTerminadosoEncurso(int modo) //1 para terminados y 0 para los que estan en curso
    {
        List<Trabajodegrado> lst = findAll();
        List<Trabajodegrado> terminados = new ArrayList<Trabajodegrado>();
        List<Trabajodegrado> encurso = new ArrayList<Trabajodegrado>();
        boolean band;
        for (Trabajodegrado t : lst) {
            band = false;
            for (TrabajogradoFase f : t.getTrabajogradoFaseList()) {
                if( f.getFaseid().getFaseid().intValue() == 5 && f.getEstado().intValue() == 1)break;
                if( f.getEstado().intValue() == 0) {
                    band = true;
                    break;
                }
            }
            t.getUsuarioRolTrabajogradoList().size();
            t.getProductodetrabajoList().size();
            if (!band) {
                terminados.add(t);
            } else {
                encurso.add(t);
            }
        }
        if (modo == 0) {
            return encurso;
        } else {
            return terminados;
        }
    }
}
