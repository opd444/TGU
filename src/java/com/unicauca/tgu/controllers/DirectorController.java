package com.unicauca.tgu.controllers;

import com.unicauca.tgu.Auxiliares.ServiciosSimcaController;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.FormatosTablas.TablaPerfil;
import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "directorController")
@SessionScoped
public class DirectorController implements Serializable {

    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuRolTrab;
    @EJB
    private TrabajodegradoFacade ejbFacadetrabgrad;
    @EJB
    private UsuarioFacade ejbFacadeusuario;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTraFase;

    private List<TablaPerfil> trabs;
    //int modo;             // 0 para la seccion de trabajos en curso y 1 para trabajos terminados
    private boolean modo;             // 0 para la seccion de trabajos en curso y 1 para trabajos terminados
    private String titulotablaDirector;
    private String titulotabla;
    private String nombrenuevoTG;
    private Date fecha;
    private int filas;
    private boolean numMaxTrabPermitidos;
    private int iddirector;

    public DirectorController() {
        numMaxTrabPermitidos = false;
    }

    @PostConstruct
    public void init() {
        modo = false;
        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s =  (ServiciosSimcaController)context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);
        iddirector = s.getUsulog().getPersonacedula().intValue();
        titulotablaDirector = "Trabajos de Grado en Curso";
        fecha = new Date();
    }

    public List<TablaPerfil> getTrabsDirector() {
        trabs = new ArrayList();

        List<Trabajodegrado> trabstemp;

        if (modo) {
            trabstemp = ejbFacadetrabgrad.getTrabajosTerminadosporDirectorId(iddirector);
        } else {
            trabstemp = ejbFacadetrabgrad.getTrabajosEnCursoPorDirectorId(iddirector);
        }
        TablaPerfil f = new TablaPerfil();
        trabs = f.llenarTabla(trabstemp);
        return trabs;
    }

    public void setTrabs(List<TablaPerfil> trabs) {
        this.trabs = trabs;
    }

    public String getTitulotabla() {
        return titulotabla;
    }

    public void setTitulotabla(String titulotabla) {
        this.titulotabla = titulotabla;
    }

    public void trabajosenCurso() {
        modo = false;
        titulotablaDirector = "Trabajos de Grado en Curso";
    }

    public void trabajosTerminados() {
        modo = true;
        titulotablaDirector = "Trabajos de Grado Terminados";
    }

    public String getNombrenuevoTG() {
        return nombrenuevoTG;
    }

    public void setNombrenuevoTG(String nombrenuevoTG) {
        this.nombrenuevoTG = nombrenuevoTG;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void crearTG() {
        try
        {
            if(nombrenuevoTG.length() > 0)
            {
                Trabajodegrado trab = new Trabajodegrado();
                trab.setTrabajonombre(nombrenuevoTG.trim());
                trab.setTrabajoid(BigDecimal.ZERO);
                ejbFacadetrabgrad.create(trab);

                trab = ejbFacadetrabgrad.findbyNombreTg(nombrenuevoTG.trim());

                UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);      //agregando director
                usuroltg.setRolid(new Rol(BigDecimal.ZERO));
                usuroltg.setTrabajoid(trab);
                usuroltg.setPersonacedula(new Usuario(new BigDecimal(iddirector)));

                ejbFacadeUsuRolTrab.create(usuroltg);
                nombrenuevoTG = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Completado", "Trabajo de Grado creado con éxito."));
            }
            else
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Información", "Debe ingresar el nombre del Trabajo de Grado."));
        
       }
        catch(Exception e)
        {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Ocurrio un problema al efectuar dicha operación."));
        }
    }

    public boolean isModo() {
        return modo;
    }

    public void setModo(boolean modo) {
        this.modo = modo;
    }

    public void contenidoTgDirector(ActionEvent event) //guardar informacion del trabajo de grado que se esta tratando
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

        TrabajodeGradoActual.director = ejbFacadeusuario.buscarporUsuid(iddirector).get(0);
        
        TrabajodeGradoActual.fase = (Fase) event.getComponent().getAttributes().get("fase");
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect("../director/fase-"+TrabajodeGradoActual.fase.getFaseorden().intValue()+".xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DirectorController.class.getName()).log(Level.SEVERE, null, ex);
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

    public boolean isNumMaxTrabPermitidos() {
        return numMaxTrabPermitidos;
    }

    public void setNumMaxTrabPermitidos(boolean numTrabajosPermitidos) {
        this.numMaxTrabPermitidos = numTrabajosPermitidos;
    }

    public String getTitulotablaDirector() {
        return titulotablaDirector;
    }
    public void btnAvalar() { // TODO Refactorizar con cosulta directa en el facade
        List<TrabajogradoFase> lst = ejbFacadeTraFase.findAll();
        
        for(int i = 0; i < lst.size(); i++) {
            if(lst.get(i).getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                if(lst.get(i).getFaseid().getFaseid().equals(BigDecimal.valueOf(2))) {
                    lst.get(i).setEstado(BigInteger.ONE);
                    ejbFacadeTraFase.edit(lst.get(i));
                    
                }
                if(lst.get(i).getFaseid().getFaseid().equals(BigDecimal.valueOf(3))) {
                    lst.get(i).setEstado(BigInteger.ZERO);
                    ejbFacadeTraFase.edit(lst.get(i));
                    
                }
            }
        }     
        //enviar correo al coordinador
        
        /*Servicio_Email se = new Servicio_Email();
            se.setSubject("El Anteproyecto del trabajo de grado: '"+TrabajodeGradoActual.nombreTg+"' ha sido avalado.");

            if(director!=null)
            {  
                se.setTo(director.getPersonacorreo());
                se.enviarAvaladoAnteproyecto(TrabajodeGradoActual.nombreTg);
            }
        */
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Anteproyecto Avalado con Exito", ""));
        
    }
}
