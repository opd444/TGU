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
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author seven
 */
@ManagedBean
@SessionScoped
public class FormatoB {

    private Usuario usuEvaluador;
    private Usuario usuEst1;
    private Usuario usuEst2;
    private Usuario usuDir;
    /**
     * Creates a new instance of FormatoB
     */
    private String titulo;
    private String estudiante1;
    private String estudiante2;
    private String director;
    private String est1est2;
    private int elementoConsideradoA;
    private int elementoConsideradoB;
    private int elementoConsideradoC;
    private int elementoConsideradoD;
    private int elementoConsideradoE;
    private int elementoConsideradoF;
    private int elementoConsideradoG;
    private int elementoConsideradoH;
    private String observaciones;
    private Date fecha;
    private String evaluador;
    // Adicionales
    private String usunomEvaluador;

    private Productodetrabajo prodtrab;

    private String aprobado;

    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private UsuarioFacade ejbFacadeUsu;
    @EJB
    private TrabajodegradoFacade ejbFacadeTrabGrad;
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuroltrab;

    public FormatoB() {
    }

    @PostConstruct
    public void init() {
        buscarFormatoA();
    }

    public void buscarFormatoA() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.findAll();

        Productodetrabajo prodTrab = null;

        for (int i = 0; i < lstProdTrab.size(); i++) {
            // Buscando la RevisiÃ³n del Formatos A para el trabajo de grado actual
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
                usuEst1 = ejbFacadeUsu.buscarporUsuid(personacedulaEst1).get(0);
            }
            if (decoded.get("idestud2") != null) {
                int personacedulaEst2 = Integer.valueOf(decoded.get("idestud2"));
                usuEst2 = ejbFacadeUsu.buscarporUsuid(personacedulaEst2).get(0);
            }
            if (decoded.get("iddirector") != null) {
                int personacedulaDir = Integer.valueOf(decoded.get("iddirector"));
                usuDir = ejbFacadeUsu.buscarporUsuid(personacedulaDir).get(0);
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
            elementoConsideradoA = 0;
            elementoConsideradoB = 0;
            elementoConsideradoC = 0;
            elementoConsideradoD = 0;
            elementoConsideradoE = 0;
            elementoConsideradoF = 0;
            elementoConsideradoG = 0;
            elementoConsideradoH = 0;
            fecha = new Date();
        }
    }

    public int getElementoConsideradoA() {
        return elementoConsideradoA;
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

    public int getElementoConsideradoE() {
        return elementoConsideradoE;
    }

    public void setElementoConsideradoE(int elementoConsideradoE) {
        this.elementoConsideradoE = elementoConsideradoE;
    }

    public int getElementoConsideradoF() {
        return elementoConsideradoF;
    }

    public void setElementoConsideradoF(int elementoConsideradoF) {
        this.elementoConsideradoF = elementoConsideradoF;
    }

    public int getElementoConsideradoG() {
        return elementoConsideradoG;
    }

    public void setElementoConsideradoG(int elementoConsideradoG) {
        this.elementoConsideradoG = elementoConsideradoG;
    }

    public int getElementoConsideradoH() {
        return elementoConsideradoH;
    }

    public void setElementoConsideradoH(int elementoConsideradoH) {
        this.elementoConsideradoH = elementoConsideradoH;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getEst1est2() {
        return est1est2;
    }

    public void setEst1est2(String est1est2) {
        this.est1est2 = est1est2;
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

    public String getEvaluador() {
        return evaluador;
    }

    public void setEvaluador(String evaluador) {
        this.evaluador = evaluador;
    }

    public String getAprobado() {
        if (elementoConsideradoH == 1) {
            aprobado = "Aprobado";
        }
        if (elementoConsideradoH == 0) {
            aprobado = "No Aprobado";
        }
        return aprobado;
    }

    public String elementocosiderado(int elemento) {
        if (elemento == 1) {
            return "Si";
        }
        if (elemento == 0) {
            return "No";
        }
        return null;
    }

    public void buscarEvaluador(String evaluador) {
        usunomEvaluador = evaluador;
        usuEvaluador = ejbFacadeUsu.buscarPorUsuarionombre(evaluador);
        String nombres = usuEvaluador.getPersonanombres();
        String apellidos = usuEvaluador.getPersonaapellidos();
        this.evaluador = nombres + " " + apellidos;
    }

    public String crearContenidoFormatoB() {
        Map<String, String> map = new HashMap();

        map.put("titulo", titulo);
        map.put("estudiante1Id", usuEst1.getPersonacedula().toString());
        map.put("estudiante1", estudiante1);
        if (estudiante2 != null) {
            map.put("estudiante2Id", usuEst2.getPersonacedula().toString());
            map.put("estudiante2", estudiante2);
        }
        map.put("directorId", usuDir.getPersonacedula().toString());
        map.put("director", director);
        map.put("elementoConsideradoA", String.valueOf(elementoConsideradoA));
        map.put("elementoConsideradoB", String.valueOf(elementoConsideradoB));
        map.put("elementoConsideradoC", String.valueOf(elementoConsideradoC));
        map.put("elementoConsideradoD", String.valueOf(elementoConsideradoD));
        map.put("elementoConsideradoE", String.valueOf(elementoConsideradoE));
        map.put("elementoConsideradoF", String.valueOf(elementoConsideradoF));
        map.put("elementoConsideradoG", String.valueOf(elementoConsideradoG));
        map.put("elementoConsideradoH", String.valueOf(elementoConsideradoH));
        map.put("observaciones", observaciones);
        map.put("fecha", fecha.toString());
        map.put("evaluadorId", usuEvaluador.getPersonacedula().toString());
        map.put("evaluador", evaluador);

        Gson gson = new Gson();
        String contenido = gson.toJson(map, Map.class);

        return contenido;
    }

    public String btnGuardar() {
        try {
            String contenido = crearContenidoFormatoB();
            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);

            UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);
//            Usuario usuEvaluador = null;

            if (usunomEvaluador != null) {
//                usuEvaluador = ejbFacadeUsu.buscarPorUsuarionombre(usunomEvaluador);

                usuroltg.setRolid(new Rol(BigDecimal.valueOf(4)));                            //agregando al Evaluador
                usuroltg.setTrabajoid(trab);
                usuroltg.setPersonacedula(new Usuario(usuEvaluador.getPersonacedula()));

                ejbFacadeUsuroltrab.create(usuroltg);
            }

            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.valueOf(3)));
            prod.setTrabajoid(trab);
            ejbFacadeProdTrab.create(prod);

//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("Formato B del Trabajo de Grado: '" + titulo + "' ha sido diligenciado.");
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

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato B diligenciado con éxito."));
            return "fase-3";

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Ocurrio un problema al efectuar dicha operación."));
            return "diligenciar-formato-B";
        }
    }

    public String btnEditar() {
        try {
            String contenido = crearContenidoFormatoB();

            prodtrab.setProductocontenido(contenido);
//            formatoactual.setProductoaprobado(BigInteger.valueOf(getResultado()));

            ejbFacadeProdTrab.edit(prodtrab);

//            if (getResultado() == 1) //Si fue aprobado
//            {
//                List<TrabajogradoFase> lstTrabfase = ejbFacadeTrabajoGradFase.ObtenerTrabajoFrasePor_trabajoID_faseID(TrabajodeGradoActual.id, 1);
//                TrabajogradoFase trabfase = lstTrabfase.get(0);
//                trabfase.setEstado(BigInteger.ONE);
//                ejbFacadeTrabajoGradFase.edit(trabfase);
//            }


//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("Formato B del Trabajo de Grado: '" + titulo + "' ha sido editado.");
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
            
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato B editado con éxito."));
            return "fase-3";

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Ocurrio un problema al efectuar dicha operación."));
            return "editar-formato-B";
        }
    }

    public void prepararEdicion() {
        prepararbtnVerFormatoB();
    }

    public void redirectVerFormatoB() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect("../vistas-documentos/ver-formato-B.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(FormatoB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prepararbtnVerFormatoBEvaluador1() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.findAll();

        for (Productodetrabajo ProdTrab : lstProdTrab) {
            // Buscando el Formato B
            if (ProdTrab.getFormatoid().getFormatoid().equals(BigDecimal.valueOf(3)) && ProdTrab.getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {

                prodtrab = ProdTrab;

                Gson gson = new Gson();
                Map<String, String> decoded = gson.fromJson(prodtrab.getProductocontenido(), new TypeToken<Map<String, String>>() {
                }.getType());

                if (decoded.get("evaluador") != null) {
                    evaluador = decoded.get("evaluador");
                    prepararbtnVerFormatoB();
                    return;
                }
            }
        }
    }

    public void prepararbtnVerFormatoBEvaluador2() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.findAll();

        int evaluador2 = 0;

        for (Productodetrabajo ProdTrab : lstProdTrab) {
            // Buscando el Formato B
            if (ProdTrab.getFormatoid().getFormatoid().equals(BigDecimal.valueOf(3)) && ProdTrab.getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {

                prodtrab = ProdTrab;

                Gson gson = new Gson();
                Map<String, String> decoded = gson.fromJson(prodtrab.getProductocontenido(), new TypeToken<Map<String, String>>() {
                }.getType());

                if (decoded.get("evaluador") != null) {
                    evaluador = decoded.get("evaluador");
                    evaluador2 += 1;
                    if (evaluador2 == 2) {
                        prepararbtnVerFormatoB();
                        return;
                    }
                }
            }
        }
    }

    public void btnVerFormatoB(String evaluador) {
        buscarEvaluador(evaluador);
        prepararbtnVerFormatoB();
        redirectVerFormatoB();
    }

    public void btnVerFormatoBEvaluador1() {
        prepararbtnVerFormatoBEvaluador1();
        redirectVerFormatoB();

    }

    public void btnVerFormatoBEvaluador2() {
        prepararbtnVerFormatoBEvaluador2();
        redirectVerFormatoB();
    }

    public void prepararbtnVerFormatoB() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.findAll();

        for (Productodetrabajo ProdTrab : lstProdTrab) {
            // Buscando el Formato B para el evaluador actual
            if (ProdTrab.getFormatoid().getFormatoid().equals(BigDecimal.valueOf(3)) && ProdTrab.getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                prodtrab = ProdTrab;
                Gson gson = new Gson();
                Map<String, String> decoded = gson.fromJson(prodtrab.getProductocontenido(), new TypeToken<Map<String, String>>() {
                }.getType());
                // Buscando el Formato B que diligencio el evaluador
                if (decoded.get("evaluador") != null && decoded.get("evaluador").equals(evaluador)) {
                    if (decoded.get("titulo") != null) {
                        titulo = decoded.get("titulo");
                    }
                    if (decoded.get("estudiante1") != null) {
                        estudiante1 = decoded.get("estudiante1");
                    }
                    if (decoded.get("estudiante2") != null) {
                        estudiante2 = decoded.get("estudiante2");
                    }
                    if (decoded.get("director") != null) {
                        director = decoded.get("director");
                    }
                    if (decoded.get("elementoConsideradoA") != null) {
                        elementoConsideradoA = Integer.valueOf(decoded.get("elementoConsideradoA"));
                    }
                    if (decoded.get("elementoConsideradoB") != null) {
                        elementoConsideradoB = Integer.valueOf(decoded.get("elementoConsideradoB"));
                    }
                    if (decoded.get("elementoConsideradoC") != null) {
                        elementoConsideradoC = Integer.valueOf(decoded.get("elementoConsideradoC"));
                    }
                    if (decoded.get("elementoConsideradoD") != null) {
                        elementoConsideradoD = Integer.valueOf(decoded.get("elementoConsideradoD"));
                    }
                    if (decoded.get("elementoConsideradoE") != null) {
                        elementoConsideradoE = Integer.valueOf(decoded.get("elementoConsideradoE"));
                    }
                    if (decoded.get("elementoConsideradoF") != null) {
                        elementoConsideradoF = Integer.valueOf(decoded.get("elementoConsideradoF"));
                    }
                    if (decoded.get("elementoConsideradoG") != null) {
                        elementoConsideradoG = Integer.valueOf(decoded.get("elementoConsideradoG"));
                    }
                    if (decoded.get("elementoConsideradoH") != null) {
                        elementoConsideradoH = Integer.valueOf(decoded.get("elementoConsideradoH"));
                    }
                    if (decoded.get("observaciones") != null) {
                        observaciones = decoded.get("observaciones");
                    }
                    if (decoded.get("fecha") != null) {
                        fecha = new Date();
                    }
                    if (decoded.get("evaluador") != null) {
                        evaluador = decoded.get("evaluador");
                    }
                    return;
                }
            }
        }
    }
}
