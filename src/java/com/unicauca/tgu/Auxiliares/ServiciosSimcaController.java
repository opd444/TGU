/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import com.unicauca.tgu.controllers.util.JsfUtil;
import com.unicauca.tgu.serviciosSimca.ServiciosSimca_Service;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author seven
 */
@ManagedBean
@SessionScoped
public class ServiciosSimcaController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/proyectoII/ServiciosSimca.wsdl")
    private ServiciosSimca_Service service;
    private  com.unicauca.tgu.entities.Usuario usulog;
    @EJB
    private com.unicauca.tgu.jpacontroller.UsuarioFacade ejbFacadeusuario;

    private String nombreUsuario;
    private String contrasenia;

       public ServiciosSimcaController() {
    }

    public com.unicauca.tgu.entities.Usuario getUsulog() {
        return usulog;
    }
       
       
       
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String login() {

        if (autentificarUsuario(nombreUsuario, contrasenia)) {
            //JsfUtil.addSuccessMessage("Nombre de ususario o Contraseña correctos");

            ControllerUsuario cu = new ControllerUsuario();
            Usuario usu = cu.crearUsuario(consultarUsuario(nombreUsuario));

            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            httpSession.setAttribute("nombreUsuario", usu.getNombreUsuario());
            httpSession.setAttribute("rol", usu.getRol());
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getSessionMap().put("login", nombreUsuario);

            try {
                if(nombreUsuario.equals("pmage") || nombreUsuario.equals("wpantoja") || nombreUsuario.equals("cgonzals")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("evaluador/index.xhtml");
                    return null;
                }
                
                usulog = ejbFacadeusuario.buscarPorUsuarionombre(nombreUsuario);
                
                if (usu.getRol().equals("Docente")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("director/vista-director.xhtml");
                }
                if (usu.getRol().equals("Estudiante")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("estudiante/vista-estudiante.xhtml");
                }

            } catch (IOException ex) {
                Logger.getLogger(ServiciosSimcaController.class.getName()).log(Level.SEVERE, null, ex);
            }

//            return "newjsf";
            return null;
        } else {
            JsfUtil.addErrorMessage("Nombre de ususario o Contraseña incorrectos");
            return null;
        }

    }

    public void logout() {
        try {
            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            httpSession.invalidate();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ServiciosSimcaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Boolean autentificarUsuario(java.lang.String nombreUsuario, java.lang.String contrasenia) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        com.unicauca.tgu.serviciosSimca.ServiciosSimca port = service.getServiciosSimcaPort();
        return port.autentificarUsuario(nombreUsuario, contrasenia);
    }

    private String consultarUsuario(java.lang.String nombreUsuario) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        com.unicauca.tgu.serviciosSimca.ServiciosSimca port = service.getServiciosSimcaPort();
        return port.consultarUsuario(nombreUsuario);
    }
}
