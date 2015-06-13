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
import com.unicauca.tgu.entities.Formatoproducto;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
public class ActaSustentacionController {

    private String nombreTrabajodeGrado;
    private String nombreEst1;
    private String nombreDirector;
    private String nombreEst2;
    private Date fechasus;
    private String aprobado;
    private Date fechafirm;
    private String nomJurado1;
    private String nomJurado2;
    private String numRecibo;
    private Productodetrabajo productoActual;

    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;

    @EJB
    private TrabajogradoFaseFacade ejbFacadeTraFase;
    
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuroltrab;

    public ActaSustentacionController() {
    }

    @PostConstruct
    public void init() {
        nombreTrabajodeGrado = TrabajodeGradoActual.nombreTg;
        nombreEst1 = TrabajodeGradoActual.est1.getPersonanombres() + " " + TrabajodeGradoActual.est1.getPersonaapellidos();
        nombreDirector = TrabajodeGradoActual.director.getPersonanombres() + " " + TrabajodeGradoActual.director.getPersonaapellidos();

        if (TrabajodeGradoActual.est2 != null) {
            nombreEst2 = TrabajodeGradoActual.est2.getPersonanombres() + " " + TrabajodeGradoActual.est2.getPersonaapellidos();
        }

        List<UsuarioRolTrabajogrado> jurados = ejbFacadeUsuroltrab.findbyRolId_TrabajoId(7, TrabajodeGradoActual.id);
        
        if(jurados.size()>0)
            nomJurado1 =  jurados.get(0).getPersonacedula().getPersonanombres() + " " + jurados.get(0).getPersonacedula().getPersonaapellidos();
        
        if(jurados.size()>1)
            nomJurado2 =  jurados.get(1).getPersonacedula().getPersonanombres() + " " + jurados.get(1).getPersonacedula().getPersonaapellidos();
        
        fechasus = new Date();
        fechafirm = new Date();

        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s = (ServiciosSimcaController) context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);

