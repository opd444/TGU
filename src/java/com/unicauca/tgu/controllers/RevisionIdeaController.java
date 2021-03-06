package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.Servicio_Email;
import com.unicauca.tgu.Auxiliares.ServiciosSimcaController;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.Formatoproducto;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


@ManagedBean
@ViewScoped
public class RevisionIdeaController {
    
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuroltrab;
    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private TrabajodegradoFacade ejbFacadeTrabGrad;
    @EJB
    private UsuarioFacade ejbFacadeUsuario;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTrabajoGradFase;

    private String numActa;
    private String nombretg;
    private BigDecimal trabajoid;
    private Usuario est1;
    private Usuario est2;
    private String nombreDirector;
    private int resultado;
    private String observaciones;
    private Date fecha;
    private String aprobado;
    private int idjefe;
    
    private Productodetrabajo productoActual;
    
    public RevisionIdeaController() {
        fecha = new Date();
    }

    @PostConstruct
    public void init() {
                
        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s =  (ServiciosSimcaController)context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);
        idjefe = s.getUsulog().getPersonacedula().intValue();
                
        numActa = "1A";
        resultado = 0;
        
        List<Productodetrabajo> lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 1);

        if (lst.size() > 0) {               //verificar si ya hay guardardo el formato de revisión del formato A para este trabajo de grado

            productoActual = lst.get(0);

            Gson gson = new Gson();
            Map<String, String> decoded = gson.fromJson(productoActual.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());
            
            if (decoded.get("numActa") != null) {
                numActa = decoded.get("numActa");
            }
            if (decoded.get("nombre") != null) {
                nombretg = decoded.get("nombre");
            }
//        //decoded.get("idestud1");
            if (decoded.get("idestud1") != null) {
                int x = Integer.parseInt(decoded.get("idestud1"));
                est1 = ejbFacadeUsuario.find(BigDecimal.valueOf(x));
            }
//        //decoded.get("idestud2");
            if (decoded.get("idestud2") != null) {
                int x = Integer.parseInt(decoded.get("idestud2"));
                est2 = ejbFacadeUsuario.find(BigDecimal.valueOf(x));
            }
//        //decoded.get("iddirector");
            if (decoded.get("nombredirector") != null) {
                nombreDirector = decoded.get("nombredirector");
            }
            if (decoded.get("resultado") != null) {
                resultado = Integer.parseInt(decoded.get("resultado"));
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

    public UsuarioRolTrabajogradoFacade getEjbFacadeUsuroltrab() {
        return ejbFacadeUsuroltrab;
    }

    public void setEjbFacadeUsuroltrab(UsuarioRolTrabajogradoFacade ejbFacadeUsuroltrab) {
        this.ejbFacadeUsuroltrab = ejbFacadeUsuroltrab;
    }

    public ProductodetrabajoFacade getEjbFacadeProdTrab() {
        return ejbFacadeProdTrab;
    }

    public void setEjbFacadeProdTrab(ProductodetrabajoFacade ejbFacadeProdTrab) {
        this.ejbFacadeProdTrab = ejbFacadeProdTrab;
    }

    public TrabajodegradoFacade getEjbFacadeTrabGrad() {
        return ejbFacadeTrabGrad;
    }

    public void setEjbFacadeTrabGrad(TrabajodegradoFacade ejbFacadeTrabGrad) {
        this.ejbFacadeTrabGrad = ejbFacadeTrabGrad;
    }

    public UsuarioFacade getEjbFacadeUsuario() {
        return ejbFacadeUsuario;
    }

    public void setEjbFacadeUsuario(UsuarioFacade ejbFacadeUsuario) {
        this.ejbFacadeUsuario = ejbFacadeUsuario;
    }

    public String getNumActa() {
        return numActa;
    }

    public void setNumActa(String numActa) {
        this.numActa = numActa;
    }

    public String getNombretg() {
        nombretg = TrabajodeGradoActual.nombreTg;
        return nombretg;
    }

    public void setNombretg(String nombretg) {
        this.nombretg = nombretg;
    }

    public BigDecimal getTrabajoid() {
        return trabajoid;
    }

    public void setTrabajoid(BigDecimal trabajoid) {
        this.trabajoid = trabajoid;
    }

    public Usuario getEst1() {
        if (TrabajodeGradoActual.est1 != null) {
            est1 = TrabajodeGradoActual.est1;
            return est1;
        } else {
            return null;
        }
    }

    public void setEst1(Usuario est1) {
        this.est1 = est1;
    }

    public Usuario getEst2() {
        if (TrabajodeGradoActual.est2 != null) {
            est2 = TrabajodeGradoActual.est2;
            return est2;
        } else {
            return null;
        }
    }

    public void setEst2(Usuario est2) {
        this.est2 = est2;
    } 

    public String getNombreDirector() {
        nombreDirector = TrabajodeGradoActual.director.getPersonanombres()+" "+TrabajodeGradoActual.director.getPersonaapellidos();
        return nombreDirector;
    }

    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
    }

    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getAprobado() {
        if(resultado == 1)
            return "Aprobado";
        else
            return "No aprobado";
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }

    public Productodetrabajo getProductoActual() {
        return productoActual;
    }

    public void setProductoActual(Productodetrabajo productoActual) {
        this.productoActual = productoActual;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public String obtenerDatos() {
        Map<String, String> map = new HashMap();
        
        map.put("numActa", getNumActa());
        
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
        map.put("iddirector", Integer.toString(TrabajodeGradoActual.director.getPersonacedula().intValue()));
        map.put("nombredirector", getNombreDirector());
        map.put("resultado", Integer.toString(getResultado()));
        map.put("observaciones", getObservaciones());
        map.put("fecha", getFecha().toString());
        
        Gson gson = new Gson();
        String contenido = gson.toJson(map, Map.class);

        return contenido;
    }

    public String guardar() {
        try {

            String contenido = obtenerDatos();
            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);
            
            UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);
            usuroltg.setRolid(new Rol(BigDecimal.valueOf(2)));                            //agregando al jefe depto
            usuroltg.setTrabajoid(trab);
            usuroltg.setPersonacedula(new Usuario(new BigDecimal(idjefe)));
            ejbFacadeUsuroltrab.create(usuroltg);
            
            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.valueOf(getResultado()), contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.ONE));
            prod.setTrabajoid(trab);
            ejbFacadeProdTrab.create(prod);
            
            if(getResultado() == 1) //Si fue aprobado
            {
                List<TrabajogradoFase> lstTrabfase = ejbFacadeTrabajoGradFase.ObtenerTrabajoFrasePor_trabajoID_faseID(TrabajodeGradoActual.id,1);
                TrabajogradoFase trabfase = lstTrabfase.get(0);
                trabfase.setEstado(BigInteger.ONE);
                ejbFacadeTrabajoGradFase.edit(trabfase);
            }
            
