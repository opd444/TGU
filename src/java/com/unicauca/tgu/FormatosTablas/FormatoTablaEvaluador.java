package com.unicauca.tgu.FormatosTablas;

import java.util.Date;

public class FormatoTablaEvaluador
{
    private Date fecha;
    private String trabajoGrado;
    private String director;
    private String est1;
    private String est2;
    private boolean aprobado;
    private int trabajoGradoId;
    private int est1Id;
    private int est2Id;
    private int directorId;
    
    public FormatoTablaEvaluador() {
        est1Id = -1;
        est2Id = -1;
        directorId = -1;
        aprobado = false;        
    }
        
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTrabajoGrado() {
        return trabajoGrado;
    }

    public void setTrabajoGrado(String trabajoGrado) {
        this.trabajoGrado = trabajoGrado;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getEst1() {
        return est1;
    }

    public void setEst1(String est1) {
        this.est1 = est1;
    }

    public String getEst2() {
        return est2;
    }

    public void setEst2(String est2) {
        this.est2 = est2;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public int getTrabajoGradoId() {
        return trabajoGradoId;
    }

    public void setTrabajoGradoId(int trabajoGradoId) {
        this.trabajoGradoId = trabajoGradoId;
    }

    public int getEst1Id() {
        return est1Id;
    }

    public void setEst1Id(int est1Id) {
        this.est1Id = est1Id;
    }

    public int getEst2Id() {
        return est2Id;
    }

    public void setEst2Id(int est2Id) {
        this.est2Id = est2Id;
    }

    public int getDirectorId() {
        return directorId;
    }

    public void setDirectorId(int directorId) {
        this.directorId = directorId;
    }
}