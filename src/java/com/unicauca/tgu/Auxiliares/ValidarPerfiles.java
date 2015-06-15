/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class ValidarPerfiles {

    private com.unicauca.tgu.entities.Usuario usulog;
    @EJB
    private com.unicauca.tgu.jpacontroller.UsuarioFacade ejbFacadeusuario;
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeURT;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTrabFase;

    /**
     * Creates a new instance of ValidarPerfiles
     */
    public ValidarPerfiles() {
    }

    public void validarPerfil(String pefil) {
        String rol = VistaActual.rol;
                
        if (!rol.equals(pefil)) {

            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            String nombreUsuario = httpSession.getAttribute("nombreUsuario").toString();

            usulog = ejbFacadeusuario.buscarPorUsuarionombre(nombreUsuario);

            String vistaX = setOutcomePefil(rol);
            
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/tgu/faces/" + vistaX + ".xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ValidarPerfiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private String setOutcomePefil(String rol) {
        switch (rol) {
            case "Director":
                return "perfiles/vista-director";
            case "Estudiante": {
                List<UsuarioRolTrabajogrado> list = ejbFacadeURT.findByUsuid_Rolid(usulog.getPersonacedula().intValue(), 1);
                if (!list.isEmpty()) {
                    List<TrabajogradoFase> lstTrabFase = ejbFacadeTrabFase.ObtenerTrabajoFrasePor_trabajoID(list.get(0).getTrabajoid().getTrabajoid().intValue());
                    int x = 999;
                    for (TrabajogradoFase tg : lstTrabFase) {
                        if (tg.getEstado().intValue() == 0 && tg.getFaseid().getFaseorden().intValue() < x) {
                            x = tg.getFaseid().getFaseorden().intValue();
                        }
                    }
                    return "estudiante/fase-" + x;
                } else {
                    return "perfiles/vista-estudiante";
                }
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
