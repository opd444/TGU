package com.unicauca.tgu.controllers;

import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.FormatosTablas.TablaPerfil;
import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import java.io.IOException;
import java.util.ArrayList;
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

@ManagedBean
@ViewScoped
public class JefeDepController {

    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private UsuarioFacade ejbFacadeusuario;
    @EJB
    private TrabajodegradoFacade ejbFacadetrabgrad;
    
    private boolean modo;           
    private String titulotablaJefe;
    private List<TablaPerfil> trabs;

     public JefeDepController() {
    }  
    
    @PostConstruct
    public void init() {
        modo = false;
        titulotablaJefe = "Trabajos de Grado en Curso";
    }

    public boolean isModo() {
        return modo;
    }

    public void setModo(boolean modo) {
        this.modo = modo;
    }
    
    public void trabajosenCurso() {
        modo = false;
        titulotablaJefe = "Trabajos de Grado en Curso";
    }

    public void trabajosTerminados() {
        modo = true;
        titulotablaJefe = "Trabajos de Grado Terminados";
    }

    public List<TablaPerfil> getTrabsJefe() {

        trabs = new ArrayList();

        List<Trabajodegrado> trabstemp = new ArrayList();

        if (modo == true) {
            trabstemp = ejbFacadetrabgrad.getTrabajosTerminados();
        } else {
            
            List<Trabajodegrado> lstAux = ejbFacadetrabgrad.getTrabajosEnCurso();
            
            for(int i=0; i<lstAux.size(); i++)
            {
                List<Productodetrabajo> lstProd = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(lstAux.get(i).getTrabajoid().intValue(), 0);
                if(!lstProd.isEmpty()) {
                    trabstemp.add(lstAux.get(i));
                }
            }
        }

        TablaPerfil f = new TablaPerfil();

        trabs = f.llenarTabla(trabstemp);

        return trabs;
    }

    public void setTrabsJefe(List<TablaPerfil> trabs) {
        this.trabs = trabs;
    }

    public void contenidoTgJefe(ActionEvent event)  //guardar informacion del trabajo de grado que se esta tratando
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
            context.redirect("../jefe-de-departamento/fase-" + TrabajodeGradoActual.fase.getFaseorden().intValue() + ".xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DirectorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getTitulotablaJefe() {
        return titulotablaJefe;
    }
    
}