        List<Productodetrabajo> lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 9);

        if (lst.size() > 0) {               //verificar si ya hay guardardo un producto para este trabajo de grado

            productoActual = lst.get(0);
            Gson gson = new Gson();
            Map<String, String> decoded = gson.fromJson(productoActual.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());

            if (decoded.containsKey("nombretg")) {
                nombreTrabajodeGrado = decoded.get("nombretg");
            }

            if (decoded.containsKey("concepto")) {
                aprobado = decoded.get("concepto");
            }

            if (decoded.containsKey("numrecibo")) {
                numRecibo = decoded.get("numrecibo");
            }

            if (decoded.containsKey("fechasus")) {
                SimpleDateFormat formateador = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                try {
                    fechasus = (Date) formateador.parse(decoded.get("fechasus"));
                } catch (ParseException ex) {
                    Logger.getLogger(FormatoA.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (decoded.containsKey("fechafirm")) {
                SimpleDateFormat formateador = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                try {
                    fechafirm = (Date) formateador.parse(decoded.get("fechafirm"));
                } catch (ParseException ex) {
                    Logger.getLogger(FormatoA.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public String getNombreTrabajodeGrado() {
        return nombreTrabajodeGrado;
    }

    public Date getFechasus() {
        return fechasus;
    }

    public void setFechasus(Date fechasus) {
        this.fechasus = fechasus;
    }

    public Date getFechafirm() {
        return fechafirm;
    }

    public String getNombreEst1() {
        return nombreEst1;
    }

    public String getNombreEst2() {
        return nombreEst2;
    }

    public String getNombreDirector() {
        return nombreDirector;
    }

    public void setFechafirm(Date fechafirm) {
        this.fechafirm = fechafirm;
    }

    public String getNomJurado1() {
        return nomJurado1;
    }

    public void setNomJurado1(String nomJurado1) {
        this.nomJurado1 = nomJurado1;
    }

    public String getNomJurado2() {
        return nomJurado2;
    }

    public String getAprobado() {
        return aprobado;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }

    public void setNomJurado2(String nomJurado2) {
        this.nomJurado2 = nomJurado2;
    }

    public String getNumRecibo() {
        return numRecibo;
    }

    public void setNumRecibo(String numRecibo) {
        this.numRecibo = numRecibo;
    }

    public String obtenerDatos() {

        Map<String, String> map = new HashMap<>();

        map.put("nombretg", TrabajodeGradoActual.nombreTg);

        map.put("nombredirector", nombreDirector);

        map.put("fechasus", fechasus.toString());

        map.put("fechafirm", fechafirm.toString());
        map.put("concepto", aprobado);
        map.put("numrecibo", numRecibo);

        Gson gson = new Gson();
        String contenido = gson.toJson(map, Map.class);

        return contenido;
    }

    public String guardar() {
        try {

            String contenido = obtenerDatos();
            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);

            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.valueOf(9)));
            prod.setTrabajoid(trab);
            ejbFacadeProdTrab.create(prod);

            if (aprobado.equals("Aprobado")) {

                List<TrabajogradoFase> lst = ejbFacadeTraFase.ObtenerTrabajoFrasePor_trabajoID(TrabajodeGradoActual.id);

                for (TrabajogradoFase l : lst) {

                    if (l.getFaseid().getFaseid().equals(BigDecimal.valueOf(5))) {
                        l.setEstado(BigInteger.ONE);
                        ejbFacadeTraFase.edit(l);
                    }
                    if (l.getFaseid().getFaseid().equals(BigDecimal.valueOf(6))) {
                        l.setEstado(BigInteger.ZERO);
                        ejbFacadeTraFase.edit(l);
                    }
                }
            }
            
            

//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("La revision de la idea del Trabajo de Grado: '"+nombretg+"' ha sido diligenciada.");
//
//            if(est1!=null)
//            {  
//            se.setTo(est1.getPersonacorreo());
//            se.enviarDiligenciadoRevisionIdea(nombretg);
//            }
//            if(est2!=null)
//            {
//            se.setTo(est2.getPersonacorreo());
//            se.enviarDiligenciadoRevisionIdea(nombretg);
//            }
//            if(TrabajodeGradoActual.director!=null)
//            {
//            se.setTo(TrabajodeGradoActual.director.getPersonacorreo());
//            se.enviarDiligenciadoRevisionIdea(nombretg);
//            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Acta de Sustentación diligenciada con éxito.", ""));
            return "fase-5";
        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocurrio un problema al efectuar dicha operación.", ""));
            return "editar-acta-sustentacion";
        }
    }
    
    public String editar() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {

            if (aprobado.equals("Aprobado")) {

                List<TrabajogradoFase> lst = ejbFacadeTraFase.ObtenerTrabajoFrasePor_trabajoID(TrabajodeGradoActual.id);

                for (TrabajogradoFase l : lst) {

                    if (l.getFaseid().getFaseid().equals(BigDecimal.valueOf(5))) {
                        l.setEstado(BigInteger.ONE);
                        ejbFacadeTraFase.edit(l);
                    }
                    if (l.getFaseid().getFaseid().equals(BigDecimal.valueOf(6))) {
                        l.setEstado(BigInteger.ZERO);
                        ejbFacadeTraFase.edit(l);
                    }
                }
            }
            else {

                List<TrabajogradoFase> lst = ejbFacadeTraFase.ObtenerTrabajoFrasePor_trabajoID(TrabajodeGradoActual.id);

                for (TrabajogradoFase l : lst) {

                    if (l.getFaseid().getFaseid().equals(BigDecimal.valueOf(5))) {
                        l.setEstado(BigInteger.ZERO);
                        ejbFacadeTraFase.edit(l);
                    }
                    if (l.getFaseid().getFaseid().equals(BigDecimal.valueOf(6))) {
                        l.setEstado(BigInteger.ZERO);
                        ejbFacadeTraFase.edit(l);
                    }
                }
            }
            
            productoActual.setProductocontenido(obtenerDatos());

            ejbFacadeProdTrab.edit(productoActual);
            

//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("Anteproyecto del Trabajo de Grado" + nombretg + " Editado");
//
//            if (TrabajodeGradoActual.director != null) {
//                se.setTo(TrabajodeGradoActual.director.getPersonacorreo());
//                se.enviarEditadoAnteproyecto(nombretg);
//            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Acta de sustentación editado con éxito."));
            return "fase-5";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocurrio un problema al efectuar dicha operación.", ""));
            return "editar-acta-sustentacion";
        }
    }

}
