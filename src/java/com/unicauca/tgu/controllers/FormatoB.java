/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class FormatoB {

    /**
     * Creates a new instance of FormatoB
     */
    private String titulo;
    private String estudiante1;
    private String estudiante2;
    private String director;
    private boolean elementoConsideradoA;
    private boolean elementoConsideradoB;
    private boolean elementoConsideradoC;
    private boolean elementoConsideradoD;
    private boolean elementoConsideradoE;
    private boolean elementoConsideradoF;
    private boolean elementoConsideradoG;
    private boolean elementoConsideradoH;
    private String observaciones;
    private Date fecha;
    private String evaluador;
    
    public FormatoB() {
    }

    public boolean isElementoConsideradoA() {
        return elementoConsideradoA;
    }

    public void setElementoConsideradoA(boolean elementoConsideradoA) {
        this.elementoConsideradoA = elementoConsideradoA;
    }

    public boolean isElementoConsideradoB() {
        return elementoConsideradoB;
    }

    public void setElementoConsideradoB(boolean elementoConsideradoB) {
        this.elementoConsideradoB = elementoConsideradoB;
    }

    public boolean isElementoConsideradoC() {
        return elementoConsideradoC;
    }

    public void setElementoConsideradoC(boolean elementoConsideradoC) {
        this.elementoConsideradoC = elementoConsideradoC;
    }

    public boolean isElementoConsideradoD() {
        return elementoConsideradoD;
    }

    public void setElementoConsideradoD(boolean elementoConsideradoD) {
        this.elementoConsideradoD = elementoConsideradoD;
    }

    public boolean isElementoConsideradoE() {
        return elementoConsideradoE;
    }

    public void setElementoConsideradoE(boolean elementoConsideradoE) {
        this.elementoConsideradoE = elementoConsideradoE;
    }

    public boolean isElementoConsideradoF() {
        return elementoConsideradoF;
    }

    public void setElementoConsideradoF(boolean elementoConsideradoF) {
        this.elementoConsideradoF = elementoConsideradoF;
    }

    public boolean isElementoConsideradoG() {
        return elementoConsideradoG;
    }

    public void setElementoConsideradoG(boolean elementoConsideradoG) {
        this.elementoConsideradoG = elementoConsideradoG;
    }

    public boolean isElementoConsideradoH() {
        return elementoConsideradoH;
    }

    public void setElementoConsideradoH(boolean elementoConsideradoH) {
        this.elementoConsideradoH = elementoConsideradoH;
    }
    
    
}
