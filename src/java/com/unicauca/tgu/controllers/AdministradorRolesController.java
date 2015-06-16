/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.jpacontroller.FaseFacade;
import com.unicauca.tgu.jpacontroller.RolFacade;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class AdministradorRolesController {

    /**
     * Creates a new instance of AdministrarFasesController
     */
    private List<Rol> lstRoles;
    @EJB
    private RolFacade ejbFacadeRol;
    private Rol rol;
    private int rolId;

    public AdministradorRolesController() {
    }

    @PostConstruct
    public void init() {
        lstRoles = ejbFacadeRol.findAll();
        rol = new Rol();
    }

    public List<Rol> getRoles() {
        return lstRoles;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        for (Rol item : lstRoles) {
            if (item.getRolid().equals(BigDecimal.valueOf(rolId))) {
                rol = item;
            }
        }
        this.rolId = rolId;
    }

    public String crearRol() throws IOException {
        ejbFacadeRol.create(rol);
        //ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        //context.redirect("administrar-roles.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Rol creado con éxito.",""));
        return "administrar-roles.xhtml";
    }
    public String editarRol() throws IOException {
        ejbFacadeRol.edit(rol);
        //ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        //context.redirect("administrar-roles.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Rol editado con éxito.",""));
        return "administrar-roles.xhtml";
    }
    public String eliminarRol() throws IOException {
        ejbFacadeRol.remove(rol);
        //ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        //context.redirect("administrar-roles.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Rol eliminado con éxito.",""));
        return "administrar-roles.xhtml";
    }
}
