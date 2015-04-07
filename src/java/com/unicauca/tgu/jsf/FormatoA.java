/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jsf;

import com.google.gson.Gson;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.Auxiliares.UsuarioComun;
import com.unicauca.tgu.entities.Formatoproducto;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author pcblanco
 */
@ManagedBean
@SessionScoped
public class FormatoA { 
    
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuroltrab;
    
    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    
    @EJB
    private TrabajodegradoFacade ejbFacadeTrabGrad;
    
    @EJB
    private UsuarioFacade ejbFacadeUsuario;
    
    private String nombretg;
    private String numstud;
    private Usuario est1;
    private Usuario est2;
    private int iddirector;
    private String nombreDirector;
    private String objetivos;
    private String aportes;
    private String tiempo;
    private String recursos;
    private String financiacion;
    private String observaciones;
    private Date fecha;
    
    
    public FormatoA() { 
        iddirector = UsuarioComun.id;
        nombreDirector = UsuarioComun.nombreComplet;
        fecha = new Date();
    }
    
    public String getNombretg() {
        return nombretg;
    }

    public void setNombretg(String nombretg) {
        this.nombretg = nombretg;
    }

    public String getNumstud() {
        return numstud;
    }

    public void setNumstud(String numstud) {
        this.numstud = numstud;
    }

    public Usuario getEst1() {
        return est1;
    }

    public void setEst1(Usuario est1) {
        this.est1 = est1;
    }

    public Usuario getEst2() {
        return est2;
    }

    public void setEst2(Usuario est2) {
        this.est2 = est2;
    }
    
    public int getIddirector() {
        return iddirector;
    }

    public void setIddirector(int iddirector) {
        this.iddirector = iddirector;
    }

    public String getNombreDirector() {
        return nombreDirector;
    }

    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getAportes() {
        return aportes;
    }

    public void setAportes(String aportes) {
        this.aportes = aportes;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getRecursos() {
        return recursos;
    }

    public void setRecursos(String recursos) {
        this.recursos = recursos;
    }

    public String getFinanciacion() {
        return financiacion;
    }

    public void setFinanciacion(String financiacion) {
        this.financiacion = financiacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public void guardar()
       {
          Map<String, String> map = new HashMap<String, String>();
          
          map.put("nombre", TrabajodeGradoActual.nombreTg);
          map.put("idestud1", est1.getPersonacedula().intValue()+"");
          map.put("nombreestud", est1.getPersonanombres()+" "+est1.getPersonaapellidos());
          if(est2!=null && est2.getPersonacedula()!=null)
          {    
          map.put("idestud2", est2.getPersonacedula().intValue()+"");
          map.put("nombreestud2", est2.getPersonanombres()+" "+est2.getPersonaapellidos());
          }
          map.put("iddirector", getIddirector()+"");
          map.put("nombredirector", getNombreDirector());
          map.put("objetivos", getObjetivos());
          map.put("aportes", getAportes());
          map.put("tiempo", getTiempo());
          map.put("recursos", getRecursos());
          map.put("financiacion", getFinanciacion());
          map.put("observaciones", getObservaciones());
          map.put("fecha", getFecha().toString());
          
          Gson gson = new Gson();
          String contenido = gson.toJson(map, Map.class);
          
          Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);
          
          UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);      //agregando director
          usuroltg.setRolid(new Rol(BigDecimal.ZERO));  
          usuroltg.setTrabajoid(trab);
          usuroltg.setPersonacedula(new Usuario(new BigDecimal(getIddirector())));
          
          ejbFacadeUsuroltrab.create(usuroltg);
          
           usuroltg.setRolid(new Rol(BigDecimal.ONE));              //agregando primer estudiante
           usuroltg.setPersonacedula(est1);
           
          ejbFacadeUsuroltrab.create(usuroltg);
          
          Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
          prod.setFormatoid(new Formatoproducto(BigDecimal.ZERO));
          prod.setTrabajoid(trab);
          
          ejbFacadeProdTrab.create(prod);
    }
    public void verProductodetrabajo(BigDecimal productoid) {
        List<Productodetrabajo> lst = ejbFacadeProdTrab.findAll();
        Productodetrabajo producto;
        for(int i = 0; i < lst.size(); i++) {
            if(lst.get(i).getProductoid().equals(productoid)) 
                producto = lst.get(i);
        }
        Map<String, String> map = new HashMap<String, String>();
        Gson gson = new Gson();
        String contenido;
        //map = new ObjectMapper();
    } 
    
    public List<Usuario> complete()
          {
              est1 = new Usuario();
              est2 = new Usuario();
              return ejbFacadeUsuario.buscarEstudiantesDisponibles();
          }  
    
}
