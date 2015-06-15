/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.Formatoproducto;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class FormatoH {

    private String titulo;
    private Usuario estudiante1;
    private String est1nomsapes;
    private Usuario estudiante2;
    private String est2nomsapes;
    private Usuario director;
    private String directornomsapes;
    private Usuario jurado1;
    private String jurado1nomsapes;
    private Usuario jurado2;
    private String jurado2nomsapes;
    private String observaciones;
    private Date fecha;

    private String estadoDirector = "Aprobado";
    private String estadoJurado1;
    private String estadoJurado2 = "Aprobado";

    private List<String> estados;
    /**
     * Creates a new instance of FormatoH
     */
    private Productodetrabajo prodtrab;

    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private UsuarioFacade ejbFacadeUsu;
    @EJB
    private UsuarioRolFacade ejbFacadeUsuRol;
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuRolTrab;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTraFase;

    public FormatoH() {
    }

    @PostConstruct
    public void init() {
        estados = new ArrayList<String>();
        estados.add("Aprobado");
        estados.add("Aprobado con correcciones");
        estados.add("Aplazado");
        fecha = new Date();

        buscarFormatoA();
        prepararEdicion();
    }

    public void buscarFormatoA() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.findAll();

        Productodetrabajo prodTrab = null;

        for (int i = 0; i < lstProdTrab.size(); i++) {
            // Buscando el Oficio de asiganción de Jurados
            if (lstProdTrab.get(i).getFormatoid().getFormatoid().equals(BigDecimal.valueOf(8))
                    && lstProdTrab.get(i).getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                prodTrab = lstProdTrab.get(i);
            }
        }
        if (prodTrab != null) {
            Gson gson = new Gson();
            Map<String, String> decoded = gson.fromJson(prodTrab.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());
            /// titulo del trabajo de grado
            if (decoded.get("titulo") != null) {
                titulo = decoded.get("titulo");
            }
            /// Buscando el director y los estudiantes para el trabajo de grado actual
            if (decoded.get("idestud1") != null) {
                int personacedulaEst1 = Integer.valueOf(decoded.get("idestud1"));
                estudiante1 = ejbFacadeUsu.buscarporUsuid(personacedulaEst1).get(0);
            }
            if (decoded.get("idestud2") != null) {
                int personacedulaEst2 = Integer.valueOf(decoded.get("idestud2"));
                estudiante2 = ejbFacadeUsu.buscarporUsuid(personacedulaEst2).get(0);
            }
            if (decoded.get("iddirector") != null) {
                int personacedulaDir = Integer.valueOf(decoded.get("iddirector"));
                director = ejbFacadeUsu.buscarporUsuid(personacedulaDir).get(0);
            }
            if (decoded.get("idJurado1") != null) {
                int personacedulaJurado1 = Integer.valueOf(decoded.get("idJurado1"));
                jurado1 = ejbFacadeUsu.buscarporUsuid(personacedulaJurado1).get(0);
            }
            if (decoded.get("idJurado2") != null) {
                int personacedulaJurado2 = Integer.valueOf(decoded.get("idJurado2"));
                jurado2 = ejbFacadeUsu.buscarporUsuid(personacedulaJurado2).get(0);
            }

            if (decoded.get("nombreestud") != null) {
                est1nomsapes = decoded.get("nombreestud");
            }
            if (decoded.get("nombreestud2") != null) {
                est2nomsapes = decoded.get("nombreestud2");
            }
            if (decoded.get("nombredirector") != null) {
                directornomsapes = decoded.get("nombredirector");
            }
            if (decoded.get("nombreJurado1") != null) {
                jurado1nomsapes = decoded.get("nombreJurado1");
            }
            if (decoded.get("nombreJurado2") != null) {
                jurado2nomsapes = decoded.get("nombreJurado2");
            }
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEst1nomsapes() {
        return est1nomsapes;
    }

    public void setEst1nomsapes(String est1nomsapes) {
        this.est1nomsapes = est1nomsapes;
    }

    public String getEst2nomsapes() {
        return est2nomsapes;
    }

    public void setEst2nomsapes(String est2nomsapes) {
        this.est2nomsapes = est2nomsapes;
    }

    public String getDirectornomsapes() {
        return directornomsapes;
    }

    public void setDirectornomsapes(String directornomsapes) {
        this.directornomsapes = directornomsapes;
    }

    public String getJurado1nomsapes() {
        return jurado1nomsapes;
    }

    public void setJurado1nomsapes(String jurado1nomsapes) {
        this.jurado1nomsapes = jurado1nomsapes;
    }

    public String getJurado2nomsapes() {
        return jurado2nomsapes;
    }

    public void setJurado2nomsapes(String jurado2nomsapes) {
        this.jurado2nomsapes = jurado2nomsapes;
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

    public String getEstadoDirector() {
        return estadoDirector;
    }

    public void setEstadoDirector(String estadoDirector) {
        this.estadoDirector = estadoDirector;
    }

    public String getEstadoJurado1() {
        return estadoJurado1;
    }

    public void setEstadoJurado1(String estadoJurado1) {
        this.estadoJurado1 = estadoJurado1;
    }

    public String getEstadoJurado2() {
        return estadoJurado2;
    }

    public void setEstadoJurado2(String estadoJurado2) {
        this.estadoJurado2 = estadoJurado2;
    }

    public List<String> getEstados() {
        return estados;
    }

    public Date getToday() {
        return new Date();
    }

    public String crearContenidoFormatoH() {
        Map<String, String> map = new HashMap();

        map.put("titulo", titulo);
        map.put("estudiante1Id", estudiante1.getPersonacedula().toString());
        map.put("estudiante1", est1nomsapes);

        if (estudiante2 != null) {
            map.put("estudiante2Id", estudiante2.getPersonacedula().toString());
            map.put("estudiante2", est2nomsapes);

        }
        map.put("directorId", director.getPersonacedula().toString());
        map.put("director", directornomsapes);
        map.put("jurado1Id", jurado1.getPersonacedula().toString());
        map.put("jurado1", jurado1nomsapes);
        map.put("jurado2Id", jurado2.getPersonacedula().toString());
        map.put("jurado2", jurado2nomsapes);
        map.put("observaciones", observaciones);
        map.put("fecha", fecha.toString());

        map.put("estadoDirector", String.valueOf(estadoDirector));
        map.put("estadoJurado1", String.valueOf(estadoJurado1));
        map.put("estadoJurado2", String.valueOf(estadoJurado2));

        Gson gson = new Gson();
        String contenido = gson.toJson(map, Map.class);

        return contenido;
    }

    public String btnGuardar(TrabajodegradoController mgb) {
        try {
            String contenido = crearContenidoFormatoH();
            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);

            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.valueOf(10))); // Agregando el Formato H
            prod.setTrabajoid(trab);
            ejbFacadeProdTrab.create(prod);
            
            actualizarAlEstadoFinalizacion(mgb);
//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("Formato G del Trabajo de Grado: '" + titulo + "' ha sido diligenciado.");
//
//            Usuario secretariaGeneral = buscarSecretariaGeneral();
//
//            if (secretariaGeneral != null) {
//                se.setTo(secretariaGeneral.getPersonacorreo());
//                se.enviarDiligenciadoFormatoG(titulo);
//            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato H diligenciado con éxito."));
            return "fase-5";

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocurrio un problema al efectuar dicha operación.", ""));
            return "diligenciar-formato-H";
        }
    }

    public String btnEditar(TrabajodegradoController mgb) {
        try {
            String contenido = crearContenidoFormatoH();

            prodtrab.setProductocontenido(contenido);

            ejbFacadeProdTrab.edit(prodtrab);
            
            actualizarAlEstadoFinalizacion(mgb);

//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("Formato G del Trabajo de Grado: '" + titulo + "' ha sido editado.");
//
//            if (usuEst1 != null) {
//                se.setTo(usuEst1.getPersonacorreo());
//                se.enviarResultadoFormatoB(titulo, getAprobado(), evaluador);
//            }
//            if (usuEst2 != null) {
//                se.setTo(usuEst2.getPersonacorreo());
//                se.enviarResultadoFormatoB(titulo, getAprobado(), evaluador);
//            }
//            if (usuDir != null) {
//                se.setTo(usuDir.getPersonacorreo());
//                se.enviarResultadoFormatoB(titulo, getAprobado(), evaluador);
//            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato H editado con éxito."));
            return "fase-5";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocurrio un problema al efectuar dicha operación.", ""));
            return "editar-formato-H";
        }
    }

    public void prepararEdicion() {
        prepararbtnVerFormatoH();
    }

    public void prepararbtnVerFormatoH() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 10);

        if (lstProdTrab.size() > 0) {
            prodtrab = lstProdTrab.get(0);
            Gson gson = new Gson();
            Map<String, String> decoded = gson.fromJson(prodtrab.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());
            // Buscando el Formato H para el trabajo de grado actual
            if (decoded.get("titulo") != null) {
                titulo = decoded.get("titulo");
            }
            if (decoded.get("estudiante1") != null) {
                est1nomsapes = decoded.get("estudiante1");
            }
            if (decoded.get("estudiante2") != null) {
                est2nomsapes = decoded.get("estudiante2");
            }
            if (decoded.get("director") != null) {
                directornomsapes = decoded.get("director");
            }
            if (decoded.get("jurado1") != null) {
                jurado1nomsapes = decoded.get("jurado1");
            }
            if (decoded.get("jurado2") != null) {
                jurado2nomsapes = decoded.get("jurado2");
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
            if (decoded.get("estadoDirector") != null) {
                estadoDirector = decoded.get("estadoDirector");
            }
            if (decoded.get("estadoJurado1") != null) {
                estadoJurado1 = decoded.get("estadoJurado1");
            }
            if (decoded.get("estadoJurado2") != null) {
                estadoJurado2 = decoded.get("estadoJurado2");
            }
        }
    }

    public void actualizarAlEstadoFinalizacion(TrabajodegradoController mgb) {

        if (estadoDirector.equals("Aprobado") && estadoJurado1.equals("Aprobado") && estadoJurado2.equals("Aprobado")) {
            List<TrabajogradoFase> lst = ejbFacadeTraFase.findAll();

            for (int i = 0; i < lst.size(); i++) {
                if (lst.get(i).getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                    if (lst.get(i).getFaseid().getFaseid().equals(BigDecimal.valueOf(5))) {
                        lst.get(i).setEstado(BigInteger.ONE);
                        ejbFacadeTraFase.edit(lst.get(i));

                    }
                    if (lst.get(i).getFaseid().getFaseid().equals(BigDecimal.valueOf(6))) {
                        lst.get(i).setEstado(BigInteger.ZERO);
                        ejbFacadeTraFase.edit(lst.get(i));

                    }
                }
            }

            mgb.incializar();
            FacesContext context = FacesContext.getCurrentInstance();
            RequestContext requestContext = RequestContext.getCurrentInstance();
            context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Trabajo de Grado Finalizado."));
            requestContext.update("formularioavalar");
        }
    }
}
