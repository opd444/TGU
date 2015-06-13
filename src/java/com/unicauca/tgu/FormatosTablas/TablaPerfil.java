package com.unicauca.tgu.FormatosTablas;
import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    
    public List<TablaPerfil> llenarTabla(List<Trabajodegrado> trabstemp)
            
    {
            List<TablaPerfil> trabs;
            trabs = new ArrayList();
             int cont;     
             TablaPerfil f;
             
         for (Trabajodegrado t : trabstemp) {
            cont = 0;
            f = new TablaPerfil();                  //sacamos la informacion general tanto Jurado y los estud.
            f.setFecha(t.getUsuarioRolTrabajogradoList().get(0).getFechaasignacion());
            f.setTrabajoGradoId(t.getUsuarioRolTrabajogradoList().get(0).getTrabajoid().getTrabajoid().intValue());
            f.setTrabajoGrado(t.getUsuarioRolTrabajogradoList().get(0).getTrabajoid().getTrabajonombre());

            for (UsuarioRolTrabajogrado l : t.getUsuarioRolTrabajogradoList()) {
                if (l.getRolid().getRolid().intValue() == 0) //director
                {
                    f.setDirector(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                    f.setDirectorId(l.getPersonacedula().getPersonacedula().intValue());
                } else if (l.getRolid().getRolid().intValue() == 1 && cont == 0) //Estudiante 1
                {
                    f.setEst1(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                    f.setEst1Id(l.getPersonacedula().getPersonacedula().intValue());
                    cont++;
                } else if (l.getRolid().getRolid().intValue() == 1 && cont == 1) //estudiante 2
                {
                    f.setEst2(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                    f.setEst2Id(l.getPersonacedula().getPersonacedula().intValue());
                }
            }
            List<TrabajogradoFase> tgfs = t.getTrabajogradoFaseList();
            int x = 999;
            for (TrabajogradoFase tg : tgfs) {
                if (tg.getEstado().intValue() == 0 && tg.getFaseid().getFaseorden().intValue() < x) {
                    f.setEstado(tg.getFaseid());
                    x = tg.getFaseid().getFaseorden().intValue();
                }
            }

            trabs.add(f);
        }
        return trabs; 
    }
          
      
}