package com.unicauca.tgu.controllers;

import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.FasesTrabajoDeGrado;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "trabajodegradoController")
@ViewScoped
public class TrabajodegradoController implements Serializable {

    private BigDecimal trabajoid;
    private Trabajodegrado trabajo;
    private FasesTrabajoDeGrado fases;

    @EJB
    private ProductodetrabajoFacade ejbFacadePro;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTrabFase;

    @EJB
    private com.unicauca.tgu.jpacontroller.TrabajodegradoFacade ejbFacade;

    @PostConstruct
    public void init() {
        trabajoid = BigDecimal.valueOf(TrabajodeGradoActual.id);      //se Recupera el Id del trabajo actual
        habilitarfases();
    }

    //Formato A
    public boolean getBtnDiligenciarFormatoA() {
        return verificarProductodeTrabajo(trabajoid.intValue(), 0);
    }

    public boolean getBtnDiligenciarRevisionFormatoA() {
        return verificarProductodeTrabajo(trabajoid.intValue(), 1);
    }

    public boolean getBtnVerFormatoA() {
        return !verificarProductodeTrabajo(trabajoid.intValue(), 0);
    }

    public boolean getBtnEditarFormatoA() {

        List<Productodetrabajo> lst = ejbFacadePro.findAll();
        List<Productodetrabajo> lstProd = obtenerProductodeTrabajoporFormato(trabajoid.intValue(), 0);
        List<Productodetrabajo> lstProd2 = obtenerProductodeTrabajoporFormato(trabajoid.intValue(), 1);

        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).getTrabajoid().getTrabajoid().equals(trabajoid)) {
                if (lstProd.size() > 0) {
                    if (lstProd2.size() > 0) {
                        return getBtnEditarRevisionFormatoA();
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    public boolean getBtnVerRevisionFormatoA() {
        return !verificarProductodeTrabajo(trabajoid.intValue(), 1);
    }

    public List<Productodetrabajo> obtenerProductodeTrabajoporFormato(int idtrabajo, int idformato) {
        return ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(idtrabajo, idformato);
    }

    public boolean verificarProductodeTrabajo(int idtrabajo, int idformato) {
        List<Productodetrabajo> Lst = obtenerProductodeTrabajoporFormato(idtrabajo, idformato);
        return Lst.size() > 0;
    }

    public boolean getBtnEditarRevisionFormatoA() {
        List<Productodetrabajo> lstProd = obtenerProductodeTrabajoporFormato(trabajoid.intValue(), 1);

        if (lstProd.size() > 0) {
            if (lstProd.get(0).getProductoaprobado().equals(BigInteger.ONE)) //Si ha sido aprobado el prod del formato 1
            {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean getBtnDiligenciarAnteproyecto() {
        List<Productodetrabajo> lstProd = obtenerProductodeTrabajoporFormato(TrabajodeGradoActual.id, 2);
        if (lstProd.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getBtnEditarAnteproyecto() {
        List<Productodetrabajo> lstProd = obtenerProductodeTrabajoporFormato(TrabajodeGradoActual.id, 2);
        if (lstProd.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getBtnVerAnteproyecto() {
        List<Productodetrabajo> lstProd = obtenerProductodeTrabajoporFormato(TrabajodeGradoActual.id, 2);
        if (lstProd.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public void incializar() {
        habilitarfases();
    }

    public Trabajodegrado getTrabajo() {
        return new Trabajodegrado(trabajoid, TrabajodeGradoActual.nombreTg);
    }

    public void setTrabajo(Trabajodegrado trabajo) {

        this.trabajo = trabajo;
    }

    public BigDecimal getTrabajoid() {
        return trabajoid;
    }

    public void setTrabajoid(BigDecimal trabajoid) {
        this.trabajoid = trabajoid;
    }

    public void trabajoAsignado(BigDecimal trabajoid) {
        this.trabajoid = trabajoid;
    }

    public FasesTrabajoDeGrado getFases() {
        return fases;
    }

    public void setFases(FasesTrabajoDeGrado fases) {
        this.fases = fases;
    }

    public TrabajodegradoController() {
    }

    private TrabajodegradoFacade getFacade() {
        return ejbFacade;
    }

    private void habilitarfases() {
        int fase = 0;
        List<TrabajogradoFase> traFase = ejbFacadeTrabFase.ObtenerTrabajoFrasePor_trabajoID(trabajoid.intValue());
        for (TrabajogradoFase traFase1 : traFase) {
            if (traFase1.getTrabajoid().getTrabajoid().equals(trabajoid)) {
                if (traFase1.getEstado().equals(BigInteger.ONE)) {
                    fase = traFase1.getFaseid().getFaseid().intValue();
                }
            }
        }
        fases = new FasesTrabajoDeGrado(fase + 1);
    }

    // Formato B

    public boolean getBtnDiligenciarFormatoB() {
        return verificarProductodeTrabajo(trabajoid.intValue(), 3);
    }

    public boolean getBtnEditarFormatoB() {
        return true;
    }

    public boolean getBtnVerFormatoB() {
        return true;
    }
    // Formato B
}
