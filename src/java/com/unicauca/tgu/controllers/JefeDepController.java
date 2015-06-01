package com.unicauca.tgu.controllers;

import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.FormatosTablas.TablaPerfil;
import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
    private UsuarioRolTrabajogradoFacade ejbFacade;
    @EJB
    private UsuarioFacade ejbFacadeusuario;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTrabFase;
    @EJB
    private ProductodetrabajoFacade productoTrabEJB;
    
    private boolean modo;           
    private String titulotablaJefe;
    private String titulotabla;
    private List<TablaPerfil> trabs;

     public JefeDepController() {
    }  
    
    @PostConstruct
    public void init() {
        modo = false;
        titulotablaJefe = "Lista de ideas por revisar";
    }

    public String getTitulotabla() {
        return titulotabla;
    }

    public void setTitulotabla(String titulotabla) {
        this.titulotabla = titulotabla;
    }

    public boolean isModo() {
        return modo;
    }

    public void setModo(boolean modo) {
        this.modo = modo;
    }
    
    public void ideasPorRevisar()
    {
      modo = false;
      titulotablaJefe = "Lista de ideas por revisar";
    }
    
    public void ideasRevisadas()
    {
      modo = true;
      titulotablaJefe = "Lista de ideas revisadas";
    }

    public List<TablaPerfil> getTrabsJefe() {

        trabs = new ArrayList();
        int cont;     
        TablaPerfil f;
        List<Productodetrabajo> lstProductos = new ArrayList();
        List<Productodetrabajo> lstAux1 = productoTrabEJB.ObtenerProdsTrabajoPor_formatoID(0);
        
        if(modo == false) //Ideas por revisar
        {
            for(Productodetrabajo p : lstAux1)
            {                
                List<Productodetrabajo> lstAux2 = productoTrabEJB.ObtenerProdsTrabajoPor_trabajoID_formatoID(p.getTrabajoid().getTrabajoid().intValue(),1);
                if(lstAux2.isEmpty())   //Sino tiene el formato Revision Idea asociado.
                    lstProductos.add(p);
            }
        }
        else    //Ideas ya revisadas
        {
            for(Productodetrabajo p : lstAux1)
            {                
                List<Productodetrabajo> lstAux2 = productoTrabEJB.ObtenerProdsTrabajoPor_trabajoID_formatoID(p.getTrabajoid().getTrabajoid().intValue(),1);
                if(!lstAux2.isEmpty())   //Si tiene el formato Revision Idea asociado.
                    lstProductos.add(p);
            }
        }
        
        for(Productodetrabajo t : lstProductos)
        {
            cont = 0;
            f = new TablaPerfil();                  //sacamos la informacion general tanto jefe depto, director y los estud.
            
            List<UsuarioRolTrabajogrado> lst = ejbFacade.findbytrabajoId(t.getTrabajoid().getTrabajoid().intValue());
            
            if(lst.size() > 0)
            {
                f.setFecha(lst.get(0).getFechaasignacion());
                f.setTrabajoGradoId(lst.get(0).getTrabajoid().getTrabajoid().intValue());
                f.setTrabajoGrado(lst.get(0).getTrabajoid().getTrabajonombre());

                for(UsuarioRolTrabajogrado l : lst)
                {
                        if(l.getRolid().getRolid().intValue() == 0)  //director
                      {
                          f.setDirector(l.getPersonacedula().getPersonanombres()+" "+l.getPersonacedula().getPersonaapellidos());
                          f.setDirectorId(l.getPersonacedula().getPersonacedula().intValue());   
                      }   
                    else if(l.getRolid().getRolid().intValue() == 1 && cont ==0)           //Estudiante 1
                          { 
                           f.setEst1(l.getPersonacedula().getPersonanombres()+" "+l.getPersonacedula().getPersonaapellidos());
                           f.setEst1Id(l.getPersonacedula().getPersonacedula().intValue());
                           cont ++;
                          }
                      else if(l.getRolid().getRolid().intValue() == 1 && cont ==1)      //estudiante 2
                       { 
                           f.setEst2(l.getPersonacedula().getPersonanombres()+" "+l.getPersonacedula().getPersonaapellidos());
                           f.setEst2Id(l.getPersonacedula().getPersonacedula().intValue());                      
                       }
                }
                
                List<TrabajogradoFase> lstTrabFase = ejbFacadeTrabFase.ObtenerTrabajoFrasePor_trabajoID(t.getTrabajoid().getTrabajoid().intValue());
                for(TrabajogradoFase tf : lstTrabFase)
                {
                    if(tf.getFaseid().getFaseid().equals(BigDecimal.valueOf(1)))
                    {
                        if(tf.getEstado().equals(BigInteger.valueOf(1)))
                            f.setAprobado(true);
                        else
                            f.setAprobado(false);
                        break;
                    }
                }

                int x = 999;
                for (TrabajogradoFase tg : lstTrabFase) {
                    if (tg.getEstado().intValue() == 0 && tg.getFaseid().getFaseorden().intValue() < x) {
                        f.setEstado(tg.getFaseid());
                        x = tg.getFaseid().getFaseorden().intValue();
                    }
                }
                
                trabs.add(f);
            }
        }
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
