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
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author pcblanco
 */
@ManagedBean
@ViewScoped
public class Anteproyecto {

    @EJB
    private UsuarioFacade ejbFacadeUsuario;

    private String nombretg;
    private String directortg;
    private String est1;
    private String est2;
    private String objetivos;
    private UploadedFile file;
    private String nombarch;
    private StreamedContent filedown;
    private String rutaarch;
    private boolean archivomodificado = false;
    private Productodetrabajo anteproyactual;
    private final String directorioanteproyectos = "D:\\Archivos_TGU\\Anteproyectos\\";
    private Usuario doc1;
    private Usuario doc2;

    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;

    public Anteproyecto() {
    }

    @PostConstruct
    public void init() {

        List<Productodetrabajo> lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 2);
        if (lst.size() > 0) {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(lst.get(0).getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());

            nombarch = map.get("nombarch");
            objetivos = map.get("obj");
            rutaarch = map.get("rutaarch");         
            
            if(map.containsKey("iddoc1"))
                {
                    doc1 = ejbFacadeUsuario.find(new BigDecimal(map.get("iddoc1")));
                }
            if(map.containsKey("iddoc2"))
                {
                    doc2 = ejbFacadeUsuario.find(new BigDecimal(map.get("iddoc2")));
                }

            anteproyactual = lst.get(0);

            preparardescarga();
        }
    }

    public String getNombarch() {
        if (!"".equals(nombarch)) {
            return nombarch;
        } else if (file == null) {
            return "No se ha seleccionado un archivo";
        } else {
            return file.getFileName();
        }
    }

    public void setNombarch(String nombarch) {
        this.nombarch = nombarch;
    }

    public String getNombretg() {
        nombretg = TrabajodeGradoActual.nombreTg;
        return nombretg;
    }

    public void setNombretg(String nombretg) {
        this.nombretg = nombretg;
    }

    public String getDirectortg() {
        directortg = TrabajodeGradoActual.director.getPersonanombres() + " " + TrabajodeGradoActual.director.getPersonaapellidos();
        return directortg;
    }

    public void setDirectortg(String directortg) {
        this.directortg = directortg;
    }

    public String getEst1() {
        if (TrabajodeGradoActual.est1 != null) {
            est1 = TrabajodeGradoActual.est1.getPersonanombres() + " " + TrabajodeGradoActual.est1.getPersonaapellidos();
            return est1;
        } else {
            return "";
        }
    }

    public void setEst1(String est1) {
        this.est1 = est1;
    }

    public String getEst2() {
        if (TrabajodeGradoActual.est2 != null) {
            est2 = TrabajodeGradoActual.est2.getPersonanombres() + " " + TrabajodeGradoActual.est2.getPersonaapellidos();
            return est2;
        } else {
            return "";
        }
    }

    public void setEst2(String est2) {
        this.est2 = est2;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Usuario getDoc1() {
        return doc1;
    }

    public void setDoc1(Usuario doc1) {
        this.doc1 = doc1;
    }

    public Usuario getDoc2() {
        return doc2;
    }

    public void setDoc2(Usuario doc2) {
        this.doc2 = doc2;
    }

    public StreamedContent getFiledown() {
        return filedown;
    }

    public void copiarArchivo() throws FileNotFoundException, IOException {
        OutputStream out = new FileOutputStream(new File(directorioanteproyectos + file.getFileName()));
        InputStream in = file.getInputstream();

        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = in.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        in.close();
        out.flush();
        out.close();
    }

    public void guardar() {

        FacesContext context = FacesContext.getCurrentInstance();

        if (file != null && !"".equals(file.getFileName())) {

            try {
                copiarArchivo();
                Map<String, String> map = new HashMap<String, String>();

                map.put("nombretg", nombretg);
                map.put("directortg", directortg);
                map.put("est1", est1);
                map.put("est2", est2);
                map.put("obj", objetivos);
                map.put("rutaarch", directorioanteproyectos + file.getFileName());
                map.put("nombarch", file.getFileName());

                Gson gson = new Gson();
                String contenido = gson.toJson(map, Map.class);

                Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
                prod.setFormatoid(new Formatoproducto(BigDecimal.valueOf(2)));
                prod.setTrabajoid(new Trabajodegrado(BigDecimal.valueOf(TrabajodeGradoActual.id)));

                ejbFacadeProdTrab.create(prod);

//                Servicio_Email se = new Servicio_Email();
//                se.setSubject("Anteproyecto del Trabajo de Grado" + nombretg + " Diligenciado");
//
//            if (TrabajodeGradoActual.director != null) {
//                se.setTo(TrabajodeGradoActual.director.getPersonacorreo());
//                se.enviarDiligenciadoAnteproyecto(nombretg);
//            }          
                context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Anteproyecto Diligenciado con Exito", ""));

                /*  Para redireccionar despues de guardar
                 ExternalContext extcontext = FacesContext.getCurrentInstance().getExternalContext();
                 extcontext.redirect("fases-trabajo-de-grado.xhtml?trabajoid="+TrabajodeGradoActual.id);
                 */
            } catch (Exception e) {
                System.out.println(e.getMessage());
                context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ocurrio algun error al intentar  efectuar la operacion"));
            }

        } else {
            context.addMessage("archivoant", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Archivo de anteproyecto es obligatorio", "Archivo de anteproyecto es obligatorio"));
        }
    }

    public void editar() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {

            Gson gson = new Gson();
            Map<String, String> mapedicion
                    = gson.fromJson(anteproyactual.getProductocontenido(), new TypeToken<Map<String, String>>() {
                    }.getType());

            if (archivomodificado) {
                File f = new File(rutaarch);
                f.delete();
                copiarArchivo();
                mapedicion.put("rutaarch", directorioanteproyectos + file.getFileName());
                mapedicion.put("nombarch", file.getFileName());
            }

            mapedicion.put("obj", objetivos);

            String contenido = gson.toJson(mapedicion, Map.class);

            anteproyactual.setProductocontenido(contenido);

            ejbFacadeProdTrab.edit(anteproyactual);

            Servicio_Email se = new Servicio_Email();
            se.setSubject("Anteproyecto del Trabajo de Grado" + nombretg + " Editado");

            if (TrabajodeGradoActual.director != null) {
                se.setTo(TrabajodeGradoActual.director.getPersonacorreo());
                se.enviarEditadoAnteproyecto(nombretg);
            }

            context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Anteproyecto Editado con Exito", ""));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ocurrio algun error al intentar  efectuar la operacion"));
        }

    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        nombarch = file.getFileName();
        archivomodificado = true;
        if (filedown != null) {
            preparardescarga();
        }
    }

    public void preparardescarga() {
        try {
            if (archivomodificado) {
                filedown.getStream().close();
                filedown = new DefaultStreamedContent(file.getInputstream(), "application/pdf", nombarch);
            } else {
                filedown = new DefaultStreamedContent(new FileInputStream(rutaarch), "application/pdf", nombarch);
            }
        } catch (Exception e) {
        }

    }

    public List<Usuario> complete(String query) {

        query = query.trim();
        query = query.toUpperCase();

        List<Usuario> ls = ejbFacadeUsuario.buscarEvaluadores(query);

        return ls;
    }

    public void guardarevaluadores() {
         
       FacesContext context = FacesContext.getCurrentInstance();
        try {          
        Gson gson = new Gson();
        Map<String, String> mapedicion
                = gson.fromJson(anteproyactual.getProductocontenido(), new TypeToken<Map<String, String>>() {
                }.getType());

        if (doc1 == null) {
            if (mapedicion.containsKey("iddoc1")) {
                mapedicion.remove("iddoc1");
                mapedicion.remove("nombredoc1");
            }
        } else {
             mapedicion.put("iddoc1", doc1.getPersonacedula().intValue()+"");
             mapedicion.put("nombredoc1", doc1.getPersonanombres()+" "+doc1.getPersonaapellidos());
        }
        
        if (doc2 == null) {
            if (mapedicion.containsKey("iddoc2")) {
                mapedicion.remove("iddoc2");
                mapedicion.remove("nombredoc2");
            }
        } else {
             mapedicion.put("iddoc2", doc2.getPersonacedula().intValue()+"");
             mapedicion.put("nombredoc2", doc2.getPersonanombres()+" "+doc2.getPersonaapellidos());
        }
        
        String contenido = gson.toJson(mapedicion, Map.class);

        anteproyactual.setProductocontenido(contenido);

        ejbFacadeProdTrab.edit(anteproyactual);
        
        Servicio_Email se = new Servicio_Email();
        se.setSubject("Asignacion como evaluador de anteproyecto");

            if (doc1 != null) {
                se.setTo(doc1.getPersonacorreo());
                se.enviarAsignacionEvaluacionanteproyecto(nombretg);
            }
        
            if (doc2 != null) {
                se.setTo(doc2.getPersonacorreo());
                se.enviarAsignacionEvaluacionanteproyecto(nombretg);
            }
            
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Evaluadores Asignados correctamente"));
        //ExternalContext extcontext = context.getExternalContext();
        // extcontext.redirect("fases-trabajo-de-grado.xhtml");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ocurrio algun error al intentar  efectuar la operacion"));
        }

    }
}