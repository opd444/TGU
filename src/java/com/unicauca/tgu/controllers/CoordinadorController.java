package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuRolTg;
    @EJB
    private UsuarioFacade ejbFacadeusuario;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTrabFase;

    private List<TablaPerfil> anteproys;
    private int modo;    // 0 SIN ASIGNAER EVAL, 1 ASIGNADOS
    private String titulo;

    public CoordinadorController() {
        titulo = "Anteproyectos sin evaluadores asignados";
        modo = 0;
    }

    public List<TablaPerfil> getAnteproys() {

        anteproys = new ArrayList();

        List<Productodetrabajo> tem = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_formatoID(2);

        if (tem.size() > 0) {

            int cont = 0;
            TablaPerfil f;

            for (Productodetrabajo t : tem) {
                
                if(trabajoDeGradoAvalado(t.getProductocontenido())) {
                
                    cont = 0;
                    f = new TablaPerfil();                  //sacamos la informacion general tanto jefe depto, director y los estud.

                    List<UsuarioRolTrabajogrado> lst = ejbFacadeUsuRolTg.findbytrabajoId(t.getTrabajoid().getTrabajoid().intValue());

                    if (lst.size() > 0) {
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
                        
                        List<TrabajogradoFase> lstTrabFase = ejbFacadeTrabFase.ObtenerTrabajoFrasePor_trabajoID(t.getTrabajoid().getTrabajoid().intValue());
                        int x = 999;
                        for (TrabajogradoFase tg : lstTrabFase) {
                            if (tg.getEstado().intValue() == 0 && tg.getFaseid().getFaseorden().intValue() < x) {
                                f.setEstado(tg.getFaseid());
                                x = tg.getFaseid().getFaseorden().intValue();
                            }
                        }

                        if (modo == 0) {
                            if (!trabajoDeGradoAsignado(t.getProductocontenido())) {
                                anteproys.add(f);
                            }
                        } else if (trabajoDeGradoAsignado(t.getProductocontenido())) {
                            anteproys.add(f);
                        }
                        
                    }
                }
            }

        }
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
    
    public void porAsignarEvaluadores()
    {
      modo = 0;
      titulo = "Anteproyectos sin evaluadores asignados";
    }
    
    public void conEvaludaresAsignados()
    {
      modo = 1;
      titulo = "Anteproyectos con evaluadores asignados";
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
