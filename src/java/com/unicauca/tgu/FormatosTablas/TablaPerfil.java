package com.unicauca.tgu.FormatosTablas;
import com.unicauca.tgu.entities.Fase;
import java.util.Date;

public class TablaPerfil
{
    private Date fecha;
    private String trabajoGrado;
    private String director;
    private String est1;
    private String est2;
    private Fase estado;
    private int trabajoGradoId;
    private int directorId;
    private int est1Id;
    private int est2Id;
    private boolean aprobado;

    public TablaPerfil() {
        fecha = new Date();
        trabajoGrado = "";
        director = "";
        est1 = "";
        est2 = "";
        estado = new Fase();
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

    public Fase getEstado() {
        return estado;
    }

    public void setEstado(Fase estado) {
        this.estado = estado;
    }

    public int getTrabajoGradoId() {
        return trabajoGradoId;
    }

    public void setTrabajoGradoId(int trabajoGradoId) {
        this.trabajoGradoId = trabajoGradoId;
    }

    public int getDirectorId() {
        return directorId;
    }

    public void setDirectorId(int directorId) {
        this.directorId = directorId;
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

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }
}