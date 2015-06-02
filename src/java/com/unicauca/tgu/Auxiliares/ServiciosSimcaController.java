/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import com.unicauca.tgu.controllers.util.JsfUtil;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.UsuarioRol;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.RolFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import com.unicauca.tgu.serviciosSimca.ServiciosSimca_Service;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
    private com.unicauca.tgu.entities.Usuario usulog;
    @EJB
    private com.unicauca.tgu.jpacontroller.UsuarioFacade ejbFacadeusuario;
    @EJB
    private RolFacade ejbFacadeRol;
    @EJB
    private UsuarioRolFacade ejbFacadeUsuRol;
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeURT;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTrabFase;

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

                usulog = ejbFacadeusuario.buscarPorUsuarionombre(nombreUsuario);
                List<Rol> roles = new ArrayList();

                List<UsuarioRol> lstUsuRol = ejbFacadeUsuRol.findAll();

                BigInteger personacedula = usulog.getPersonacedula().toBigInteger();

                for (UsuarioRol usurolItem : lstUsuRol) {

                    if (usurolItem.getPersonacedula().equals(personacedula)) {

                        int rolid = usurolItem.getRolid().intValue();

                        Rol rol = buscarPorRolId(BigDecimal.valueOf(rolid));
                        roles.add(rol);
                    }
                }
                
                VistaActual.rol = roles.get(0).getRolnombre();
                String vistaX = setOutcomePefil(roles.get(0).getRolnombre());
                FacesContext.getCurrentInstance().getExternalContext().redirect("faces/"+vistaX+".xhtml");
                
            } catch (IOException ex) {
                Logger.getLogger(ServiciosSimcaController.class.getName()).log(Level.SEVERE, null, ex);
            }

//            return "newjsf";
            return null;
        } else {
            JsfUtil.addErrorMessage("Nombre de usuario o Contraseña incorrectos");
            return null;
        }

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

    /**
     * setOutcomePefil: define hacia donde redireccionar cuando se cambia el
     * perfil actual.
     */
    private String setOutcomePefil(String rol) {
        switch (rol) {
            case "Director":
                return "perfiles/vista-director";
            case "Estudiante":
            {
                List<UsuarioRolTrabajogrado> list = ejbFacadeURT.findByUsuid_Rolid(usulog.getPersonacedula().intValue(), 1);
                if(!list.isEmpty())
                {
                    List<TrabajogradoFase> lstTrabFase = ejbFacadeTrabFase.ObtenerTrabajoFrasePor_trabajoID(list.get(0).getTrabajoid().getTrabajoid().intValue());
                    int x = 999;
                    for (TrabajogradoFase tg : lstTrabFase) {
                        if (tg.getEstado().intValue() == 0 && tg.getFaseid().getFaseorden().intValue() < x)
                            x = tg.getFaseid().getFaseorden().intValue();
                    }
                    return "estudiante/fase-"+x;
                }
                else
                    return "perfiles/vista-estudiante";
            }
            case "Jefe de Departamento":
                return "perfiles/vista-jefe-de-departamento";
            case "Coordinador de Programa":
                return "perfiles/vista-coordinador-de-programa";
            case "Evaluador":
                return "perfiles/vista-evaluador";
            case "Secretaria General":
                return "perfiles/vista-secretaria-general";
            case "Jurado":
                return "perfiles/vista-jurado";
        }
        return null;
    }
}
