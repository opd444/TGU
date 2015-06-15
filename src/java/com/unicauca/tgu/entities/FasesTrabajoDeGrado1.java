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
public class FasesTrabajoDeGrado1 {

    boolean primeraFase;
    boolean segundaFase;
    boolean terceraFase;
    boolean cuartaFase;
    boolean quintaFase;
    boolean sextaFase;

    public FasesTrabajoDeGrado1(int fase) {
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

     public boolean getEnensimafase(int n) {
        switch (n) {
            case 1:
                return primeraFase;
            case 2:
                return segundaFase;
            case 3:
                return terceraFase;
            case 4:
                return cuartaFase;
            case 5:
                return quintaFase;
            case 6:
                return sextaFase;
            default:
                return primeraFase;
        }
    }

    public void primeraFase() {
        primeraFase = false;
        segundaFase = true;
        terceraFase = true;
        cuartaFase = true;
        quintaFase = true;
        sextaFase = true;
    }

    public void segundaFase() {
        primeraFase = false;
        segundaFase = false;
        terceraFase = true;
        cuartaFase = true;
        quintaFase = true;
        sextaFase = true;
    }

    public void terceraFase() {
        primeraFase = false;
        segundaFase = false;
        terceraFase = false;
        cuartaFase = true;
        quintaFase = true;
        sextaFase = true;
    }

    public void cuartaFase() {
        primeraFase = false;
        segundaFase = false;
        terceraFase = false;
        cuartaFase = false;
        quintaFase = true;
        sextaFase = true;
    }

    public void quintaFase() {
        primeraFase = false;
        segundaFase = false;
        terceraFase = false;
        cuartaFase = false;
        quintaFase = false;
        sextaFase = true;
    }

    public void sextaFase() {
        primeraFase = false;
        segundaFase = false;
        terceraFase = false;
        cuartaFase = false;
        quintaFase = false;
        sextaFase = false;
    }
}
