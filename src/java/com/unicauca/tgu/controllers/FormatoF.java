/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.ServiciosSimcaController;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.Formatoproducto;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author pcblanco
 */
@ManagedBean
@ViewScoped
public class FormatoF {

    private String nombretg;
    private String nomEst1;
    private String nomEst2;
    private String nomDirector;
    private String cumpleEntrega;
    private String cumpleDocyAnexos;
    private String obs;
    private Date fecha;
    private UploadedFile anexos;
    private UploadedFile articulos;
    private UploadedFile trabajogrado;
    private StreamedContent anexosdown;
    private StreamedContent articulosdown;
    private StreamedContent trabdown;
    private String nombarchanex;
    private String nombarchart;
    private String nombarchtrab;
    private boolean archivomodificadoanex;
    private boolean archivomodificadoart;
    private boolean archivomodificadotrab;
    private String directorioformatoF;
    private String rutaarchanex;
    private String rutaarchart;
    private String rutaarchtrab;
    private String urlrep;
    private Productodetrabajo productoActual;

    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;

    public FormatoF() {
        cumpleEntrega = "No";
        cumpleDocyAnexos = "No";
    }

    @PostConstruct
    public void init() {
        nombretg = TrabajodeGradoActual.nombreTg;
        nomEst1 = TrabajodeGradoActual.est1.getPersonanombres() + " " + TrabajodeGradoActual.est1.getPersonaapellidos();
        nomDirector = TrabajodeGradoActual.director.getPersonanombres() + " " + TrabajodeGradoActual.director.getPersonaapellidos();

        if (TrabajodeGradoActual.est2 != null) {
            nomEst2 = TrabajodeGradoActual.est2.getPersonanombres() + " " + TrabajodeGradoActual.est2.getPersonaapellidos();
        }

        fecha = new Date();
        directorioformatoF = "D:\\Archivos_TGU\\FormatoF\\" + TrabajodeGradoActual.nombreTg + "\\";

        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s = (ServiciosSimcaController) context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);

