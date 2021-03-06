package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xalan.internal.xsltc.dom.DOMWSFilter;
import com.unicauca.tgu.Auxiliares.Servicio_Email;
import com.unicauca.tgu.Auxiliares.ServiciosSimcaController;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
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
import com.unicauca.tgu.entities.UsuarioRol;
import com.unicauca.tgu.jpacontroller.UsuarioRolFacade;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;


@ManagedBean
@ViewScoped
public class FormatoA {

    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuroltrab;
    @EJB
    private UsuarioRolFacade ejbFacadeUsuRol;
    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private TrabajodegradoFacade ejbFacadeTrabGrad;
    @EJB
    private UsuarioFacade ejbFacadeUsuario;

    private String nombretg;
    private BigDecimal trabajoid;
    private String numstud;
    private Usuario est1;
    private String nomEst1;
    private Usuario est2;
    private String nomEst2;
    private int iddirector;
    private String nombreDirector;
    private String objetivos;
    private String aportes;
    private int tiempo;
    private String condiciones;
    private String recursos;
    private String financiacion;
    private String observaciones;
    private Date fecha;
    private Productodetrabajo productoActual;

    public FormatoA() {
    }

    @PostConstruct
    public void init() {

        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s = (ServiciosSimcaController) context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);
        iddirector = s.getUsulog().getPersonacedula().intValue();
        nombreDirector = s.getUsulog().getPersonanombres() + " " + s.getUsulog().getPersonaapellidos();
        fecha = new Date();
        tiempo = 9;

        if (!TrabajodeGradoActual.nombreTg.isEmpty()) {
            nombretg = TrabajodeGradoActual.nombreTg;
        } else {
            nombretg = "";
        }

        List<Productodetrabajo> lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 0);

        if (lst.size() > 0) {               //verificar si ya hay guardardo un formato A para este trabajo de grado

            productoActual = lst.get(0);
            Gson gson = new Gson();
            Map<String, String> decoded = gson.fromJson(productoActual.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());

            if (decoded.get("nombre") != null) {
                nombretg = decoded.get("nombre");
            }
            if (decoded.get("idestud1") != null) {
                int x = Integer.parseInt(decoded.get("idestud1"));
                est1 = ejbFacadeUsuario.find(BigDecimal.valueOf(x));
            }
            if (decoded.get("idestud2") != null) {
                int x = Integer.parseInt(decoded.get("idestud2"));
                est2 = ejbFacadeUsuario.find(BigDecimal.valueOf(x));
            }
            if (decoded.get("numeroEstudiantes") != null) {
                numstud = decoded.get("numeroEstudiantes");
            }
            if (decoded.get("nombreestud") != null) {
                nomEst1 = decoded.get("nombreestud");
            }
            if (decoded.get("nombreestud2") != null) {
                nomEst2 = decoded.get("nombreestud2");
            }
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
                tiempo = Integer.parseInt(decoded.get("tiempo"));
            }
            if (decoded.get("condiciones") != null) {
                condiciones = decoded.get("condiciones");
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
                SimpleDateFormat formateador = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                try {
                    fecha = (Date) formateador.parse(decoded.get("fecha"));
                } catch (ParseException ex) {
                    Logger.getLogger(FormatoA.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public UsuarioFacade getEjbFacadeUsuario() {
        return ejbFacadeUsuario;
    }

    public BigDecimal getTrabajoid() {
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

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }    

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
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

    public String editar() {
        try {
            
            String contenido = obtenerDatos();
            UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);
            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);
            ejbFacadeTrabGrad.edit(trab);

            usuroltg.setTrabajoid(trab);
            usuroltg.setRolid(new Rol(BigDecimal.ONE));      //Agregamos rol 1 para estudiantes
                        
            Usuario est1ant = TrabajodeGradoActual.est1;    //estudiantes anteriores
            Usuario est2ant = TrabajodeGradoActual.est2;         
            
            if (est1 != null) {

                //agregando primer estudiante
                usuroltg.setPersonacedula(est1);

                if (est1ant == null) {
                    ejbFacadeUsuroltrab.create(usuroltg);

                } else if (est1ant.getPersonacedula().intValue() != est1.getPersonacedula().intValue()) {
                    List<UsuarioRolTrabajogrado> tmp = ejbFacadeUsuroltrab.findbyUsuid(est1ant.getPersonacedula().intValue());
                    ejbFacadeUsuroltrab.remove(tmp.get(0));
                    ejbFacadeUsuroltrab.create(usuroltg);
                }
            }
            if (est2 != null) {
                //agregando primer estudiante
                usuroltg.setPersonacedula(est2);

                if (est2ant == null) {
                    ejbFacadeUsuroltrab.create(usuroltg);

                } else if (est2ant.getPersonacedula().intValue() != est2.getPersonacedula().intValue()) {
                    List<UsuarioRolTrabajogrado> tmp = ejbFacadeUsuroltrab.findbyUsuid(est2ant.getPersonacedula().intValue());
                    ejbFacadeUsuroltrab.remove(tmp.get(0));
                    ejbFacadeUsuroltrab.create(usuroltg);
                }
            }
            
            TrabajodeGradoActual.est1 = est1;
            TrabajodeGradoActual.est2 = est2;
            
            productoActual.setProductocontenido(contenido);

            ejbFacadeProdTrab.edit(productoActual);

//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("El Formato A del Trabajo de Grado: '" + nombretg + "' ha sido editado.");
//
//            if (est1 != null) {
//                se.setTo(est1.getPersonacorreo());
//                se.enviarEditadoFormatoA(nombretg);
//            }
//            if (est2 != null) {
//                se.setTo(est2.getPersonacorreo());
//                se.enviarEditadoFormatoA(nombretg);
//            }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato A editado con éxito."));
            return "fase-1";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Ocurrio un problema al efectuar dicha operación."));
            return "editar-formato-A";
        }
    }

    public String obtenerDatos() {
        Map<String, String> map = new HashMap();

        map.put("nombre", getNombretg());
        TrabajodeGradoActual.nombreTg = getNombretg();
        
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
        
        map.put("iddirector", getIddirector() + "");
        map.put("nombredirector", getNombreDirector());
        map.put("objetivos", getObjetivos().trim());
        map.put("aportes", getAportes().trim());
        map.put("tiempo", getTiempo() + "");
        map.put("condiciones", getCondiciones().trim());
        map.put("recursos", getRecursos().trim());
        map.put("financiacion", getFinanciacion().trim());
        map.put("observaciones", getObservaciones().trim());
        map.put("fecha", getFecha().toString());

        Gson gson = new Gson();
        String contenido = gson.toJson(map, Map.class);

        return contenido;
    }

    public String guardar() {

        try {

            String contenido = obtenerDatos();

            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);
            ejbFacadeTrabGrad.edit(trab);

            UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);
            usuroltg.setTrabajoid(trab);

            if (est1 != null) {
                usuroltg.setRolid(new Rol(BigDecimal.ONE));              //agregando primer estudiante
                usuroltg.setPersonacedula(est1);
                ejbFacadeUsuroltrab.create(usuroltg);
                TrabajodeGradoActual.est1 = est1;
            }

            if (est2 != null) {
                usuroltg.setRolid(new Rol(BigDecimal.ONE));              //agregando segundo estudiante
                usuroltg.setPersonacedula(est2);
                ejbFacadeUsuroltrab.create(usuroltg);
                TrabajodeGradoActual.est2 = est2;
            }

            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.ZERO));
            prod.setTrabajoid(trab);
            
            ejbFacadeProdTrab.create(prod);

