/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class ProcedimientoView {
    
    private List<String> images;
    /**
     * Creates a new instance of ProcedimientoView
     */
    public ProcedimientoView() {
    }
    
     
    @PostConstruct
    public void init() {
        images = new ArrayList<String>();
        
        images.add("AprobacionDeAnteproyecto.png");
        images.add("EjecucionDeTrabajoDeGrado.png");
        images.add("EvaluacionDelTrabajoDeGrado.png");
    }
 
    public List<String> getImages() {
        return images;
    }
}
