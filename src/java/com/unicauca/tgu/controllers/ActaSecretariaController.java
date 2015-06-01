package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.Servicio_Email;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.FormatosTablas.FormatoTablaActa;
import com.unicauca.tgu.entities.Formatoproducto;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class ActaSecretariaController {

    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTraFase1;

    private String nombreTrabajodeGrado;
    private String nombreDirector;
    private Date fechaact;
    private String resultado;
    private String numact;
    private FormatoTablaActa acta;

    /**
     * Creates a new instance of ActaSecretariaController
     */
    public ActaSecretariaController() {
        nombreDirector = TrabajodeGradoActual.director.getPersonanombres() + " " + TrabajodeGradoActual.director.getPersonaapellidos();
        nombreTrabajodeGrado = TrabajodeGradoActual.nombreTg;
        numact = "1A";
        resultado = "0";
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        fechaact = c.getTime();
    }

    public String getNombreTrabajodeGrado() {
        return nombreTrabajodeGrado;
    }

    public void setNombreTrabajodeGrado(String nombreTrabajodeGrado) {
        this.nombreTrabajodeGrado = nombreTrabajodeGrado;
    }

    public String getNombreDirector() {
        return nombreDirector;
    }

    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
    }

    public Date getFechaact() {
        return fechaact;
    }

    public void setFechaact(Date fechaact) {
        this.fechaact = fechaact;
    }

    public String getNumact() {
        return numact;
    }

    public void setNumact(String numact) {
        this.numact = numact;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public FormatoTablaActa getActa() {
        return acta;
    }

    public void setActa(FormatoTablaActa acta) {
        this.acta = acta;
    }

    public String obtenerDatos() {

        Map<String, String> map = new HashMap<>();

        map.put("numacta", numact);

        map.put("nombretg", TrabajodeGradoActual.nombreTg);

        map.put("nombredirector", nombreDirector);

        map.put("fecha", fechaact.toString());

        map.put("resultado", resultado);

        Gson gson = new Gson();
        String contenido = gson.toJson(map, Map.class);

        return contenido;
    }

    public String guardar() {
        try {

            String contenido = obtenerDatos();
            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(TrabajodeGradoActual.id), TrabajodeGradoActual.nombreTg);

            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.valueOf(5)));
            prod.setTrabajoid(trab);
            ejbFacadeProdTrab.create(prod);

            if (resultado.equals("1")) {
                List<TrabajogradoFase> lst = ejbFacadeTraFase1.findAll();

                for (int i = 0; i < lst.size(); i++) {
                    if (lst.get(i).getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                        if (lst.get(i).getFaseid().getFaseid().equals(BigDecimal.valueOf(3))) {
                            lst.get(i).setEstado(BigInteger.ONE);
                            ejbFacadeTraFase1.edit(lst.get(i));

                        }
                        if (lst.get(i).getFaseid().getFaseid().equals(BigDecimal.valueOf(4))) {
                            lst.get(i).setEstado(BigInteger.ZERO);
                            ejbFacadeTraFase1.edit(lst.get(i));

                        }
                    }
                }
            }

//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("La revision de la idea del Trabajo de Grado: '"+nombretg+"' ha sido diligenciada.");
//
//            if(est1!=null)
//            {  
//            se.setTo(est1.getPersonacorreo());
//            se.enviarDiligenciadoRevisionIdea(nombretg);
//            }
//            if(est2!=null)
//            {
//            se.setTo(est2.getPersonacorreo());
//            se.enviarDiligenciadoRevisionIdea(nombretg);
//            }
//            if(TrabajodeGradoActual.director!=null)
//            {
//            se.setTo(TrabajodeGradoActual.director.getPersonacorreo());
//            se.enviarDiligenciadoRevisionIdea(nombretg);
//            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Acta de resolución asignada."));
            return "diligenciar-acta-resolucion";
        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Ocurrio un problema al efectuar dicha operación."));
            return "diligenciar-acta-resolucion";
        }
    }

    public void eliminarActa() {

        FacesContext context = FacesContext.getCurrentInstance();
        try {

            ejbFacadeProdTrab.Eliminar_prod(acta.getIdprod());

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Acta Eliminada"));

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ocurrio algun error al intentar  efectuar la operacion"));
        }

    }

    public List<FormatoTablaActa> getActas() {

        List<Productodetrabajo> lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 5);
        List<FormatoTablaActa> lstact = new ArrayList<>();
        FormatoTablaActa act;

        Gson gson = new Gson();
        Map<String, String> map;

        for (Productodetrabajo p : lst) {
            map = gson.fromJson(p.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());

            act = new FormatoTablaActa(p.getProductoid().intValue(), map.get("numacta"), map.get("fecha"));
            lstact.add(act);
        }
        return lstact;
    }
}
