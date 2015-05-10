package com.unicauca.tgu.controllers;

import com.unicauca.tgu.Auxiliares.ServiciosSimcaController;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.FormatosTablas.FormatoTablaDirector;
import com.unicauca.tgu.FormatosTablas.FormatoTablaJefe;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "directorController")
@SessionScoped
public class DirectorController implements Serializable {

    @EJB
    private com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade ejbFacade;

    @EJB
    private com.unicauca.tgu.jpacontroller.TrabajodegradoFacade ejbFacadetrabgrad;

    @EJB
    private com.unicauca.tgu.jpacontroller.UsuarioFacade ejbFacadeusuario;

    private List<FormatoTablaDirector> trabs;
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

    public List<FormatoTablaDirector> getTrabsDirector() {
        trabs = new ArrayList();

        List<Trabajodegrado> trabstemp;

        if (modo == true) {
            trabstemp = ejbFacadetrabgrad.getTrabajosTerminadosporDirectorId(iddirector);
        } else {
            trabstemp = ejbFacadetrabgrad.getTrabajosEnCursoPorDirectorId(iddirector);
            /*if((trabstemp.size()+1) <= 3)
                numMaxTrabPermitidos = false;
            else
               numMaxTrabPermitidos = true;*/
        }

        int cont;
        FormatoTablaDirector f = new FormatoTablaDirector();

        for (Trabajodegrado t : trabstemp) {
            cont = 0;
            f = new FormatoTablaDirector();                  //sacamos la informacion general tanto director y los estud.
            f.setFecha(t.getUsuarioRolTrabajogradoList().get(0).getFechaasignacion());
            f.setTrabajoGradoId(t.getUsuarioRolTrabajogradoList().get(0).getTrabajoid().getTrabajoid().intValue());
            f.setTrabajoGrado(t.getUsuarioRolTrabajogradoList().get(0).getTrabajoid().getTrabajonombre());
            for (UsuarioRolTrabajogrado l : t.getUsuarioRolTrabajogradoList()) {
                if (l.getRolid().getRolid().intValue() == 1 && cont == 0) {
                    f.setEst1(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                    f.setEst1Id(l.getPersonacedula().getPersonacedula().intValue());
                    cont++;
                } else if (l.getRolid().getRolid().intValue() == 1 && cont == 1) {
                    f.setEst2(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                    f.setEst2Id(l.getPersonacedula().getPersonacedula().intValue());
                }
            }
            if (modo == false) {
                List<TrabajogradoFase> tgfs = t.getTrabajogradoFaseList();
                int x = 999;
                for (TrabajogradoFase tg : tgfs) {
                    if (tg.getEstado().intValue() == 0 && tg.getFaseid().getFaseorden().intValue() < x) {
                        f.setEstado(tg.getFaseid().getFasenombre());
                        x = tg.getFaseid().getFaseorden().intValue();
                    }
                }
            } else {
                f.setEstado("Finalizado");
            }
            trabs.add(f);
        }
        return trabs;
    }

    public void setTrabs(List<FormatoTablaDirector> trabs) {
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
        Trabajodegrado trab = new Trabajodegrado();
        trab.setTrabajonombre(nombrenuevoTG.trim());
        trab.setTrabajoid(BigDecimal.ZERO);
        ejbFacadetrabgrad.create(trab);

        trab = ejbFacadetrabgrad.findbyNombreTg(nombrenuevoTG.trim());

        UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);      //agregando director
        usuroltg.setRolid(new Rol(BigDecimal.ZERO));
        usuroltg.setTrabajoid(trab);
        usuroltg.setPersonacedula(new Usuario(new BigDecimal(iddirector)));

        ejbFacade.create(usuroltg);
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

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect("fases-trabajo-de-grado.xhtml");
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

}