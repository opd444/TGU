/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.jsf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.unicauca.tgu.jsf.util.JsfUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
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

    private String nombretg; private BigDecimal trabajoid;
    private String numstud;
    private Usuario est1;
    private String nomEst1;
    private Usuario est2;
    private String nomEst2;
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
        
        trabajoid = BigDecimal.valueOf(-1);
        iddirector = UsuarioComun.id;
        nombreDirector = UsuarioComun.nombreComplet;
        fecha = new Date();
    }

    @PostConstruct
    public void init() {
        if(!trabajoid.equals(BigDecimal.valueOf(-1)))
            verProductodetrabajo();
    }

    public BigDecimal getTrabajoid() {
        if(!trabajoid.equals(BigDecimal.valueOf(-1)))
            verProductodetrabajo();
        return trabajoid;
    }

    public void setTrabajoid(BigDecimal trabajoid) {
        this.trabajoid = trabajoid;
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

    public String getNomEst1() {
        return nomEst1;
    }

    public void setNomEst1(String nomEst1) {
        this.nomEst1 = nomEst1;
    }

    public String getNomEst2() {
        return nomEst2;
    }

    public void setNomEst2(String numEst2) {
        this.nomEst2 = numEst2;
    }
    
    public String editarFormatoA() {
        try {
            //getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TrabajodegradoUpdated"));
            return "fasesTrabajoGrado/index";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }        
    }

    public String guardar() {
        Map<String, String> map = new HashMap<String, String>();

        map.put("nombre", TrabajodeGradoActual.nombreTg);
        //
        int numeroEstudiantes = 0;
        if (est1 != null && est1.getPersonacedula() != null) {
            map.put("idestud1", est1.getPersonacedula().intValue() + "");
            map.put("nombreestud", est1.getPersonanombres() + " " + est1.getPersonaapellidos());
            numeroEstudiantes += 1;
        }
        if (est2 != null && est2.getPersonacedula() != null) {
            map.put("idestud2", est2.getPersonacedula().intValue() + "");
            map.put("nombreestud2", est2.getPersonanombres() + " " + est2.getPersonaapellidos());
            numeroEstudiantes += 1;
        }
        map.put("numeroEstudiantes", Integer.toString(numeroEstudiantes));
        //
        map.put("iddirector", getIddirector() + "");
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

        try {
            UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);      //agregando director
            usuroltg.setRolid(new Rol(BigDecimal.ZERO));
            usuroltg.setTrabajoid(trab);
            usuroltg.setPersonacedula(new Usuario(new BigDecimal(getIddirector())));

            ejbFacadeUsuroltrab.create(usuroltg);
            if (est1 != null) {
                usuroltg.setRolid(new Rol(BigDecimal.ONE));              //agregando primer estudiante
                usuroltg.setPersonacedula(est1);

                ejbFacadeUsuroltrab.create(usuroltg);
            }

            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.ZERO));
            prod.setTrabajoid(trab);

            ejbFacadeProdTrab.create(prod);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TrabajodegradoUpdated"));
            return "fasesTrabajoGrado/index";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public void verProductodetrabajo() {
        
        List<Productodetrabajo> lst = ejbFacadeProdTrab.findAll();
        Productodetrabajo producto = null;
        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).getTrabajoid().getTrabajoid().equals(trabajoid)) {
                producto = lst.get(i);
            }
        }

        String jsonContenido = producto.getProductocontenido();

        Gson gson = new Gson();
//        Map<String, String> decoded = new HashMap<String, String>();
        Map<String, String> decoded = gson.fromJson(jsonContenido, new TypeToken<Map<String, String>>() {
        }.getType());

        if (decoded.get("nombre") != null) {
            nombretg = decoded.get("nombre");
        }
//        //decoded.get("idestud1");
        if (decoded.get("nombreestud") != null) {
            nomEst1 = decoded.get("nombreestud");
        }
//        //decoded.get("idestud2");
        if (decoded.get("nombreestud2") != null) {
            nomEst2 = decoded.get("nombreestud2");
        }
//        //decoded.get("iddirector");
        if (decoded.get("nombredirector") != null) {
            nombreDirector = decoded.get("nombredirector");
        }
        if (decoded.get("objetivos") != null) {
            objetivos = decoded.get("objetivos");
        }
        if (decoded.get("aportes") != null) {
            aportes = decoded.get("aportes");
        }
        if (decoded.get("tiempo") != null) {
            tiempo = decoded.get("tiempo");
        }
        if (decoded.get("recursos") != null) {
            recursos = decoded.get("recursos");
        }
        if (decoded.get("financiacion") != null) {
            financiacion = decoded.get("financiacion");
        }
        if (decoded.get("observaciones") != null) {
            observaciones = decoded.get("observaciones");
        }
        if (decoded.get("fecha") != null) {
            fecha = new Date();
            //fecha.setTime(Date.parse(decoded.get("fecha")));
        }
    }

    public List<Usuario> complete() {
        est1 = new Usuario();
        est2 = new Usuario();
        return ejbFacadeUsuario.buscarEstudiantesDisponibles();
    }

}