//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("La revision de la idea del Trabajo de Grado: '"+nombretg+"' ha sido diligenciada.");
//
//            if(est1!=null)
//              {  
//                se.setTo(est1.getPersonacorreo());
//                se.enviarDiligenciadoRevisionIdea(nombretg);
//              }
//            if(est2!=null)
//             {
//                se.setTo(est2.getPersonacorreo());
//                se.enviarDiligenciadoRevisionIdea(nombretg);
//             }
//            if(TrabajodeGradoActual.director!=null)
//             {
//                se.setTo(TrabajodeGradoActual.director.getPersonacorreo());
//                se.enviarDiligenciadoRevisionIdea(nombretg);
//             }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Revisión de la idea, diligenciada con éxito."));
            return "fase-1";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocurrio un problema al efectuar dicha operación.", ""));
            return "diligenciar-formato-revision-idea";
        }
    }

    public String editar(){ 
        try
        {
            String contenido = obtenerDatos();
            
            productoActual.setProductocontenido(contenido);
            productoActual.setProductoaprobado(BigInteger.valueOf(getResultado()));
            
            ejbFacadeProdTrab.edit(productoActual);
            
            if(getResultado() == 1) //Si fue aprobado
            {
                List<TrabajogradoFase> lstTrabfase = ejbFacadeTrabajoGradFase.ObtenerTrabajoFrasePor_trabajoID_faseID(TrabajodeGradoActual.id, 1);
                TrabajogradoFase trabfase = lstTrabfase.get(0);
                trabfase.setEstado(BigInteger.ONE);
                ejbFacadeTrabajoGradFase.edit(trabfase);                
            }
            
//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("La revision de la idea del Trabajo de Grado: '"+nombretg+"' ha sido editado.");
//
//            if(est1!=null)
//              {  
//                se.setTo(est1.getPersonacorreo());
//                se.enviarEditadoRevisionIdea(nombretg);
//              }
//            if(est2!=null)
//             {
//                se.setTo(est2.getPersonacorreo());
//                se.enviarEditadoRevisionIdea(nombretg);
//             }
//            if(TrabajodeGradoActual.director!=null)
//             {
//                se.setTo(TrabajodeGradoActual.director.getPersonacorreo());
//                se.enviarEditadoRevisionIdea(nombretg);
//             }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Revisión de la idea, editada con éxito."));
            return "fase-1";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocurrio un problema al efectuar dicha operación.", ""));
            return "editar-formato-revision-idea";
        }
    }
    
    public Date getToday() {
        return new Date();
    }
}