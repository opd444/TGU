package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.FormatosTablas.TablaPerfil;
import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;


@ManagedBean
@ViewScoped
public class CoordinadorController {

    @EJB
    private UsuarioFacade ejbFacadeusuario;

    @EJB
    private TrabajodegradoFacade ejbFacadetrabgrad;

    private List<TablaPerfil> anteproys;
    private boolean modo;    // 0 SIN ASIGNAER EVAL, 1 ASIGNADOS
    private String titulo;

    public CoordinadorController() {
        titulo = "Trabajos de Grado en Curso";
        modo = false;
    }

    public List<TablaPerfil> getAnteproys() {

        anteproys = new ArrayList();

        //List<Productodetrabajo> tem = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_formatoID(2);

        List<Trabajodegrado> anteproystemp;

        if (modo) {
            anteproystemp = ejbFacadetrabgrad.getTrabajosTerminados();
        } else {
            anteproystemp = ejbFacadetrabgrad.getTrabajosEnCurso();
        }
        TablaPerfil f = new TablaPerfil();
        anteproys = f.llenarTabla(anteproystemp);
        return anteproys;
    }

    public void setAnteproys(List<TablaPerfil> anteproys) {
        this.anteproys = anteproys;
    }
    
    private boolean trabajoDeGradoAvalado(String productoContenido) {
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(productoContenido, new TypeToken<Map<String, String>>() {
        }.getType());
        
        if(map.get("avalado").equals("1"))
            return true;
        else
            return false;
    }

    private boolean trabajoDeGradoAsignado(String productoContenido) {
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(productoContenido, new TypeToken<Map<String, String>>() {
        }.getType());

        return map.containsKey("iddoc1") || map.containsKey("iddoc2");
    }

    public void contenidoTgCoordinador(ActionEvent event) //guardar informacion del trabajo de grado que se esta tratando
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
            context.redirect("../coordinador/fase-"+TrabajodeGradoActual.fase.getFaseorden().intValue()+".xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DirectorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void trabajosenCurso() {
        modo = false;
        titulo = "Trabajos de Grado en Curso";
    }

    public void trabajosTerminados() {
        modo = true;
        titulo = "Trabajos de Grado Terminados";
    }

    public boolean getModo() {
        return modo;
    }

    public void setModo(boolean modo) {
        this.modo = modo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
