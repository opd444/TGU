package com.unicauca.tgu.controllers;

import com.unicauca.tgu.Auxiliares.ServiciosSimcaController;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.FormatosTablas.FormatoTablaJefe;
import com.unicauca.tgu.FormatosTablas.FormatoTablaSecretaria;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
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
public class SecretariaController
{
    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade ejbFacadeUsuRolTg;
    @EJB
    private com.unicauca.tgu.jpacontroller.UsuarioFacade ejbFacadeusuario;
    
    private List<FormatoTablaSecretaria> anteproys;
    
    private Usuario secretaria;
    private String titulo;
    boolean modo;
    
    public SecretariaController() {
    }
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s =  (ServiciosSimcaController)context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);
        secretaria = s.getUsulog();
        modo = false;
        titulo = "Anteproyectos por aprobar";
    }
    
    public List<FormatoTablaSecretaria> getAnteproyectos()
    {
        anteproys = new ArrayList();
        FormatoTablaSecretaria f;
        int cont;
        List<Productodetrabajo> lstProductos = new ArrayList();
        List<Productodetrabajo> lstAux1 = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_formatoID(4);    //Se obtienen todos los productos con el formato: Acta de remisión. 
        
        if(modo == false) //Anteproyectos por aprobar
        {
            for(Productodetrabajo p : lstAux1)
            {                
                List<Productodetrabajo> lstAux2 = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(p.getTrabajoid().getTrabajoid().intValue(),5);
                if(lstAux2.isEmpty())   //Sino tiene formato 5 asociado.
                    lstProductos.add(p);
            }
        }
        else { //Anteproyectos aprobados
            for(Productodetrabajo p : lstAux1)
            {                
                List<Productodetrabajo> lstAux2 = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(p.getTrabajoid().getTrabajoid().intValue(),5);
                if(!lstAux2.isEmpty())   //Si tiene formato 5 asociado.
                    lstProductos.add(p);
            }
        }
        
        for(Productodetrabajo t : lstProductos)
        {
            cont = 0;
            f = new FormatoTablaSecretaria();

            List<UsuarioRolTrabajogrado> lst = ejbFacadeUsuRolTg.findbytrabajoId(t.getTrabajoid().getTrabajoid().intValue());
            if (lst.size() > 0)
            {
                f.setFecha(lst.get(0).getFechaasignacion());
                f.setTrabajoGradoId(lst.get(0).getTrabajoid().getTrabajoid().intValue());
                f.setTrabajoGrado(lst.get(0).getTrabajoid().getTrabajonombre());

                for (UsuarioRolTrabajogrado l : lst) {
                    if (l.getRolid().getRolid().intValue() == 0) //director
                    {
                        f.setDirector(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                        f.setDirectorId(l.getPersonacedula().getPersonacedula().intValue());
                    } else if (l.getRolid().getRolid().intValue() == 1 && cont == 0) //Estudiante 1
                    {
                        f.setEst1(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                        f.setEst1Id(l.getPersonacedula().getPersonacedula().intValue());
                        cont++;
                    } else if (l.getRolid().getRolid().intValue() == 1 && cont == 1) //estudiante 2
                    {
                        f.setEst2(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                        f.setEst2Id(l.getPersonacedula().getPersonacedula().intValue());
                    }
                }                
                anteproys.add(f);                 
            }
        }
        return anteproys;
    }
    
    public void contenidoTgSecretaria(ActionEvent event) //guardar informacion del trabajo de grado que se esta tratando
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
        
        //Agregamos el director respectivo de dicho trabajo de grado
        idusu = (Integer) event.getComponent().getAttributes().get("iddirector");
        if (idusu != -1) {
            TrabajodeGradoActual.director = ejbFacadeusuario.buscarporUsuid(idusu).get(0);
        }

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect("../secretaria/fase-evaluacion-anteproyecto.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DirectorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setAnteproyectos(List<FormatoTablaSecretaria> anteproys) {
        this.anteproys = anteproys;
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
    
    public void anteproyectosPorAprobar()
    {
        modo = false;
        titulo = "Anteproyectos por aprobar";
    }
    
    public void anteproyectosYaAprobados()
    {
        modo = true;
        titulo = "Anteproyectos ya aprobados";
    }
}