/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.ServiciosSimcaController;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.FormatosTablas.FormatoTablaJefe;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class Evaluador {

    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private UsuarioFacade ejbFacadeUsu;
    
    @EJB
    private com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade ejbFacadeUsuRolTg;
    
    List<FormatoTablaJefe> trabajosporeval;
    
    private Usuario evaluador;
    /**
     * Creates a new instance of CoordinadorProg
     */
    public Evaluador() {
    }

     @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s =  (ServiciosSimcaController)context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);
        evaluador = s.getUsulog();       
    }   

    private boolean trabajoDeGradoAsignado(String usuNombre, String productoContenido) {
        Gson gson = new Gson();

        Map<String, String> map = gson.fromJson(productoContenido, new TypeToken<Map<String, String>>() {
        }.getType());

        if (map.containsKey("iddoc1")) {
            Usuario doc1 = ejbFacadeUsu.find(new BigDecimal(map.get("iddoc1")));
            if (doc1.getUsuarionombre().equals(usuNombre)) {
                return true;
            }
        }
        if (map.containsKey("iddoc2")) {
            Usuario doc2 = ejbFacadeUsu.find(new BigDecimal(map.get("iddoc2")));
            if (doc2.getUsuarionombre().equals(usuNombre)) {
                return true;
            }
        }

        return false;
    }

    public List<FormatoTablaJefe> getTrabajosporeval() {
       
        trabajosporeval = new ArrayList();
        
        List<Productodetrabajo> tem = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_formatoID(2);
        
         if (tem.size() > 0) {               
             
         int cont = 0;
         FormatoTablaJefe f;
             
             for(Productodetrabajo t : tem)
        {
            if(trabajoDeGradoAsignado(evaluador.getUsuarionombre(), t.getProductocontenido()))
          {          
            cont = 0;
            f = new FormatoTablaJefe();                  //sacamos la informacion general tanto jefe depto, director y los estud.
            
            List<UsuarioRolTrabajogrado> lst = ejbFacadeUsuRolTg.findbytrabajoId(t.getTrabajoid().getTrabajoid().intValue());
            
            if(lst.size() > 0)
            {
                f.setFecha(lst.get(0).getFechaasignacion());
                f.setTrabajoGradoId(lst.get(0).getTrabajoid().getTrabajoid().intValue());
                f.setTrabajoGrado(lst.get(0).getTrabajoid().getTrabajonombre());

                for(UsuarioRolTrabajogrado l : lst)
                {
                        if(l.getRolid().getRolid().intValue() == 0)  //director
                      {
                          f.setDirector(l.getPersonacedula().getPersonanombres()+" "+l.getPersonacedula().getPersonaapellidos());
                          f.setDirectorId(l.getPersonacedula().getPersonacedula().intValue());   
                      }   
                    else if(l.getRolid().getRolid().intValue() == 1 && cont ==0)           //Estudiante 1
                          { 
                           f.setEst1(l.getPersonacedula().getPersonanombres()+" "+l.getPersonacedula().getPersonaapellidos());
                           f.setEst1Id(l.getPersonacedula().getPersonacedula().intValue());
                           cont ++;
                          }
                      else if(l.getRolid().getRolid().intValue() == 1 && cont ==1)      //estudiante 2
                       { 
                           f.setEst2(l.getPersonacedula().getPersonanombres()+" "+l.getPersonacedula().getPersonaapellidos());
                           f.setEst2Id(l.getPersonacedula().getPersonacedula().intValue());                      
                       }
                }
                               
                trabajosporeval.add(f);
            }
          }
        }  
               
               }  
        
        return trabajosporeval;
    }

    public void setTrabajosporeval(List<FormatoTablaJefe> trabajosporeval) {
        this.trabajosporeval = trabajosporeval;
    }

    public void contenidoTgEvaluador(ActionEvent event) {
        
         //Agregamos los datos del trabajo de grado para no enviar por url.                          
        TrabajodeGradoActual.id =  (Integer)event.getComponent().getAttributes().get("idtrabajo");
        TrabajodeGradoActual.nombreTg = (String) event.getComponent().getAttributes().get("nombretrab");

        //Agregamos el primer estudiante a la clase estatica 
        int idusu = (Integer) event.getComponent().getAttributes().get("est1");
        if (idusu != -1) {
            TrabajodeGradoActual.est1 = ejbFacadeUsu.buscarporUsuid(idusu).get(0);
        }

        //Agregamos el segundo estudiante si hay uno
        idusu = (Integer) event.getComponent().getAttributes().get("est2");
        if (idusu != -1) {
            TrabajodeGradoActual.est2 = ejbFacadeUsu.buscarporUsuid(idusu).get(0);
        }

        idusu = (Integer)event.getComponent().getAttributes().get("iddirector");
        if(idusu!=-1)TrabajodeGradoActual.director = ejbFacadeUsu.buscarporUsuid(idusu).get(0);
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect("../evaluador/fases-trabajo-de-grado.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DirectorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
