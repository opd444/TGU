/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import com.unicauca.tgu.controllers.DirectorController;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.UsuarioRol;
import com.unicauca.tgu.jpacontroller.RolFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolFacade;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class MenuController implements Serializable {

    @EJB
    UsuarioFacade ejbFacadeUsu;
    @EJB
    RolFacade ejbFacadeRol;
    @EJB
    UsuarioRolFacade ejbFacadeUsuRol;

    private MenuModel model;
    private List<DefaultMenuItem> items;

    private boolean initMenu = false; // para el bucle de redireccionamiento
    private boolean outPerfiles = false;

    public MenuController() {
        items = new ArrayList();
    }

    @PostConstruct
    protected void initialize() {
        model = new DefaultMenuModel();

        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s = (ServiciosSimcaController) context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);

        com.unicauca.tgu.entities.Usuario usu = ejbFacadeUsu.buscarPorUsuarionombre(s.getNombreUsuario());
        List<Rol> roles = new ArrayList();

        List<UsuarioRol> lstUsuRol = ejbFacadeUsuRol.findAll();

        BigInteger personacedula = usu.getPersonacedula().toBigInteger();

        for (UsuarioRol usurolItem : lstUsuRol) {

            if (usurolItem.getPersonacedula().equals(personacedula)) {

                int rolid = usurolItem.getRolid().intValue();

                Rol rol = buscarPorRolId(BigDecimal.valueOf(rolid));
//                System.out.println(rol.getRolnombre());
                roles.add(rol);
            }
        }

        DefaultSubMenu perfiles = new DefaultSubMenu("Perfiles");
        perfiles.setStyle("background-color: lightcyan;");

        for (Rol rolItem : roles) {
            String rolnombre = rolItem.getRolnombre();
            DefaultMenuItem item = new DefaultMenuItem(rolnombre);
            item.setCommand("#{menuController.cmd" + createNameOfCmd(rolnombre) + "}");
            item.setUpdate("formMenu");
//            item.setOutcome(setOutcomePefil(rolnombre));
            items.add(item);
            perfiles.addElement(item);
            initMenu = true; // para el bucle de redireccionamiento
            setStyleItem();
            initMenu = false; // para el bucle de redireccionamiento
        }

        model.addElement(perfiles);
    }

    private Rol buscarPorRolId(BigDecimal rolid) {
        List<Rol> roles = ejbFacadeRol.findAll();

        for (Rol rolItem : roles) {
            if (rolItem.getRolid().equals(rolid)) {
                return rolItem;
            }
        }
        return null;
    }

    public MenuModel getModel() {
        return model;
    }

    private void redirectVista(String facelet) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            if (outPerfiles) {
                context.redirect("../perfiles/" + facelet + ".xhtml");
            } else {
                context.redirect(facelet + ".xhtml");
            }

        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cambioPerfil(DefaultMenuItem item) {
        itemsSetDefaultStyle();
        item.setStyle("background-color: #c2dfef");
        VistaActual.rol = (String) item.getValue();
        if (!initMenu) {// para el bucle de redireccionamiento
            redirectVista(setOutcomePefil((String) item.getValue()));
        }
    }

    /**
     * cmdX: los siguientes métodos son usados para especificar las operaciones
     * de cada perfil
     */
    public void cmdDirector() {
        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Director")) {
                cambioPerfil(item);
            }
        }
    }

    public void cmdEstudiante() {
        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Estudiante")) {
                cambioPerfil(item);
            }
        }
    }

    public void cmdJefeDepartamento() {
        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Jefe de Departamento")) {
                cambioPerfil(item);
            }
        }
    }

    public void cmdCoordinadorPrograma() {
        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Coordinador de Programa")) {
                cambioPerfil(item);
            }
        }
    }

    public void cmdEvaluador() {
        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Evaluador")) {
                cambioPerfil(item);
            }
        }
    }

    public void cmdSecretariaGeneral() {
        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Secretaria General")) {
                cambioPerfil(item);
            }
        }
    }

    public void cmdJurado() {
        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Jurado")) {
                cambioPerfil(item);
            }
        }
    }

    /**
     * itemsSetDefaultStyle: fija color por defecto para cada item en perfiles
     */
    private void itemsSetDefaultStyle() {
        for (DefaultMenuItem item : items) {
            item.setStyle("background-color: white;");
        }
    }

    /**
     * createNameOfCmd: define el nombre de la función a usar para cada perfil
     */
    private String createNameOfCmd(String rol) {

        switch (rol) {
            case "Director":
                return "Director";
            case "Estudiante":
                return "Estudiante";
            case "Jefe de Departamento":
                return "JefeDepartamento";
            case "Coordinador de Programa":
                return "CoordinadorPrograma";
            case "Evaluador":
                return "Evaluador";
            case "Secretaria General":
                return "SecretariaGeneral";
            case "Jurado":
                return "Jurado";
        }
        return null;
    }

    /**
     * setOutcomePefil: define hacia donde redireccionar cuando se cambia el
     * perfil actual.
     */
    public String setOutcomePefil(String rol) {
        switch (rol) {
            case "Director":
                return "vista-director";
            case "Estudiante":
                return "vista-estudiante";
            case "Jefe de Departamento":
                return "vista-jefe-de-departamento";
            case "Coordinador de Programa":
                return "vista-coordinador-de-programa";
            case "Evaluador":
                return "vista-evaluador";
            case "Secretaria General":
                return "vista-secretaria-general";
            case "Jurado":
                return "vista-jurado";
        }
        return null;
    }

    /**
     * setStyleItem: cambia de color el item cuando este es selecionado
     */
    public void setStyleItem() {
        String rol = VistaActual.rol;
        if (!rol.equals("null")) {
            switch (rol) {
                case "Director":
                    cmdDirector();
                    break;
                case "Estudiante":
                    cmdEstudiante();
                    break;
                case "Jefe de Departamento":
                    cmdJefeDepartamento();
                    break;
                case "Coordinador de Programa":
                    cmdCoordinadorPrograma();
                    break;
                case "Evaluador":
                    cmdEvaluador();
                    break;
                case "Secretaria General":
                    cmdSecretariaGeneral();
                    break;
                case "Jurado":
                    cmdJurado();
                    break;
            }
        }
    }

    public boolean isOutPerfiles() {
        return outPerfiles;
    }

    public void setOutPerfiles(boolean outPerfiles) {
        this.outPerfiles = outPerfiles;
    }
    /**
     * outcomeCarpetaPefil: redirecciona hacia la carperta de cada perfil
     * @return 
     */
    public String outcomeCarpetaPefil() {
        String rol = VistaActual.rol;
        switch (rol) {
            case "Director":
                return "director";
            case "Estudiante":
                return "estudiante";
            case "Jefe de Departamento":
                return "jefe-de-departamento";
            case "Coordinador de Programa":
                return "coordinador";
            case "Evaluador":
                return "evaluador";
            case "Secretaria General":
                return "secretaria";
            case "Jurado":
                return "jurado";
        }
        return null;
    }
    public String getRolActual() {
        return VistaActual.rol;
    }
}
