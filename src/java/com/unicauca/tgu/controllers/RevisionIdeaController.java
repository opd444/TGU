/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.Formatoproducto;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author pcblanco
 */
@ManagedBean
@ViewScoped
public class RevisionIdeaController {

    private int numActa;
    private String nombretg;
    private String directortg;
    private String est1;
    private String est2;
    private int resultado;
    private String observaciones;
    private Productodetrabajo ideaActual;
    
    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;

    public RevisionIdeaController() {
    }

    @PostConstruct
    public void init() {

        List<Productodetrabajo> lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 1);
        if (lst.size() > 0) {
            ideaActual = lst.get(0);
        }
    }

    public int getNumActa() {
        return numActa;
    }

    public void setNumActa(int numActa) {
        this.numActa = numActa;
    }

    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public String getNombretg() {
        nombretg = TrabajodeGradoActual.nombreTg;
        return nombretg;
    }

    public void setNombretg(String nombretg) {
        this.nombretg = nombretg;
    }

    public String getDirectortg() {
        directortg = TrabajodeGradoActual.director.getPersonanombres() + " " + TrabajodeGradoActual.director.getPersonaapellidos();
        return directortg;
    }

    public void setDirectortg(String directortg) {
        this.directortg = directortg;
    }

    public String getEst1() {
        if (TrabajodeGradoActual.est1 != null) {
            est1 = TrabajodeGradoActual.est1.getPersonanombres() + " " + TrabajodeGradoActual.est1.getPersonaapellidos();
            return est1;
        } else {
            return "";
        }
    }

    public void setEst1(String est1) {
        this.est1 = est1;
    }

    public String getEst2() {
        if (TrabajodeGradoActual.est2 != null) {
            est2 = TrabajodeGradoActual.est2.getPersonanombres() + " " + TrabajodeGradoActual.est2.getPersonaapellidos();
            return est2;
        } else {
            return "";
        }
    }

    public void setEst2(String est2) {
        this.est2 = est2;
    }

    public void guardar() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Map<String, String> map = new HashMap<String, String>();

            map.put("numActa", ""+numActa);
            map.put("nombretg", nombretg);
            map.put("directortg", directortg);
            map.put("est1", est1);
            map.put("est2", est2);
            map.put("resultado", ""+resultado);
            map.put("obs", observaciones);
            
            Gson gson = new Gson();
            String contenido = gson.toJson(map, Map.class);

            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.valueOf(1)));
            prod.setTrabajoid(new Trabajodegrado(BigDecimal.valueOf(TrabajodeGradoActual.id)));

            ejbFacadeProdTrab.create(prod);
            context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Revision de la Idea diligenciada con Exito", ""));

            /*  Para redireccionar despues de guardar
             ExternalContext extcontext = FacesContext.getCurrentInstance().getExternalContext();
             extcontext.redirect("fases-trabajo-de-grado.xhtml?trabajoid="+TrabajodeGradoActual.id);
             */
        } catch (Exception e) {
            System.out.println(e.getMessage());
            context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ocurrio algun error al intentar  efectuar la operacion"));
        }
    }

    public void editar(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            Gson gson = new Gson();
            Map<String, String> mapedicion
                    = gson.fromJson(ideaActual.getProductocontenido(), new TypeToken<Map<String, String>>() {
                    }.getType());

            mapedicion.put("numActa", ""+numActa);
            mapedicion.put("resultado", ""+resultado);
            mapedicion.put("obs", observaciones);

            String contenido = gson.toJson(mapedicion, Map.class);
            ideaActual.setProductocontenido(contenido);

            ejbFacadeProdTrab.edit(ideaActual);

            context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Revision de la Idea Editada con Exito", ""));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ocurrio algun error al intentar  efectuar la operacion"));
        }
    }
}