/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.UsuarioRol;
import com.unicauca.tgu.jpacontroller.RolFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolFacade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
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
@SessionScoped
public class MenuController implements Serializable {

    @EJB
    UsuarioFacade ejbFacadeUsu;
    @EJB
    RolFacade ejbFacadeRol;
    @EJB
    UsuarioRolFacade ejbFacadeUsuRol;

    private MenuModel model;
    private List<DefaultMenuItem> items;

    public MenuController() {
        items = new ArrayList();
    }

    @PostConstruct
    protected void initialize() {
        model = new DefaultMenuModel();

        com.unicauca.tgu.entities.Usuario usu = ejbFacadeUsu.buscarPorUsuarionombre("wpantoja");
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
        perfiles.setStyle("background-color: white;");

        for (Rol rolItem : roles) {
            String rolnombre = rolItem.getRolnombre();
            DefaultMenuItem item = new DefaultMenuItem(rolnombre);
            item.setCommand("#{menuController.cmd" + createNameOfCmd(rolnombre) + "}");
            item.setUpdate("formMenu");
            items.add(item);
            perfiles.addElement(item);
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

    /**
     * cmdX: los siguientes métodos son usados para especificar las operaciones
     * de cada perfil
     */
    public void cmdDirector() {

        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Director")) {
                itemsSetDefaultStyle();
                item.setStyle("background-color: #c2dfef");
            }
        }
    }

    public void cmdEstudiante() {

        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Estudiante")) {
                itemsSetDefaultStyle();
                item.setStyle("background-color: #c2dfef");
            }
        }
    }

    public void cmdJefeDepartamento() {

        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Jefe de Departamento")) {
                itemsSetDefaultStyle();
                item.setStyle("background-color: #c2dfef");
            }
        }
    }

    public void cmdCoordinadorPrograma() {

        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Coordinador de Programa")) {
                itemsSetDefaultStyle();
                item.setStyle("background-color: #c2dfef");
            }
        }
    }

    public void cmdEvaluador() {

        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Evaluador")) {
                itemsSetDefaultStyle();
                item.setStyle("background-color: #c2dfef");
            }
        }
    }

    public void cmdSecretariaGeneral() {

        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Secretaria General")) {
                itemsSetDefaultStyle();
                item.setStyle("background-color: #c2dfef");
            }
        }
    }

    public void cmdJurado() {

        for (DefaultMenuItem item : items) {
            if (item.getValue().equals("Jurado")) {
                itemsSetDefaultStyle();
                item.setStyle("background-color: #c2dfef");
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
        String nameOfCmd = new String();

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
}
