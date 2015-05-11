/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
public class MenuView {

    private MenuModel model;

    public MenuView() {
    }

    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();
        
        DefaultSubMenu perfiles = new DefaultSubMenu("Perfiles");
        perfiles.setStyle("background-color: white");
        
        DefaultMenuItem director = new DefaultMenuItem("Director");
        
        DefaultMenuItem estudiante = new DefaultMenuItem("Estudiante");
        DefaultMenuItem jefeDepartamento = new DefaultMenuItem("Jefe de Departamento");
        
        DefaultMenuItem coordinadorPrograma = new DefaultMenuItem("Coordinador de Programa");
        coordinadorPrograma.setOutcome(null);
        
        DefaultMenuItem evaluador = new DefaultMenuItem("Evaluador");        
        evaluador.setStyle("background-color: #c2dfef");
        
        model.addElement(perfiles);
        perfiles.addElement(evaluador);
        
    }

    public MenuModel getModel() {
        return model;
    }
    
}
