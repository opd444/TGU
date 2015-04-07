/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.entities;

/**
 *
 * @author seven
 */
public class FasesTrabajoDeGrado {

    String primeraFase;
    String segundaFase;
    String terceraFase;
    String cuartaFase;
    String quintaFase;
    String sextaFase;

    public FasesTrabajoDeGrado(int fase) {
        switch (fase) {
            case 1:
                primeraFase();
                break;
            case 2:
                segundaFase();
                break;
            case 3:
                terceraFase();
                break;
            case 4:
                cuartaFase();
                break;
            case 5:
                quintaFase();
                break;
            case 6:
                sextaFase();
                break;
            default:
                primeraFase();
                break;
        }
    }

    public String getPrimeraFase() {
        return primeraFase;
    }

    public void setPrimeraFase(String primeraFase) {
        this.primeraFase = primeraFase;
    }

    public String getSegundaFase() {
        return segundaFase;
    }

    public void setSegundaFase(String segundaFase) {
        this.segundaFase = segundaFase;
    }

    public String getTerceraFase() {
        return terceraFase;
    }

    public void setTerceraFase(String terceraFase) {
        this.terceraFase = terceraFase;
    }

    public String getCuartaFase() {
        return cuartaFase;
    }

    public void setCuartaFase(String cuartaFase) {
        this.cuartaFase = cuartaFase;
    }

    public String getQuintaFase() {
        return quintaFase;
    }

    public void setQuintaFase(String quintaFase) {
        this.quintaFase = quintaFase;
    }

    public String getSextaFase() {
        return sextaFase;
    }

    public void setSextaFase(String sextaFase) {
        this.sextaFase = sextaFase;
    }

    public void primeraFase() {
        primeraFase = "false";
        segundaFase = "true";
        terceraFase = "true";
        cuartaFase = "true";
        quintaFase = "true";
        sextaFase = "true";
    }

    public void segundaFase() {
        primeraFase = "false";
        segundaFase = "false";
        terceraFase = "true";
        cuartaFase = "true";
        quintaFase = "true";
        sextaFase = "true";
    }

    public void terceraFase() {
        primeraFase = "false";
        segundaFase = "false";
        terceraFase = "false";
        cuartaFase = "true";
        quintaFase = "true";
        sextaFase = "true";
    }

    public void cuartaFase() {
        primeraFase = "false";
        segundaFase = "false";
        terceraFase = "false";
        cuartaFase = "false";
        quintaFase = "true";
        sextaFase = "true";
    }

    public void quintaFase() {
        primeraFase = "false";
        segundaFase = "false";
        terceraFase = "false";
        cuartaFase = "false";
        quintaFase = "false";
        sextaFase = "true";
    }

    public void sextaFase() {
        primeraFase = "false";
        segundaFase = "false";
        terceraFase = "false";
        cuartaFase = "false";
        quintaFase = "false";
        sextaFase = "false";
    }
}
