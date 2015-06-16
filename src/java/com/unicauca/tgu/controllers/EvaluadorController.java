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
import com.unicauca.tgu.FormatosTablas.TablaPerfil;
import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
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
public class EvaluadorController {

    @EJB
    private ProductodetrabajoFacade productoTrabEJB;
    @EJB
    private UsuarioFacade ejbFacadeUsu;
    @EJB
    private UsuarioRolTrabajogradoFacade usuRolTgEJB;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTrabFase;
    @EJB
    private TrabajodegradoFacade ejbFacadetrabgrad;
    
    private List<TablaPerfil> trabajos;
    private Usuario evaluador;
    private String titulo;
    boolean modo;
    
    /**
     * Creates a new instance of CoordinadorProg
     */
    public EvaluadorController() {
    }

     @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s =  (ServiciosSimcaController)context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);
        evaluador = s.getUsulog();
        modo = false;
        titulo = "Trabajos de Grado en Curso";
    }   
    /*
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
    
     private boolean trabajoDeGradoEvaluado(String usuNombreCompleto, String productoContenido) {
        Gson gson = new Gson();

        Map<String, String> map = gson.fromJson(productoContenido, new TypeToken<Map<String, String>>() {
        }.getType());

        if (map.containsKey("evaluador")) {
            String nombre = map.get("evaluador");
            if (nombre.equals(usuNombreCompleto)) {
                return true;
            }
        }
        return false;
    }*/
    
     public List<TablaPerfil> getTrabsEvaluador()
     {

        trabajos = new ArrayList();
        List<Trabajodegrado> anteproystemp;

        if (modo) {
            anteproystemp = ejbFacadetrabgrad.getTrabajosTerminadosporEvaluadorId(evaluador.getPersonacedula().intValue());
        } else {
            anteproystemp = ejbFacadetrabgrad.getTrabajosEnCursoPorEvaluadorId(evaluador.getPersonacedula().intValue());
        }
        
        TablaPerfil f = new TablaPerfil();
        
        trabajos = f.llenarTabla(anteproystemp);
        
        return trabajos;
    }

    public void setAnteproyectos(List<TablaPerfil> trabajos) {
        this.trabajos = trabajos;
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
        
        TrabajodeGradoActual.fase = (Fase) event.getComponent().getAttributes().get("fase");
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect("../evaluador/fase-"+TrabajodeGradoActual.fase.getFaseorden().intValue()+".xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DirectorController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isModo() {
        return modo;
    }

    public void setModo(boolean modo) {
        this.modo = modo;
    }
    
    public void trabajosenCurso() {
        modo = false;
        titulo = "Trabajos de Grado en Curso";
    }

    public void trabajosTerminados() {
        modo = true;
        titulo = "Trabajos de Grado Terminados";
    }
}
