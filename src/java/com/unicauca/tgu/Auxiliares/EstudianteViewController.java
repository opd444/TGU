/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author seven
 */
@ManagedBean
@SessionScoped
public class EstudianteViewController {
    
    @EJB
    private UsuarioFacade ejbFacadeUsu;
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeURT;
    /**
     * Creates a new instance of EstudianteViewController
     */
    private Trabajodegrado entityTrabajo;
    private UsuarioRolTrabajogrado entityURT;
    
    public EstudianteViewController() {
    }
    
    public void nombresApellidos(String usuarionombre) {
        com.unicauca.tgu.entities.Usuario usu = ejbFacadeUsu.buscarPorUsuarionombre(usuarionombre);
        
        if(ejbFacadeURT.findbyUsuid(usu.getPersonacedula().intValue()) != null) {
            entityTrabajo = ejbFacadeURT.findbyUsuid(usu.getPersonacedula().intValue()).get(0).getTrabajoid();
        }        
        
//        return usu.getPersonanombres() + usu.getPersonaapellidos();
    }

    public Trabajodegrado getEntityTrabajo() {
        return entityTrabajo;
    }

    public void setEntityTrabajo(Trabajodegrado entityTrabajo) {
        this.entityTrabajo = entityTrabajo;
    }
    public void loadTrabajoDeGrado() {
        List<UsuarioRolTrabajogrado> lst = ejbFacadeURT.findbytrabajoId(entityTrabajo.getTrabajoid().intValue());
        
        int numest = 0;
        for(int i=0; i<lst.size(); i++) {
            if(lst.get(i).getRolid().getRolnombre().equals("Director")) {
                TrabajodeGradoActual.id = lst.get(i).getTrabajoid().getTrabajoid().intValue();
                TrabajodeGradoActual.nombreTg = lst.get(i).getTrabajoid().getTrabajonombre();
                TrabajodeGradoActual.director = lst.get(i).getPersonacedula();
            }
            if(lst.get(i).getRolid().getRolnombre().equals("Estudiante")) {
                numest += 1;
                if(numest == 1) {
                    TrabajodeGradoActual.est1 = lst.get(i).getPersonacedula();
                }
                if(numest == 2) {
                    TrabajodeGradoActual.est2 = lst.get(i).getPersonacedula();
                }
            }
        }        
    }
}
