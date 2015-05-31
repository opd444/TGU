/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.Servicio_Email;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.Formatoproducto;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRol;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    /**
     * Creates a new instance of FormatoG
     */
    // Adicionales
    private Productodetrabajo prodtrab;
    //
    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private UsuarioFacade ejbFacadeUsu;
    @EJB
    private UsuarioRolFacade ejbFacadeUsuRol;
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuRolTrab;

    public FormatoG() {
    }

    @PostConstruct
    public void init() {
        buscarFormatoA();
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
            }
            if (decoded.get("idestud2") != null) {
                int personacedulaEst2 = Integer.valueOf(decoded.get("idestud2"));
                estudiante2 = ejbFacadeUsu.buscarporUsuid(personacedulaEst2).get(0);
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

            fecha = new Date();
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

        Gson gson = new Gson();
        String contenido = gson.toJson(map, Map.class);

        return contenido;
    }

    public String btnGuardar() {
        try {
            String contenido = crearContenidoFormatoG();
            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);

            UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);

            if (jurado1 != null) {
                usuroltg.setRolid(new Rol(BigDecimal.valueOf(7)));                            //agregando al Jurado 1
                usuroltg.setTrabajoid(trab);
                usuroltg.setPersonacedula(new Usuario(jurado1.getPersonacedula()));

                ejbFacadeUsuRolTrab.create(usuroltg);
            }

            if (jurado2 != null) {
                usuroltg.setRolid(new Rol(BigDecimal.valueOf(7)));                            //agregando al Jurado 2
                usuroltg.setTrabajoid(trab);
                usuroltg.setPersonacedula(new Usuario(jurado2.getPersonacedula()));

                ejbFacadeUsuRolTrab.create(usuroltg);
            }

            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.valueOf(7)));                  // agregando el formato g
            prod.setTrabajoid(trab);
            ejbFacadeProdTrab.create(prod);

            Servicio_Email se = new Servicio_Email();
            se.setSubject("Formato G del Trabajo de Grado: '" + titulo + "' ha sido diligenciado.");
            
            Usuario secretariaGeneral = buscarSecretariaGeneral();

            if (secretariaGeneral != null) {
                se.setTo(secretariaGeneral.getPersonacorreo());
                se.enviarDiligenciadoFormatoG(titulo);
            }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato G diligenciado con éxito."));
            return "fase-ejecucion-del-trabajo-de-grado";

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Ocurrio un problema al efectuar dicha operación."));
            return "diligenciar-formato-G";
        }
    }
    
    public Usuario buscarSecretariaGeneral() {
        List<UsuarioRol> items = ejbFacadeUsuRol.findAll();
        for(UsuarioRol item : items) {
            // Rolid = 6, Rolnombre = Secretaria General
            if(item.getRolid().equals(BigInteger.valueOf(6))) {
                BigInteger personacedula = item.getPersonacedula();
                return ejbFacadeUsu.buscarporUsuid(personacedula.intValue()).get(0);
            }
        }
        return null;
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
            return "fase-ejecucion-del-trabajo-de-grado";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Ocurrio un problema al efectuar dicha operación."));
            return "editar-formato-G";
        }
    }

    public void prepararEdicion() {
        prepararbtnVerFormatoG();
    }   

    public void prepararbtnVerFormatoG() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.findAll();

        for (Productodetrabajo ProdTrab : lstProdTrab) {
            // Buscando el Formato G para el trabajo de grado actual
            if (ProdTrab.getFormatoid().getFormatoid().equals(BigDecimal.valueOf(7)) && ProdTrab.getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                prodtrab = ProdTrab;
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
                if (decoded.get("estudiante2") != null) {
                    est2nomsapes = decoded.get("estudiante2");
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
                    fecha = new Date();
                }
            }
        }
    }

    public List<Usuario> complete(String query) {
        query = query.trim();
        query = query.toUpperCase();

        List<Usuario> ls = ejbFacadeUsu.buscarEvaluadores(query);
        List<Usuario> usus = new ArrayList();

        for (Usuario u : ls) {
            List<UsuarioRol> lstTrab1 = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 1);
            if (!lstTrab1.isEmpty()) {
                continue;
            }
            lstTrab1 = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 5);
            if (!lstTrab1.isEmpty()) {
                continue;
            }
            lstTrab1 = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 6);
            if (!lstTrab1.isEmpty()) {
                continue;
            }
            lstTrab1 = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 8);
            if (!lstTrab1.isEmpty()) {
                continue;
            }
            List<UsuarioRolTrabajogrado> lstTrabs = ejbFacadeUsuRolTrab.findByUsuid_Rolid(u.getPersonacedula().intValue(), 4);
            if (lstTrabs.size() > 3) //Para evitar que se asigne un evaluador con 3 trabajos de grado.
            {
                continue;
            }
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

        List<Usuario> ls = ejbFacadeUsu.buscarEvaluadores(query);
        List<Usuario> usus = new ArrayList();

        for (Usuario u : ls) {
            List<UsuarioRol> lstTrab1 = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 1);
            if (!lstTrab1.isEmpty()) {
                continue;
            }
            lstTrab1 = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 5);
            if (!lstTrab1.isEmpty()) {
                continue;
            }
            lstTrab1 = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 6);
            if (!lstTrab1.isEmpty()) {
                continue;
            }
            lstTrab1 = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 8);
            if (!lstTrab1.isEmpty()) {
                continue;
            }
            List<UsuarioRolTrabajogrado> lstTrabs = ejbFacadeUsuRolTrab.findByUsuid_Rolid(u.getPersonacedula().intValue(), 4);
            if (lstTrabs.size() > 3) //Para evitar que se asigne un evaluador con 3 trabajos de grado.
            {
                continue;
            }
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
}
