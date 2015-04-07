/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jpacontroller;

import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import java.util.ArrayList;
import java.util.List;
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
    @PersistenceContext(unitName = "jpawebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrabajodegradoFacade() {
        super(Trabajodegrado.class);
    }

    public Trabajodegrado findbyNombreTg(String nombretg) {
       try
        {         
            String queryString = "SELECT t FROM Trabajodegrado t "+
            "where t.trabajonombre=:nombretg";
            TypedQuery<Trabajodegrado> query = getEntityManager().createQuery(queryString,Trabajodegrado.class);  
            query.setParameter("nombretg", nombretg);
            return query.getSingleResult();
        }finally
        {
           // em.close();
            //return null;
        }
    }
    
    public List<Trabajodegrado> getTrabajosTerminados()
             { 
                 return getTrabajosTerminadosoEncurso(1);
             }
    
    public List<Trabajodegrado> getTrabajosEnCurso()
             { 
                 return getTrabajosTerminadosoEncurso(0);
             }
    
    public List<Trabajodegrado> getTrabajosTerminadosoEncurso(int modo)  //1 para terminados y 0 para los que estan en curso
          { 
              List<Trabajodegrado> lst = findAll();
              List<Trabajodegrado> terminados = new ArrayList<Trabajodegrado>();
              List<Trabajodegrado> encurso = new ArrayList<Trabajodegrado>();
              boolean band;
              for(Trabajodegrado t:lst)
               {
                   band = false; 
                   for(TrabajogradoFase f:t.getTrabajogradoFaseList())
                 {
                    if(f.getEstado().intValue() == 0)
                        {
                            band=true;
                            break;
                        }
                 }
                   if(!band) terminados.add(t);
                   else encurso.add(t);
               } 
              if(modo==0)return encurso;
              else return terminados;
            }
    
     public List<Trabajodegrado> getTrabajosTerminadosporDirectorId(int idusu)
             { 
                 return getTrabajosTerminadosoEnCursoporDirectorId(idusu,1);
             }
    
     public List<Trabajodegrado> getTrabajosEnCursoPorDirectorId(int idusu)
             { 
                 return getTrabajosTerminadosoEnCursoporDirectorId(idusu,0);
             } 
    
     public List<Trabajodegrado> getTrabajosTerminadosoEnCursoporDirectorId(int idusu,int modo)
          {
              List<Trabajodegrado> lst;
              if(modo==1)
              lst = getTrabajosTerminados();
              else lst = getTrabajosEnCurso();
              
              List<Trabajodegrado> ret = new ArrayList();
                           
               for(Trabajodegrado t:lst)
               {
                 for(UsuarioRolTrabajogrado u: t.getUsuarioRolTrabajogradoList())
                   {
                       if(u.getPersonacedula().getPersonacedula().intValue()==idusu && u.getRolid().getRolid().intValue()==0)
                           {                               //filtramos trabajos de grado terminados por un director(IDrol = 0)
                             ret.add(t);
                             break;
                           }
                   }       
               }                           
          return ret;
          }
}
