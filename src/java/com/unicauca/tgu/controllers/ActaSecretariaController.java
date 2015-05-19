/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.FormatosTablas.FormatoTablaActa;
import com.unicauca.tgu.entities.Formatoproducto;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author pcblanco
 */
@ManagedBean
@ViewScoped
public class ActaSecretariaController {

    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;

    private String nombreTrabajodeGrado;
    private String nombreDirector;
    private Date fechaact;
    private int numact;  

    /**
     * Creates a new instance of ActaSecretariaController
     */
    public ActaSecretariaController() {
        nombreDirector = TrabajodeGradoActual.director.getPersonanombres() + " " + TrabajodeGradoActual.director.getPersonaapellidos();
        nombreTrabajodeGrado = TrabajodeGradoActual.nombreTg;
    }

    public String getNombreTrabajodeGrado() {
        return nombreTrabajodeGrado;
    }

    public void setNombreTrabajodeGrado(String nombreTrabajodeGrado) {
        this.nombreTrabajodeGrado = nombreTrabajodeGrado;
    }

    public String getNombreDirector() {
        return nombreDirector;
    }

    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
    }

    public Date getFechaact() {
        return fechaact;
    }

    public void setFechaact(Date fechaact) {
        this.fechaact = fechaact;
    }

    public int getNumact() {
        return numact;
    }

    public void setNumact(int numact) {
        this.numact = numact;
    }

    public String obtenerDatos() {

        Map<String, String> map = new HashMap<>();

        map.put("numacta", Integer.toString(numact));

        map.put("nombretg", TrabajodeGradoActual.nombreTg);

        map.put("nombredirector", nombreDirector);

        map.put("fecha", fechaact.toString());

        Gson gson = new Gson();
        String contenido = gson.toJson(map, Map.class);

        return contenido;
    }

    public void guardar() {
        try {

            String contenido = obtenerDatos();
            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);

            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.valueOf(5)));
            prod.setTrabajoid(trab);
            ejbFacadeProdTrab.create(prod);

            /*
             Servicio_Email se = new Servicio_Email();
             se.setSubject("La revision de la idea del Trabajo de Grado: '"+nombretg+"' ha sido diligenciada.");

             if(est1!=null)
             {  
             se.setTo(est1.getPersonacorreo());
             se.enviarDiligenciadoRevisionIdea(nombretg);
             }
             if(est2!=null)
             {
             se.setTo(est2.getPersonacorreo());
             se.enviarDiligenciadoRevisionIdea(nombretg);
             }
             if(TrabajodeGradoActual.director!=null)
             {
             se.setTo(TrabajodeGradoActual.director.getPersonacorreo());
             se.enviarDiligenciadoRevisionIdea(nombretg);
             }
             */
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "¡Acta diligenciada con éxito!", "Se le ha enviado un correo a los involucrados en el trabajo de grado."));
        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "¡Error!", "Lo sentimos, no se pudo guardar el Acta."));
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public List<FormatoTablaActa> getActas() {
        
        List<Productodetrabajo> lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 5);
        List<FormatoTablaActa> lstact = new ArrayList<>();
        FormatoTablaActa act;
        
        Gson gson = new Gson();
        Map<String, String> map;

        for (Productodetrabajo p : lst) {
            map = gson.fromJson(p.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());

            act = new FormatoTablaActa(p.getProductoid().intValue(),Integer.parseInt(map.get("numacta")),map.get("fecha"));
            lstact.add(act);
        }
        return lstact;
    }

}
