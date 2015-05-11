/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
    private String numEstudiantes;
    private String est1est2;
    private int elementoConsideradoA;
    private int elementoConsideradoB;
    private int elementoConsideradoC;
    private int elementoConsideradoD;
    private int elementoConsideradoE;
    private int elementoConsideradoF;
    private int elementoConsideradoG;
    private int elementoConsideradoH;
    private String observaciones;
    private Date fecha;
    private String evaluador;

    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;

    public FormatoB() {
    }

    @PostConstruct
    public void init() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.findAll();
        
        Productodetrabajo prodTrab = null;
        
        for(int i = 0; i < lstProdTrab.size(); i++) {
            if(lstProdTrab.get(i).getFormatoid().getFormatoid().equals(BigDecimal.valueOf(1)) &&
                    lstProdTrab.get(i).getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                prodTrab = lstProdTrab.get(i);
            }
        }
        if(prodTrab != null) {
            Gson gson = new Gson();
            Map<String, String> decoded = gson.fromJson(prodTrab.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());
            
            if (decoded.get("nombre") != null) {
                titulo = decoded.get("nombre");
            }
            if (decoded.get("numeroEstudiantes") != null) {
                numEstudiantes = decoded.get("numeroEstudiantes");
            }
            if (decoded.get("numeroEstudiantes") != null) {
                numEstudiantes = decoded.get("numeroEstudiantes");
            }
            if (decoded.get("nombreestud") != null) {
                estudiante1 = decoded.get("nombreestud");
                est1est2 = estudiante1;
            }
            if (decoded.get("nombreestud2") != null) {
                estudiante2 = decoded.get("nombreestud2");
                est1est2 += (" y " + estudiante2);
            }
            if (decoded.get("nombredirector") != null) {
                director = decoded.get("nombredirector");                
            }            
        }
        
    }

    public int getElementoConsideradoA() {
        return elementoConsideradoA;
    }

    public void setElementoConsideradoA(int elementoConsideradoA) {
        this.elementoConsideradoA = elementoConsideradoA;
    }

    public int getElementoConsideradoB() {
        return elementoConsideradoB;
    }

    public void setElementoConsideradoB(int elementoConsideradoB) {
        this.elementoConsideradoB = elementoConsideradoB;
    }

    public int getElementoConsideradoC() {
        return elementoConsideradoC;
    }

    public void setElementoConsideradoC(int elementoConsideradoC) {
        this.elementoConsideradoC = elementoConsideradoC;
    }

    public int getElementoConsideradoD() {
        return elementoConsideradoD;
    }

    public void setElementoConsideradoD(int elementoConsideradoD) {
        this.elementoConsideradoD = elementoConsideradoD;
    }

    public int getElementoConsideradoE() {
        return elementoConsideradoE;
    }

    public void setElementoConsideradoE(int elementoConsideradoE) {
        this.elementoConsideradoE = elementoConsideradoE;
    }

    public int getElementoConsideradoF() {
        return elementoConsideradoF;
    }

    public void setElementoConsideradoF(int elementoConsideradoF) {
        this.elementoConsideradoF = elementoConsideradoF;
    }

    public int getElementoConsideradoG() {
        return elementoConsideradoG;
    }

    public void setElementoConsideradoG(int elementoConsideradoG) {
        this.elementoConsideradoG = elementoConsideradoG;
    }

    public int getElementoConsideradoH() {
        return elementoConsideradoH;
    }

    public void setElementoConsideradoH(int elementoConsideradoH) {
        this.elementoConsideradoH = elementoConsideradoH;
    }

    

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getNumEstudiantes() {
        return numEstudiantes;
    }

    public void setNumEstudiantes(String numEstudiantes) {
        this.numEstudiantes = numEstudiantes;
    }

    public String getEst1est2() {
        return est1est2;
    }

    public void setEst1est2(String est1est2) {
        this.est1est2 = est1est2;
    }
    
    
}
