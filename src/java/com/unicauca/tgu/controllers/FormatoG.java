/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.unicauca.tgu.entities.UsuarioRol;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.io.IOException;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class FormatoG {

    private String departamento; /* TODO: falta almacenar en la base de datos el departamento */

    private String titulo;
    private Usuario estudiante1;
    private String est1nomsapes;
    private String est1codigo;
    private Usuario estudiante2;
    private String est2nomsapes;
    private String est2codigo;
    private Usuario director;
    private String directornomsapes;
    private Usuario jurado1;
    private String jurado1nomsapes;
    private Usuario jurado2;
    private String jurado2nomsapes;
    private String observaciones;
    private Date fecha;
    
    private int idJefeDepto;
    /**
     * Creates a new instance of FormatoG
     */
    // Adicionales
    private Productodetrabajo prodtrab;

    private boolean existeEst1;
    private boolean existeEst2;
    private boolean est1Avalado;
    private boolean est2Avalado;
    //
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

    public FormatoG() {
        fecha = new Date();
    }

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s =  (ServiciosSimcaController)context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);
        idJefeDepto = s.getUsulog().getPersonacedula().intValue();
        existeEst1 = false;
        existeEst2 = false;
        est1Avalado = false;
        est2Avalado = false;
        buscarFormatoA();
        prepararEdicion();
    }

    public void buscarFormatoA() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.findAll();

        Productodetrabajo prodTrab = null;

        for (int i = 0; i < lstProdTrab.size(); i++) {
            // Buscando la Revisión del Formatos A para el trabajo de grado actual
            if (lstProdTrab.get(i).getFormatoid().getFormatoid().equals(BigDecimal.valueOf(1))
                    && lstProdTrab.get(i).getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                prodTrab = lstProdTrab.get(i);
            }
        }
        if (prodTrab != null) {
            Gson gson = new Gson();
            Map<String, String> decoded = gson.fromJson(prodTrab.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());
            /// titulo del trabajo de grado
            if (decoded.get("nombre") != null) {
                titulo = decoded.get("nombre");
            }
            /// Buscando el director y los estudiantes para el trabajo de grado actual
            if (decoded.get("idestud1") != null) {
                int personacedulaEst1 = Integer.valueOf(decoded.get("idestud1"));
                estudiante1 = ejbFacadeUsu.buscarporUsuid(personacedulaEst1).get(0);
                existeEst1 = true;
            }
            if (decoded.get("idestud2") != null) {
                int personacedulaEst2 = Integer.valueOf(decoded.get("idestud2"));
                estudiante2 = ejbFacadeUsu.buscarporUsuid(personacedulaEst2).get(0);
                existeEst2 = true;
            }
            if (decoded.get("iddirector") != null) {
                int personacedulaDir = Integer.valueOf(decoded.get("iddirector"));
                director = ejbFacadeUsu.buscarporUsuid(personacedulaDir).get(0);
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
        }
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
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

    public String getEst1codigo() {
        return est1codigo;
    }

    public void setEst1codigo(String est1codigo) {
        this.est1codigo = est1codigo;
    }

    public String getEst2nomsapes() {
        return est2nomsapes;
    }

    public void setEst2nomsapes(String est2nomsapes) {
        this.est2nomsapes = est2nomsapes;
    }

    public String getEst2codigo() {
        return est2codigo;
    }

    public void setEst2codigo(String est2codigo) {
        this.est2codigo = est2codigo;
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

    public Usuario getJurado1() {
        return jurado1;
    }

    public void setJurado1(Usuario jurado1) {
        this.jurado1 = jurado1;
    }

    public Usuario getJurado2() {
        return jurado2;
    }

    public void setJurado2(Usuario jurado2) {
        this.jurado2 = jurado2;
    }

    public String crearContenidoFormatoG() {
        Map<String, String> map = new HashMap();

        map.put("departamento", departamento);
        map.put("titulo", titulo);
        map.put("estudiante1Id", estudiante1.getPersonacedula().toString());
        map.put("estudiante1", est1nomsapes);
        map.put("estudiante1avalado", String.valueOf(est1Avalado));
        if (estudiante2 != null) {
            map.put("estudiante2Id", estudiante2.getPersonacedula().toString());
            map.put("estudiante2", est2nomsapes);
            map.put("estudiante2avalado", String.valueOf(est2Avalado));
        }
        map.put("directorId", director.getPersonacedula().toString());
        map.put("director", directornomsapes);
        map.put("jurado1Id", jurado1.getPersonacedula().toString());
        map.put("jurado1", jurado1nomsapes);
        map.put("jurado2Id", jurado2.getPersonacedula().toString());
        map.put("jurado2", jurado2nomsapes);
        map.put("observaciones", observaciones);
        map.put("fecha", fecha.toString());

        Gson gson = new Gson();
        String contenido = gson.toJson(map, Map.class);

        return contenido;
    }

    public String btnGuardar() {
        try {
            String contenido = crearContenidoFormatoG();
            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);
            
            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.valueOf(7)));                  // agregando el formato g
            prod.setTrabajoid(trab);
            ejbFacadeProdTrab.create(prod);

//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("Formato G del Trabajo de Grado: '" + titulo + "' ha sido diligenciado.");
//
//            Usuario secretariaGeneral = buscarSecretariaGeneral();
//
//            if (secretariaGeneral != null) {
//                se.setTo(secretariaGeneral.getPersonacorreo());
//                se.enviarDiligenciadoFormatoG(titulo);
//            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato G diligenciado con éxito."));
            return "fase-5";

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocurrio un problema al efectuar dicha operación.", ""));
            return "diligenciar-formato-G";
        }
    }

    public Usuario buscarSecretariaGeneral() {
        List<UsuarioRol> items = ejbFacadeUsuRol.findAll();
        for (UsuarioRol item : items) {
            // Rolid = 6, Rolnombre = Secretaria General
            if (item.getRolid().equals(BigInteger.valueOf(6))) {
                BigInteger personacedula = item.getPersonacedula();
                return ejbFacadeUsu.buscarporUsuid(personacedula.intValue()).get(0);
            }
        }
        return null;
    }

    public void btnAvalarEst1(TrabajodegradoController mgb) {
        if (isExisteEst1()) {
            est1Avalado = true;

//            prepararEdicion();
//            String contenido = crearContenidoFormatoG();
//            prodtrab.setProductocontenido(contenido);
//            ejbFacadeProdTrab.edit(prodtrab);
//
            if (!existeEst2) {
                actualizarAlEstadoEvaluacion(mgb);
//                enviarCorreo();
            }
        }
    }

    public void btnAvalarEst2(TrabajodegradoController mgb) {
        if (isExisteEst2()) {
            est2Avalado = true;

//            prepararEdicion();
//            String contenido = crearContenidoFormatoG();
//            prodtrab.setProductocontenido(contenido);
//            ejbFacadeProdTrab.edit(prodtrab);

            actualizarAlEstadoEvaluacion(mgb);
//            enviarCorreo();
        }
    }

    /**
     * Enviar correo a docente
     * Enviar correo a Estudiate 1
     * Enviar correo a Estudiate 2
     */
    public void enviarCorreo() {
        Servicio_Email se = new Servicio_Email();
        se.setSubject("El trabajo final: '" + titulo + "' cumple con los requisitos para sustentar.");

        if (director != null) {
            se.setTo(director.getPersonacorreo());
            se.enviarActualizarAlEstadoEvaluacion(titulo);
        }
        if (estudiante1 != null) {
            se.setTo(estudiante1.getPersonacorreo());
            se.enviarActualizarAlEstadoEvaluacion(titulo);
        }
        if (estudiante2 != null) {
            se.setTo(estudiante2.getPersonacorreo());
            se.enviarActualizarAlEstadoEvaluacion(titulo);
        }
    }

    public String btnEditar() {
        try {
            String contenido = crearContenidoFormatoG();

            prodtrab.setProductocontenido(contenido);

            ejbFacadeProdTrab.edit(prodtrab);

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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato G editado con éxito."));
            return "fase-5";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocurrio un problema al efectuar dicha operación.", ""));
            return "editar-formato-G";
        }
    }

    public void prepararEdicion() {
        prepararbtnVerFormatoG();
    }

    public void prepararbtnVerFormatoG() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 7);

        if(lstProdTrab.size() > 0) {
            prodtrab = lstProdTrab.get(0);
            Gson gson = new Gson();
            Map<String, String> decoded = gson.fromJson(prodtrab.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());
            // Buscando el Formato G para el trabajo de grado actual
            if (decoded.get("departamento") != null) {
                departamento = decoded.get("departamento");
            }
            if (decoded.get("titulo") != null) {
                titulo = decoded.get("titulo");
            }
            if (decoded.get("estudiante1") != null) {
                est1nomsapes = decoded.get("estudiante1");
            }
            if (decoded.get("estudiante1avalado") != null) {
                est1Avalado = Boolean.valueOf(decoded.get("estudiante1avalado"));
            }
            if (decoded.get("estudiante2") != null) {
                est2nomsapes = decoded.get("estudiante2");
            }
            if (decoded.get("estudiante2avalado") != null) {
                est2Avalado = Boolean.valueOf(decoded.get("estudiante2avalado"));
            }
            if (decoded.get("director") != null) {
                directornomsapes = decoded.get("director");
            }
            if (decoded.get("jurado1Id") != null) {
                int personacedulaDir = Integer.valueOf(decoded.get("jurado1Id"));
                jurado1 = ejbFacadeUsu.buscarporUsuid(personacedulaDir).get(0);
            }
            if (decoded.get("jurado1") != null) {
                jurado1nomsapes = decoded.get("jurado1");
            }
            if (decoded.get("jurado2Id") != null) {
                int personacedulaDir = Integer.valueOf(decoded.get("jurado2Id"));
                jurado2 = ejbFacadeUsu.buscarporUsuid(personacedulaDir).get(0);
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
        }
    }

    public List<Usuario> complete(String query) {
        query = query.trim();
        query = query.toUpperCase();
        
        if (query.isEmpty()) {
            jurado1 = null;
        }

        List<Usuario> ls = ejbFacadeUsu.buscarJurados(idJefeDepto, query);
        List<Usuario> usus = new ArrayList();

        for (Usuario u : ls) {
            if(u.getPersonacedula().intValue() == director.getPersonacedula().intValue())
                continue;
            List<UsuarioRol> lstTrab = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 7);
            if (lstTrab.isEmpty())
                continue;
            usus.add(u);
        }

        if (jurado2 != null) {
            for (Usuario u : ls) {
                if (u.getPersonacedula().intValue() == jurado2.getPersonacedula().intValue()) {
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
            jurado2 = null;
        }
        
        List<Usuario> ls = ejbFacadeUsu.buscarJurados(idJefeDepto, query);
        List<Usuario> usus = new ArrayList();

        for (Usuario u : ls) {
            if(u.getPersonacedula().intValue() == director.getPersonacedula().intValue())
                continue;
            List<UsuarioRol> lstTrab = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 7);
            if (lstTrab.isEmpty())
                continue;
            usus.add(u);
        }

        if (jurado1 != null) {
            for (Usuario u : ls) {
                if (u.getPersonacedula().intValue() == jurado1.getPersonacedula().intValue()) {
                    usus.remove(u);
                }
            }
        }
        return usus;
    }

    public void handlerSelectJurado1(SelectEvent e) {
        jurado1 = (Usuario) e.getObject();
        jurado1nomsapes = jurado1.getPersonanombres() + " " + jurado1.getPersonaapellidos();
    }

    public void handlerSelectJurado2(SelectEvent e) {

        jurado2 = (Usuario) e.getObject();
        jurado2nomsapes = jurado2.getPersonanombres() + " " + jurado2.getPersonaapellidos();
    }

    public void handleUnSelectJurado1(UnselectEvent e) {
        jurado1 = null;
        jurado1nomsapes = null;
    }

    public void handleUnSelectJurado2(UnselectEvent e) {
        jurado2 = null;
        jurado2nomsapes = null;
    }

    public boolean isExisteEst1() {
        if(existeEst1)
        {
            List<Productodetrabajo> lstProductos = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 6);
            if (lstProductos.size() > 0)
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public boolean isExisteEst2() {
        if(existeEst2)
        {
            List<Productodetrabajo> lstProductos = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 6);
            if (lstProductos.size() > 0)
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public boolean isEst1Avalado() {
        prepararEdicion();
        return est1Avalado;
    }

    public boolean isEst2Avalado() {
        return est2Avalado;
    }

    public void actualizarAlEstadoEvaluacion(TrabajodegradoController mgb) {
        List<TrabajogradoFase> lst = ejbFacadeTraFase.findAll();

        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                if (lst.get(i).getFaseid().getFaseid().equals(BigDecimal.valueOf(4))) {
                    lst.get(i).setEstado(BigInteger.ONE);
                    ejbFacadeTraFase.edit(lst.get(i));

                }
                if (lst.get(i).getFaseid().getFaseid().equals(BigDecimal.valueOf(5))) {
                    lst.get(i).setEstado(BigInteger.ZERO);
                    ejbFacadeTraFase.edit(lst.get(i));

                }
            }
        }

        mgb.incializar();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Estudiante avalado con éxito."));
        requestContext.update("formularioavalar");

    }
}