//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("El Formato A del Trabajo de Grado: '" + nombretg + "' ha sido diligenciado.");
//
//            if (est1 != null) {
//                se.setTo(est1.getPersonacorreo());
//                se.enviarDiligenciadoFormatoA(nombretg);
//            }
//            if (est2 != null) {
//                se.setTo(est2.getPersonacorreo());
//                se.enviarDiligenciadoFormatoA(nombretg);
//            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato A diligenciado con éxito."));
            return "fase-1";
        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Ocurrio un problema al efectuar dicha operación."));
            return "diligenciar-formato-A";
        }
    }

    public List<Usuario> complete(String query) {

        query = query.trim();
        query = query.toUpperCase();

        if (query.isEmpty()) {
            est1 = null;
        }

        List<Usuario> ls = ejbFacadeUsuario.buscarEstudiantesDisponibles(iddirector, query);
        List<Usuario> usus = new ArrayList();

        for (Usuario u : ls) {
            List<UsuarioRol> lstTrab1 = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 1);
            if (lstTrab1.isEmpty()) {
                continue;
            }
            List<UsuarioRolTrabajogrado> lstTrabs = ejbFacadeUsuroltrab.findByUsuid_Rolid(u.getPersonacedula().intValue(), 1);
            if (lstTrabs.size() > 0) {
                continue;
            }
            usus.add(u);
        }

        if (est2 != null) {
            for (Usuario u : ls) {
                if (u.getPersonacedula().intValue() == est2.getPersonacedula().intValue()) {
                    usus.remove(u);
                }
            }
        }
        return usus;
    }

    public List<Usuario> complete2(String query) {

        query = query.trim();
        query = query.toUpperCase();

        if (query.isEmpty()) {
            est2 = null;
        }

        List<Usuario> ls = ejbFacadeUsuario.buscarEstudiantesDisponibles(iddirector, query);
        List<Usuario> usus = new ArrayList();

        for (Usuario u : ls) {
            List<UsuarioRol> lstTrab1 = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 1);
            if (lstTrab1.isEmpty()) {
                continue;
            }
            List<UsuarioRolTrabajogrado> lstTrabs = ejbFacadeUsuroltrab.findByUsuid_Rolid(u.getPersonacedula().intValue(), 1);
            if (lstTrabs.size() > 0) {
                continue;
            }
            usus.add(u);
        }

        if (est1 != null) {
            for (Usuario u : ls) {
                if (u.getPersonacedula().intValue() == est1.getPersonacedula().intValue()) {
                    usus.remove(u);
                }
            }
        }
        return usus;
    }

    public void handlerSelectest1(SelectEvent e) {
        //est1 = (Usuario) e.getObject();
    }

    public void handlerSelectest2(SelectEvent e) {
        //est2 = (Usuario) e.getObject();
    }

    public void handleUnSelectest1(UnselectEvent e) {
        //est1 = null;
    }

    public void handleUnSelectest2(UnselectEvent e) {
        //est2 = null;
    }
    
    public Date getToday() {
        return new Date();
    }
}