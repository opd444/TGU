/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.entities.Formatoproducto;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.jpacontroller.FaseFacade;
import com.unicauca.tgu.jpacontroller.FormatoproductoFacade;
import com.unicauca.tgu.jpacontroller.RolFacade;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author seven
 */
@ManagedBean
@SessionScoped
public class AdministradorProductosTrabajoController implements Serializable{

    /**
     * Creates a new instance of AdministrarFasesController
     */
    private List<Formatoproducto> lstFormatoProducto;  
    @EJB
    private FormatoproductoFacade ejbFormatoProducto;
    @EJB
    private FaseFacade ebjFase;
    private Formatoproducto formatoPT;
    private int formatoPTId;   
    private Fase fase;   
    private List<Fase> lsfase;    

    public AdministradorProductosTrabajoController() 
    {
    }

    @PostConstruct
    public void init() 
    {
        lstFormatoProducto = ejbFormatoProducto.findAll();
        formatoPT = new Formatoproducto();
        lsfase = ebjFase.findAll();
    }
     public List<Formatoproducto> getLstFormatoProducto() 
    {
        return lstFormatoProducto;
    }
    
    public Fase getFase() 
    {
        return fase;
    }

    public void setFase(Fase fase) 
    {
        this.fase = fase;
    }
    public void setLstFormatoProducto(List<Formatoproducto> lstFormatoProducto) 
    {
        this.lstFormatoProducto = lstFormatoProducto;
    }

    public List<Fase> getLsfase() 
    {
        return lsfase;
    }

    public void setLsfase(List<Fase> lsfase)
    {
        this.lsfase = lsfase;
    }
    public Formatoproducto getFormatoPT() 
    {
        return formatoPT;
    }

    public void setFormatoPT(Formatoproducto formatoPT)
    {
        this.formatoPT = formatoPT;
    }
    

    

    public String crearFormatoproducto() throws IOException {
        ejbFormatoProducto.create(formatoPT);
        //System.out.println("entro:"+this.formatoPT.getFormatoid());
        //System.out.println("entro:"+this.formatoPT.getFormatonombre());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Formato de Trabajo creado con éxito.",""));
        return "administrar-formato-productos.xhtml";
    }
    public String editarFormatoproducto() throws IOException {
        ejbFormatoProducto.edit(formatoPT);
        //ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        //context.redirect("administrar-formato-productos.xhtml");
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Formato de Trabajo editado con éxito.",""));
        return "administrar-formato-productos.xhtml";
    }
    public String eliminarFormatoproducto() throws IOException {
        ejbFormatoProducto.remove(formatoPT);
        //ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        //context.redirect("administrar-formato-productos.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Formato de Trabajo eliminado con éxito.",""));
        return "administrar-formato-productos.xhtml";
    }
}
