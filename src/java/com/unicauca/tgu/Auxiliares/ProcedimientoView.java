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
    
    private List<DetalleImagen> images;
    /**
     * Creates a new instance of ProcedimientoView
     */
    public ProcedimientoView() {
    }
    
     
    @PostConstruct
    public void init() {
        images = new ArrayList<DetalleImagen>();
        
        images.add(new DetalleImagen("AprobacionDeAnteproyecto.png","","Aprobación del Anteproyecto"));
        images.add(new DetalleImagen("EjecucionDeTrabajoDeGrado.png","","Ejecución del Trabajo de Grado"));
        images.add(new DetalleImagen("EvaluacionDelTrabajoDeGrado.png","","Evaluación del Trabajo de Grado"));
    }
 
    public List<DetalleImagen> getImages() {
        return images;
    }
}
