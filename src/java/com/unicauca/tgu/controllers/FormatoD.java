package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.ServiciosSimcaController;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.Formatoproducto;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
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
public class FormatoD {
    
    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private UsuarioFacade ejbFacadeUsu;
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuroltrab;    
    
    private String titulo;
    private String estudiante1;
    private String estudiante2;
    private String director;
    private String numEstudiantes;
    private String est1est2;
    private String lineaDesarrollo;
    private int elementoConsideradoA;
    private int elementoConsideradoB;
    private int elementoConsideradoC;
    private int elementoConsideradoD;
    private String observaciones;
    private Date fecha;
    private Productodetrabajo productoActual;
    private String aprobado;
    private int idCoord;

    public FormatoD() {
        fecha = new Date();
        idCoord = -1;
    }
    
    @PostConstruct
    public void init() {      
        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s =  (ServiciosSimcaController)context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);
        idCoord = s.getUsulog().getPersonacedula().intValue();
        
        List<Productodetrabajo> lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 1);
        
        if (lst.size() > 0) {               //verificar si ya hay guardardo el formato de Revision de la Idea para este trabajo de grado

            Gson gson = new Gson();
            Map<String, String> decoded = gson.fromJson(lst.get(0).getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());
            
            if (decoded.get("nombre") != null) {
                titulo = decoded.get("nombre");
            }
            if (decoded.get("nombreestud") != null) {
                estudiante1 = decoded.get("nombreestud");
                est1est2 = estudiante1;
            }
            if (decoded.get("nombreestud2") != null) {
                estudiante2 = decoded.get("nombreestud2");
                est1est2 += (" y " + estudiante2);
            }
            if (decoded.get("nombredirector") != null) {
                director = decoded.get("nombredirector");
            }
            if (decoded.get("numeroEstudiantes") != null) {
                numEstudiantes = decoded.get("numeroEstudiantes");
            }
        }
        
        lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 4);

        if (lst.size() > 0) {               //verificar si ya hay guardardo el formato Remision Anteproyecto para el trabajo de grado actual

            productoActual = lst.get(0);

            Gson gson = new Gson();
            Map<String, String> decoded = gson.fromJson(productoActual.getProductocontenido(), new TypeToken<Map<String, String>>() {}.getType());
            
            if (decoded.get("lineaDesarrollo") != null) {
                lineaDesarrollo = decoded.get("lineaDesarrollo");
            }
            if (decoded.get("elementoConsideradoA") != null) {
                elementoConsideradoA = Integer.parseInt(decoded.get("elementoConsideradoA"));
            }
            if (decoded.get("elementoConsideradoB") != null) {
                elementoConsideradoB = Integer.parseInt(decoded.get("elementoConsideradoB"));
            }
            if (decoded.get("elementoConsideradoC") != null) {
                elementoConsideradoC = Integer.parseInt(decoded.get("elementoConsideradoC"));
            }
            if (decoded.get("elementoConsideradoD") != null) {
                elementoConsideradoD = Integer.parseInt(decoded.get("elementoConsideradoD"));
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
    
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEstudiante1() {
        return estudiante1;
    }

    public void setEstudiante1(String estudiante1) {
        this.estudiante1 = estudiante1;
    }

    public String getEstudiante2() {
        return estudiante2;
    }

    public void setEstudiante2(String estudiante2) {
        this.estudiante2 = estudiante2;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getNumEstudiantes() {
        return numEstudiantes;
    }

    public void setNumEstudiantes(String numEstudiantes) {
        this.numEstudiantes = numEstudiantes;
    }

    public String getEst1est2() {
        return est1est2;
    }

    public void setEst1est2(String est1est2) {
        this.est1est2 = est1est2;
    }

    public int getElementoConsideradoA() {
        return elementoConsideradoA;
    }

    public String getLineaDesarrollo() {
        return lineaDesarrollo;
    }

    public void setLineaDesarrollo(String lineaDesarrollo) {
        this.lineaDesarrollo = lineaDesarrollo;
    }

    public void setElementoConsideradoA(int elementoConsideradoA) {
        this.elementoConsideradoA = elementoConsideradoA;
    }

    public int getElementoConsideradoB() {
        return elementoConsideradoB;
    }

    public void setElementoConsideradoB(int elementoConsideradoB) {
        this.elementoConsideradoB = elementoConsideradoB;
    }

    public int getElementoConsideradoC() {
        return elementoConsideradoC;
    }

    public void setElementoConsideradoC(int elementoConsideradoC) {
        this.elementoConsideradoC = elementoConsideradoC;
    }

    public int getElementoConsideradoD() {
        return elementoConsideradoD;
    }

    public void setElementoConsideradoD(int elementoConsideradoD) {
        this.elementoConsideradoD = elementoConsideradoD;
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

    public Productodetrabajo getProductoActual() {
        return productoActual;
    }

    public void setProductoActual(Productodetrabajo prodtrab) {
        this.productoActual = prodtrab;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }
    
    public String getAprobado() {
        if(elementoConsideradoD == 1) {
            aprobado = "Aprobado";
        }
        if(elementoConsideradoD == 0) {
            aprobado = "No Aprobado";
        }
        return aprobado;
    }
    
    public String elementocosiderado(int elemento) {
        if(elemento == 1) {
            return "Si";
        }
        if(elemento == 0) {
            return "No";
        }
        return null;
    }
    
    public String obtenerDatos() {
        Map<String, String> map = new HashMap();

        map.put("titulo", titulo);
        map.put("estudiante1", estudiante1);
        if (estudiante2 != null) {
            map.put("estudiante2", estudiante2);
            map.put("numeroEstudiantes", String.valueOf(2));
        }else
            map.put("numeroEstudiantes", String.valueOf(1));
        
        map.put("director", director);
        map.put("lineaDesarrollo", lineaDesarrollo);
        map.put("elementoConsideradoA", String.valueOf(elementoConsideradoA));
        map.put("elementoConsideradoB", String.valueOf(elementoConsideradoB));
        map.put("elementoConsideradoC", String.valueOf(elementoConsideradoC));
        map.put("elementoConsideradoD", String.valueOf(elementoConsideradoD));
        map.put("observaciones", observaciones);
        map.put("fecha", fecha.toString());

        Gson gson = new Gson();
        String contenido = gson.toJson(map, Map.class);
        return contenido;
    }
    
    
    public String btnGuardar() {
        try {

            String contenido = obtenerDatos();
            
            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);
            
            if (idCoord != -1) {
                UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);
                usuroltg.setTrabajoid(trab);
                Usuario usuCoordinador = ejbFacadeUsu.find(new BigDecimal(idCoord));
                
                if(usuCoordinador != null) {
                    usuroltg.setRolid(new Rol(BigDecimal.valueOf(3)));                            //agregando al Coordinador
                    usuroltg.setPersonacedula(usuCoordinador);
                    ejbFacadeUsuroltrab.create(usuroltg);
                }
            }

            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.valueOf(4)));                  //Se agrega el formato 4
            prod.setTrabajoid(trab);
            ejbFacadeProdTrab.create(prod);
            
            //TODO: Enviar al correo
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato D diligenciado con éxito."));
            return "fase-3";
        } catch (Exception e) {
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocurrio un problema al efectuar dicha operación.", ""));
            return "diligenciar-formato-D";
        }
    }
    
    
    public String btnEditar() {
        try {
            String contenido = obtenerDatos();
            productoActual.setProductocontenido(contenido);
            ejbFacadeProdTrab.edit(productoActual);
//          TODO: Enviar al correo
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato D editado con éxito."));
            return "fase-3";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocurrio un problema al efectuar dicha operación.", ""));
            return "editar-formato-D";
        }
    }
    
    public Date getToday() {
        return new Date();
    }
}