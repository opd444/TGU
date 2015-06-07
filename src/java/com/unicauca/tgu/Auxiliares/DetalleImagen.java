/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

/**
 *
 * @author seven
 */
public class DetalleImagen {
    private String name;
    private String alt;
    private String title;
    
    public DetalleImagen() {
    }
    
    public DetalleImagen(String name, String alt, String title) {
        this.name = name;
        this.alt = alt;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
}
