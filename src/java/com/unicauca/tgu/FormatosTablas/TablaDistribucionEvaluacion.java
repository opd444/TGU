package com.unicauca.tgu.FormatosTablas;

import java.util.Date;

public class TablaDistribucionEvaluacion {
    
    private String jurado;
    private String trabajoGrado;
    private Date fecha1;
    private Date fecha2;
    private Date fecha3;

    public TablaDistribucionEvaluacion() {
        jurado = "";
        trabajoGrado = "";
        fecha1 = null;
        fecha2 = null;
        fecha3 = null;
    }
    
    public String getJurado() {
        return jurado;
    }

    public void setJurado(String jurado) {
        this.jurado = jurado;
    }

    public String getTrabajoGrado() {
        return trabajoGrado;
    }

    public void setTrabajoGrado(String trabajoGrado) {
        this.trabajoGrado = trabajoGrado;
    }

    public Date getFecha1() {
        return fecha1;
    }

    public void setFecha1(Date fecha1) {
        this.fecha1 = fecha1;
    }

    public Date getFecha2() {
        return fecha2;
    }

    public void setFecha2(Date fecha2) {
        this.fecha2 = fecha2;
    }

    public Date getFecha3() {
        return fecha3;
    }

    public void setFecha3(Date fecha3) {
        this.fecha3 = fecha3;
    }
}