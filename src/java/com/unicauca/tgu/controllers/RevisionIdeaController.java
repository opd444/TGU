/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.Servicio_Email;
import com.unicauca.tgu.Auxiliares.ServiciosSimcaController;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.controllers.util.JsfUtil;
import com.unicauca.tgu.entities.Formatoproducto;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuroltrab;

    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;

    @EJB
    private TrabajodegradoFacade ejbFacadeTrabGrad;

    @EJB
    private UsuarioFacade ejbFacadeUsuario;
    
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTrabajoGradFase;

    private int numActa;
    private String nombretg;
    private BigDecimal trabajoid;
    private Usuario est1;
    private Usuario est2;
    private String nombreDirector;
    private int resultado;
    private String observaciones;
    private Date fecha;
    private String aprobado;
    private int idjefe;
    
    private Productodetrabajo formatoactual;
    
    public RevisionIdeaController() {
    }

    @PostConstruct
    public void init() {
                
        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s =  (ServiciosSimcaController)context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);
        idjefe = s.getUsulog().getPersonacedula().intValue();
                
        numActa = 1;
        resultado = 1;
        
        List<Productodetrabajo> lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 1);

        if (lst.size() > 0) {               //verificar si ya hay guardardo el formato de revisión del formato A para este trabajo de grado

            formatoactual = lst.get(0);

            Gson gson = new Gson();
            Map<String, String> decoded = gson.fromJson(formatoactual.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());
            
            if (decoded.get("numActa") != null) {
                resultado = Integer.parseInt(decoded.get("numActa"));
            }
            if (decoded.get("nombre") != null) {
                nombretg = decoded.get("nombre");
            }
//        //decoded.get("idestud1");
            if (decoded.get("idestud1") != null) {
                int x = Integer.parseInt(decoded.get("idestud1"));
                est1 = ejbFacadeUsuario.find(BigDecimal.valueOf(x));
            }
//        //decoded.get("idestud2");
            if (decoded.get("idestud2") != null) {
                int x = Integer.parseInt(decoded.get("idestud2"));
                est2 = ejbFacadeUsuario.find(BigDecimal.valueOf(x));
            }
//        //decoded.get("iddirector");
            if (decoded.get("nombredirector") != null) {
                nombreDirector = decoded.get("nombredirector");
            }
            if (decoded.get("resultado") != null) {
                resultado = Integer.parseInt(decoded.get("resultado"));
            }
            if (decoded.get("observaciones") != null) {
                observaciones = decoded.get("observaciones");
            }
        }
        
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        fecha = c.getTime();
    }

    public UsuarioRolTrabajogradoFacade getEjbFacadeUsuroltrab() {
        return ejbFacadeUsuroltrab;
    }

    public void setEjbFacadeUsuroltrab(UsuarioRolTrabajogradoFacade ejbFacadeUsuroltrab) {
        this.ejbFacadeUsuroltrab = ejbFacadeUsuroltrab;
    }

    public ProductodetrabajoFacade getEjbFacadeProdTrab() {
        return ejbFacadeProdTrab;
    }

    public void setEjbFacadeProdTrab(ProductodetrabajoFacade ejbFacadeProdTrab) {
        this.ejbFacadeProdTrab = ejbFacadeProdTrab;
    }

    public TrabajodegradoFacade getEjbFacadeTrabGrad() {
        return ejbFacadeTrabGrad;
    }

    public void setEjbFacadeTrabGrad(TrabajodegradoFacade ejbFacadeTrabGrad) {
        this.ejbFacadeTrabGrad = ejbFacadeTrabGrad;
    }

    public UsuarioFacade getEjbFacadeUsuario() {
        return ejbFacadeUsuario;
    }

    public void setEjbFacadeUsuario(UsuarioFacade ejbFacadeUsuario) {
        this.ejbFacadeUsuario = ejbFacadeUsuario;
    }

    public int getNumActa() {
        return numActa;
    }

    public void setNumActa(int numActa) {
        this.numActa = numActa;
    }

    public String getNombretg() {
        nombretg = TrabajodeGradoActual.nombreTg;
        return nombretg;
    }

    public void setNombretg(String nombretg) {
        this.nombretg = nombretg;
    }

    public BigDecimal getTrabajoid() {
        return trabajoid;
    }

    public void setTrabajoid(BigDecimal trabajoid) {
        this.trabajoid = trabajoid;
    }

    public Usuario getEst1() {
        if (TrabajodeGradoActual.est1 != null) {
            est1 = TrabajodeGradoActual.est1;
            return est1;
        } else {
            return null;
        }
    }

    public void setEst1(Usuario est1) {
        this.est1 = est1;
    }

    public Usuario getEst2() {
        if (TrabajodeGradoActual.est2 != null) {
            est2 = TrabajodeGradoActual.est2;
            return est2;
        } else {
            return null;
        }
    }

    public void setEst2(Usuario est2) {
        this.est2 = est2;
    } 

    public String getNombreDirector() {
        nombreDirector = TrabajodeGradoActual.director.getPersonanombres()+" "+TrabajodeGradoActual.director.getPersonaapellidos();
        return nombreDirector;
    }

    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
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

    public String getAprobado() {
        if(resultado == 1)
            return "Aprobado";
        else
            return "No aprobado";
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }

    public Productodetrabajo getFormatoactual() {
        return formatoactual;
    }

    public void setFormatoactual(Productodetrabajo formatoactual) {
        this.formatoactual = formatoactual;
    }
    
    public String obtenerDatos() {
        Map<String, String> map = new HashMap<String, String>();
        
        map.put("numActa", Integer.toString(getNumActa()));
        
        map.put("nombre", TrabajodeGradoActual.nombreTg);
        //
        int numeroEstudiantes = 0;
        if (est1 != null && est1.getPersonacedula() != null) {
            map.put("idestud1", est1.getPersonacedula().intValue() + "");
            map.put("nombreestud", est1.getPersonanombres() + " " + est1.getPersonaapellidos());
            numeroEstudiantes += 1;
        }
        if (est2 != null && est2.getPersonacedula() != null) {
            map.put("idestud2", est2.getPersonacedula().intValue() + "");
            map.put("nombreestud2", est2.getPersonanombres() + " " + est2.getPersonaapellidos());
            numeroEstudiantes += 1;
        }
        map.put("numeroEstudiantes", Integer.toString(numeroEstudiantes));
        map.put("iddirector", Integer.toString(TrabajodeGradoActual.director.getPersonacedula().intValue()));
        map.put("nombredirector", getNombreDirector());
        map.put("resultado", Integer.toString(getResultado()));
        map.put("observaciones", getObservaciones());
        
        Gson gson = new Gson();
        String contenido = gson.toJson(map, Map.class);

        return contenido;
    }

    public String guardar() {
        try {

            String contenido = obtenerDatos();
            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);
            
            UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);
            usuroltg.setRolid(new Rol(BigDecimal.valueOf(2)));                            //agregando al jefe depto
            usuroltg.setTrabajoid(trab);
            usuroltg.setPersonacedula(new Usuario(new BigDecimal(idjefe)));
            ejbFacadeUsuroltrab.create(usuroltg);
            
            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.valueOf(getResultado()), contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.ONE));
            prod.setTrabajoid(trab);
            ejbFacadeProdTrab.create(prod);
            
            if(getResultado() == 1) //Si fue aprobado
            {
                List<TrabajogradoFase> lstTrabfase = ejbFacadeTrabajoGradFase.ObtenerTrabajoFrasePor_trabajoID_faseID(TrabajodeGradoActual.id,1);
                TrabajogradoFase trabfase = lstTrabfase.get(0);
                trabfase.setEstado(BigInteger.ONE);
                ejbFacadeTrabajoGradFase.edit(trabfase);
            }
            
            
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
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductodetrabajoCreated"));
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"¡Revisión diligenciada con éxito!", "Se le ha enviado un correo notificando dicha operación."));
            return "fases-trabajo-de-grado";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String editar(){ 
        try
        {
            String contenido = obtenerDatos();
            
            formatoactual.setProductocontenido(contenido);
            formatoactual.setProductoaprobado(BigInteger.valueOf(getResultado()));
            
            ejbFacadeProdTrab.edit(formatoactual);
            
            if(getResultado() == 1) //Si fue aprobado
            {
                List<TrabajogradoFase> lstTrabfase = ejbFacadeTrabajoGradFase.ObtenerTrabajoFrasePor_trabajoID_faseID(TrabajodeGradoActual.id, 1);
                TrabajogradoFase trabfase = lstTrabfase.get(0);
                trabfase.setEstado(BigInteger.ONE);
                ejbFacadeTrabajoGradFase.edit(trabfase);                
            }
            
            /*
            Servicio_Email se = new Servicio_Email();
            se.setSubject("La revision de la idea del Trabajo de Grado: '"+nombretg+"' ha sido editado.");

            if(est1!=null)
              {  
                se.setTo(est1.getPersonacorreo());
                se.enviarEditadoRevisionIdea(nombretg);
              }
            if(est2!=null)
             {
                se.setTo(est2.getPersonacorreo());
                se.enviarEditadoRevisionIdea(nombretg);
             }
            if(TrabajodeGradoActual.director!=null)
             {
                se.setTo(TrabajodeGradoActual.director.getPersonacorreo());
                se.enviarEditadoRevisionIdea(nombretg);
             }
            */
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TrabajodegradoUpdated"));
            return "fases-trabajo-de-grado";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
}