        List<Productodetrabajo> lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 6);

        if (lst.size() > 0) {               //verificar si ya hay guardardo un formato A para este trabajo de grado

            productoActual = lst.get(0);
            Gson gson = new Gson();
            Map<String, String> decoded = gson.fromJson(productoActual.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());

            if (decoded.containsKey("rutaarchanexos")) {
                rutaarchanex = decoded.get("rutaarchanexos");
            }
            if (decoded.containsKey("nombarchanex")) {
                nombarchanex = decoded.get("nombarchanex");
            }

            if (decoded.containsKey("rutaarchart")) {
                rutaarchart = decoded.get("rutaarchart");
            }
            if (decoded.containsKey("nombarchart")) {
                nombarchart = decoded.get("nombarchart");
            }

            if (decoded.containsKey("rutaarchtrab")) {
                rutaarchtrab = decoded.get("rutaarchtrab");
            }
            if (decoded.containsKey("nombarchtrab")) {
                nombarchtrab = decoded.get("nombarchtrab");
            }

            if (decoded.containsKey("obs")) {
                obs = decoded.get("obs");
            }

            if (decoded.containsKey("urlrep")) {
                urlrep = decoded.get("urlrep");
            }

            if (decoded.containsKey("cumpleDocyAnexos")) {
                cumpleDocyAnexos = decoded.get("cumpleDocyAnexos");
            }

            if (decoded.containsKey("cumpleEntrega")) {
                cumpleEntrega = decoded.get("cumpleEntrega");
            }

            if (decoded.containsKey("fecha")) {
                try {
                    fecha = new SimpleDateFormat("dd-MM-yyyy").parse(decoded.get("fecha"));
                } catch (ParseException ex) {
                    Logger.getLogger(FormatoA.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (!"".equals(nombarchanex)) {
                preparardescarga(1);
            }
            if (!"".equals(nombarchart)) {
                preparardescarga(2);
            }
            if (!"".equals(nombarchtrab)) {
                preparardescarga(3);
            }
        }

    }

    public String getNombretg() {
        return nombretg;
    }

    public void setNombretg(String nombretg) {
        this.nombretg = nombretg;
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

    public void setNomEst2(String nomEst2) {
        this.nomEst2 = nomEst2;
    }

    public String getNomDirector() {
        return nomDirector;
    }

    public void setNomDirector(String nomDirector) {
        this.nomDirector = nomDirector;
    }

    public String getCumpleEntrega() {
        return cumpleEntrega;
    }

    public void setCumpleEntrega(String cumpleEntrega) {
        this.cumpleEntrega = cumpleEntrega;
    }

    public String getCumpleDocyAnexos() {
        return cumpleDocyAnexos;
    }

    public void setCumpleDocyAnexos(String cumpleDocyAnexos) {
        this.cumpleDocyAnexos = cumpleDocyAnexos;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombarchanex() {
        return nombarchanex;
    }

    public void setNombarch(String nombarch) {
        this.nombarchanex = nombarch;
    }

    public String getUrlrep() {
        return urlrep;
    }

    public void setUrlrep(String urlrep) {
        this.urlrep = urlrep;
    }

    public StreamedContent getArticulosdown() {
        return articulosdown;
    }

    public void setArticulosdown(StreamedContent articulosdown) {
        this.articulosdown = articulosdown;
    }

    public StreamedContent getAnexosdown() {
        return anexosdown;
    }

    public void setAnexosdown(StreamedContent anexosdown) {
        this.anexosdown = anexosdown;
    }

    public String getNombarchart() {
        return nombarchart;
    }

    public void setNombarchart(String nombarchart) {
        this.nombarchart = nombarchart;
    }

    public StreamedContent getTrabdown() {
        return trabdown;
    }

    public void setTrabdown(StreamedContent trabdown) {
        this.trabdown = trabdown;
    }

    public String getNombarchtrab() {
        return nombarchtrab;
    }

    public void setNombarchtrab(String nombarchtrab) {
        this.nombarchtrab = nombarchtrab;
    }

    public void handleFileUpload(FileUploadEvent event) {
        UIComponent f = event.getComponent();

        if (f.getId().equals("anexos")) {
            anexos = event.getFile();
            nombarchanex = anexos.getFileName();
            archivomodificadoanex = true;
            if (anexosdown != null) {
                preparardescarga(1);
            }
        }
        if (f.getId().equals("articulos")) {
            articulos = event.getFile();
            nombarchart = articulos.getFileName();
            archivomodificadoart = true;
            if (articulosdown != null) {
                preparardescarga(2);
            }
        }
        if (f.getId().equals("trabgrad")) {
            trabajogrado = event.getFile();
            nombarchtrab = trabajogrado.getFileName();
            archivomodificadotrab = true;
            if (trabdown != null) {
                preparardescarga(3);
            }
        }
    }

    public void preparardescarga(int archivo) {   // 1 anexos   - 2 articulos 
        try {

            if (archivo == 1) {
                if (archivomodificadoanex) {
                    anexosdown.getStream().close();
                    anexosdown = new DefaultStreamedContent(anexos.getInputstream(), "application/pdf", nombarchanex);
                } else {
                    anexosdown = new DefaultStreamedContent(new FileInputStream(rutaarchanex), "application/pdf", nombarchanex);
                }
            }
            if (archivo == 2) {
                if (archivomodificadoart) {
                    articulosdown.getStream().close();
                    articulosdown = new DefaultStreamedContent(articulos.getInputstream(), "application/pdf", nombarchart);
                } else {
                    articulosdown = new DefaultStreamedContent(new FileInputStream(rutaarchanex), "application/pdf", nombarchart);
                }
            }
            if (archivo == 3) {
                if (archivomodificadotrab) {
                    trabdown.getStream().close();
                    trabdown = new DefaultStreamedContent(trabajogrado.getInputstream(), "application/pdf", nombarchtrab);
                } else {
                    trabdown = new DefaultStreamedContent(new FileInputStream(rutaarchtrab), "application/pdf", nombarchtrab);
                }
            }
        } catch (Exception e) {
        }

    }

    public void copiarArchivo(int archivo) throws FileNotFoundException, IOException, Exception {

        File f = new File(directorioformatoF);
        Exception IOException = null;

        if (!f.exists()) {
            if (!f.mkdirs()) {
                throw IOException;
            }
        }

        OutputStream out;
        InputStream in;
        if (archivo == 1) {
            out = new FileOutputStream(new File(directorioformatoF + anexos.getFileName()));
            in = anexos.getInputstream();
        } else if (archivo == 2) {
            out = new FileOutputStream(new File(directorioformatoF + articulos.getFileName()));
            in = articulos.getInputstream();
        } else {
            out = new FileOutputStream(new File(directorioformatoF + trabajogrado.getFileName()));
            in = trabajogrado.getInputstream();
        }

        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = in.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        in.close();
        out.flush();
        out.close();
    }

    public String obtenerDatos() {
        Map<String, String> map = new HashMap();

        map.put("nombretg", nombretg);
        if (TrabajodeGradoActual.director != null) {
            map.put("idDir", TrabajodeGradoActual.director.getPersonacedula().toString());
        }
        map.put("directortg", nomDirector);
        if (TrabajodeGradoActual.est1 != null) {
            map.put("idEst1", TrabajodeGradoActual.est1.getPersonacedula().toString());
        }
        map.put("est1", nomEst1);
        if (TrabajodeGradoActual.est2 != null) {
            map.put("idEst2", TrabajodeGradoActual.est2.getPersonacedula().toString());
        }
        map.put("est2", nomEst2);

        map.put("cumpleDocyAnexos", cumpleDocyAnexos);
        map.put("cumpleEntrega", cumpleEntrega);

        map.put("obs", obs);
        if (archivomodificadoanex) {
            map.put("rutaarchanexos", directorioformatoF + anexos.getFileName());
            map.put("nombarchanex", anexos.getFileName());
        } else {
            map.put("rutaarchanexos", rutaarchanex);
            map.put("nombarchanex", nombarchanex);
        }

        if (archivomodificadoart) {
            map.put("rutaarchart", directorioformatoF + articulos.getFileName());
            map.put("nombarchart", articulos.getFileName());
        } else {
            map.put("rutaarchart", rutaarchart);
            map.put("nombarchart", nombarchart);
        }

        if (archivomodificadotrab) {
            map.put("rutaarchtrab", directorioformatoF + trabajogrado.getFileName());
            map.put("nombarchtrab", trabajogrado.getFileName());
        } else {
            map.put("rutaarchtrab", rutaarchtrab);
            map.put("nombarchtrab", nombarchtrab);
        }

        map.put("urlrep", urlrep);

        Gson gson = new Gson();
        return gson.toJson(map, Map.class);
    }

    public String guardar() {

        try {
            if (anexos != null) {
                copiarArchivo(1);
            }
            if (articulos != null) {
                copiarArchivo(2);
            }
            if (trabajogrado != null) {
                copiarArchivo(3);
            }

            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, obtenerDatos());
            prod.setFormatoid(new Formatoproducto(BigDecimal.valueOf(6)));
            prod.setTrabajoid(new Trabajodegrado(BigDecimal.valueOf(TrabajodeGradoActual.id)));

            ejbFacadeProdTrab.create(prod);

//                Servicio_Email se = new Servicio_Email();
//                se.setSubject("Anteproyecto del Trabajo de Grado" + nombretg + " Diligenciado");
//
//            if (TrabajodeGradoActual.director != null) {
//                se.setTo(TrabajodeGradoActual.director.getPersonacorreo());
//                se.enviarDiligenciadoAnteproyecto(nombretg);
//            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato F diligenciado con éxito."));
            return "fase-4";

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Ocurrio un problema al efectuar dicha operación."));
            return "diligenciar-formato-F";
        }
    }

    public String editar() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {

            if (archivomodificadoanex) {
                File f = new File(rutaarchanex);
                f.delete();
                copiarArchivo(1);

            }

            if (archivomodificadoart) {
                File f = new File(rutaarchart);
                f.delete();
                copiarArchivo(2);

            }

            if (archivomodificadotrab) {
                File f = new File(rutaarchtrab);
                f.delete();
                copiarArchivo(3);

            }

            productoActual.setProductocontenido(obtenerDatos());

            ejbFacadeProdTrab.edit(productoActual);

//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("Anteproyecto del Trabajo de Grado" + nombretg + " Editado");
//
//            if (TrabajodeGradoActual.director != null) {
//                se.setTo(TrabajodeGradoActual.director.getPersonacorreo());
//                se.enviarEditadoAnteproyecto(nombretg);
//            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Formato F editado con éxito."));
            return "fase-4";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Ocurrio un problema al efectuar dicha operación."));
            return "editar-formato-F";
        }
    }

    public String onFlowProcess(FlowEvent event) {
        if (!event.getOldStep().equals("paso2")) {
            return event.getNewStep();
        }
        if (trabajogrado!=null && anexos!=null && articulos!=null) {
            return event.getNewStep();
        } else {
            if(trabajogrado==null)FacesContext.getCurrentInstance().addMessage("tabarchivos", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incompleto : Falta el archivo del trabajo de grado",""));
            if(anexos==null)FacesContext.getCurrentInstance().addMessage("tabarchivos", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incompleto : Falta el archivo de anexos",""));
            if(articulos==null)FacesContext.getCurrentInstance().addMessage("tabarchivos", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incompleto : Falta el archivo de articulos",""));
            return event.getOldStep();
        }
    }
}
