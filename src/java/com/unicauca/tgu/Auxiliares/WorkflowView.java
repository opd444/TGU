/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.jpacontroller.FaseFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class WorkflowView {
    @EJB
    private FaseFacade ejbFacade;
    
    private MenuModel model;
    /**
     * Creates a new instance of WorkflowView
     */
    public WorkflowView() {
        
    }
    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();
        
//        List<DefaultMenuItem> lstMenuItems = new ArrayList();
//        
//        List<Fase> lstFases = ejbFacade.findAll();
//        
//        for(int i = 0; i < lstFases.size(); i++) {
//            String step = lstFases.get(i).getFasenombre();
//            
//            DefaultMenuItem menuItem = new DefaultMenuItem(step);
//            
//            lstMenuItems.add(menuItem);
//            
//            model.addElement(menuItem);
//        }
        model.addElement(new DefaultMenuItem("Presentación\nde la Idea"));
        model.addElement(new DefaultMenuItem("Realización\ndel\nAnteproyecto"));
        model.addElement(new DefaultMenuItem("Evaluación\ndel\nAnteproyecto"));
        model.addElement(new DefaultMenuItem("Ejecución\ndel Trabajo\nde Grado"));
        model.addElement(new DefaultMenuItem("Evaluación\ndel Trabajo\nde Grado"));
        model.addElement(new DefaultMenuItem("Finalizado"));
    }

    public MenuModel getModel() {
        return model;
    }
    
}
