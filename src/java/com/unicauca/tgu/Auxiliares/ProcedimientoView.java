package com.unicauca.tgu.Auxiliares;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ProcedimientoView {
    
    private List<DetalleImagen> images;

    public ProcedimientoView() {
    }
    
     
    @PostConstruct
    public void init() {
        images = new ArrayList();
        
        images.add(new DetalleImagen("AprobacionDeAnteproyecto.png","","1. Aprobación del Anteproyecto"));
        images.add(new DetalleImagen("EjecucionDeTrabajoDeGrado.png","","2. Ejecución del Trabajo de Grado"));
        images.add(new DetalleImagen("EvaluacionDelTrabajoDeGrado.png","","3. Evaluación del Trabajo de Grado"));
    }
 
    public List<DetalleImagen> getImages() {
        return images;
    }
}