/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.unicauca.tgu.Auxiliares.ServiciosSimcaController;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.FormatosTablas.TablaPerfil;
import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * @author Orlando
 */
@ManagedBean
@ViewScoped
public class JuradoController {

    @EJB
    private TrabajodegradoFacade ejbFacadetrabgrad;
    @EJB
    private UsuarioFacade ejbFacadeusuario;

    private List<TablaPerfil> trabs;
    //int modo;             // 0 para la seccion de trabajos en curso y 1 para trabajos terminados
    private boolean modo;             // 0 para la seccion de trabajos en curso y 1 para trabajos terminados
    private String titulotablaJurado;
    private Date fecha;
    private int filas;
    private int idJurado;

    public JuradoController() {
    }

    @PostConstruct
    public void init() {
        modo = false;
        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s = (ServiciosSimcaController) context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);
        idJurado = s.getUsulog().getPersonacedula().intValue();
        titulotablaJurado = "Trabajos de Grado en Curso";
        fecha = new Date();
    }

    public List<TablaPerfil> getTrabsJurado() {
        trabs = new ArrayList();

        List<Trabajodegrado> trabstemp;

        if (modo == true) {
            trabstemp = ejbFacadetrabgrad.getTrabajosTerminadosporJuradoId(idJurado);
        } else {
            trabstemp = ejbFacadetrabgrad.getTrabajosEnCursoPorJuradoId(idJurado);
        }

        TablaPerfil f = new TablaPerfil();

        trabs = f.llenarTabla(trabstemp);

        return trabs;
    }

    public void setTrabs(List<TablaPerfil> trabs) {
        this.trabs = trabs;
    }

    public void trabajosenCurso() {
        modo = false;
        titulotablaJurado = "Trabajos de Grado en Curso";
    }

    public void trabajosTerminados() {
        modo = true;
        titulotablaJurado = "Trabajos de Grado Terminados";
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isModo() {
        return modo;
    }

    public void setModo(boolean modo) {
        this.modo = modo;
    }

    public void contenidoTgJurado(ActionEvent event) //guardar informacion del trabajo de grado que se esta tratando
    {
        //Agregamos los datos del trabajo de grado para no enviar por url.                          
        TrabajodeGradoActual.id = (Integer) event.getComponent().getAttributes().get("idtrabajo");
        TrabajodeGradoActual.nombreTg = (String) event.getComponent().getAttributes().get("nombretrab");

        //Agregamos el primer estudiante a la clase estatica 
        int idusu = (Integer) event.getComponent().getAttributes().get("est1");
        if (idusu != -1) {
            TrabajodeGradoActual.est1 = ejbFacadeusuario.buscarporUsuid(idusu).get(0);
        }

        //Agregamos el segundo estudiante si hay uno
        idusu = (Integer) event.getComponent().getAttributes().get("est2");
        if (idusu != -1) {
            TrabajodeGradoActual.est2 = ejbFacadeusuario.buscarporUsuid(idusu).get(0);
        }

        idusu = (Integer) event.getComponent().getAttributes().get("iddirector");
        if (idusu != -1) {
            TrabajodeGradoActual.director = ejbFacadeusuario.buscarporUsuid(idusu).get(0);
        }      
        
        TrabajodeGradoActual.fase = (Fase) event.getComponent().getAttributes().get("fase");

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect("../jurado/fase-" + TrabajodeGradoActual.fase.getFaseorden().intValue() + ".xhtml");
        } catch (IOException ex) {
            Logger.getLogger(JuradoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public void numTrabajos() {
        if (modo == true) {
            filas = 8;
        } else {
            filas = 3;
        }
    }

    public String getTitulotablaJurado() {
        return titulotablaJurado;
    }

}
