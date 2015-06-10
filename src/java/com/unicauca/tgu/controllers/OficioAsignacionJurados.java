package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.Servicio_Email;
import com.unicauca.tgu.Auxiliares.ServiciosSimcaController;
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
public class OficioAsignacionJurados
{
    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private UsuarioFacade ejbFacadeUsuario;
    @EJB
    private UsuarioRolFacade ejbFacadeUsuRol;
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuRolTrab;
    
    private String numOficio;
    private String nombretg;
    private int trabajoid;
    private Usuario est1;
    private Usuario est2;
    private Usuario director;
    private Date fechaOficio;
    private Usuario jurado1;
    private Usuario jurado2;
    private String nombreJurado1;
    private String nombreJurado2;
    private Date fecha1;
    private Date fecha2;
    private Date fecha3;
    private int idSecretaria;
    
    private Productodetrabajo productoActual;
    
    public OficioAsignacionJurados() {
    }

    @PostConstruct
    public void init()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        ServiciosSimcaController s =  (ServiciosSimcaController)context.getApplication().evaluateExpressionGet(context, "#{serviciosSimcaController}", ServiciosSimcaController.class);
        idSecretaria = s.getUsulog().getPersonacedula().intValue();
        
        numOficio = "1A";
        nombretg = TrabajodeGradoActual.nombreTg;
        trabajoid = TrabajodeGradoActual.id;
        est1 = TrabajodeGradoActual.est1;
        est2 = TrabajodeGradoActual.est2;
        director = TrabajodeGradoActual.director;
        nombreJurado1 = "";
        nombreJurado2 = "";
        
        
        fecha1 = null;
        fecha2 = null;
        fecha3 = null;
        
        List<Productodetrabajo> lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(trabajoid, 8); //Formato: Oficio Asignacion de Jurados
        if (lst.size() > 0) {
            SimpleDateFormat formateador = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            
            productoActual = lst.get(0);
            
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(productoActual.getProductocontenido(), new TypeToken<Map<String, String>>() {}.getType());
            
            if (map.containsKey("numOficio")) {
                numOficio = map.get("numOficio");
            }
            if (map.containsKey("idJurado1")) {
                jurado1 = ejbFacadeUsuario.find(new BigDecimal(map.get("idJurado1")));
            }
            if (map.containsKey("idJurado2")) {
                jurado2 = ejbFacadeUsuario.find(new BigDecimal(map.get("idJurado2")));
            }
            if (map.containsKey("nombreJurado1")) {
                nombreJurado1 = map.get("nombreJurado1");
            }
            if (map.containsKey("nombreJurado2")) {
                nombreJurado2 = map.get("nombreJurado2");
            }
            
            try
            {
                if (map.containsKey("fecha1")) {
                    fecha1 = (Date) formateador.parse(map.get("fecha1"));
                }
                if (map.containsKey("fecha2")) {
                    fecha2 = (Date) formateador.parse(map.get("fecha2"));
                }
                if (map.containsKey("fecha3")) {
                    fecha3 = (Date) formateador.parse(map.get("fecha3"));
                }
                if (map.containsKey("fechaOficio")) {
                    fechaOficio = formateador.parse(map.get("fechaOficio"));
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else
        {
            List<Productodetrabajo> lst1 = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(trabajoid, 7); //Formato G
            if (lst1.size() > 0) {
                
                Gson gson = new Gson();
                Map<String, String> map = gson.fromJson(lst1.get(0).getProductocontenido(), new TypeToken<Map<String, String>>() {}.getType());
                
                if (map.containsKey("jurado1Id")) {
                    jurado1 = ejbFacadeUsuario.find(new BigDecimal(map.get("jurado1Id")));
                }
                if (map.containsKey("jurado2Id")) {
                    jurado2 = ejbFacadeUsuario.find(new BigDecimal(map.get("jurado2Id")));
                }
                if (map.containsKey("jurado1")) {
                    nombreJurado1 = map.get("jurado1");
                }
                if (map.containsKey("jurado2")) {
                    nombreJurado2 = map.get("jurado2");
                }
            }
            fechaOficio = new Date();
        }
    }

    public String getNumOficio() {
        return numOficio;
    }

    public void setNumOficio(String numOficio) {
        this.numOficio = numOficio;
    }

    public String getNombretg() {
        return nombretg;
    }

    public void setNombretg(String nombretg) {
        this.nombretg = nombretg;
    }

    public int getTrabajoid() {
        return trabajoid;
    }

    public void setTrabajoid(int trabajoid) {
        this.trabajoid = trabajoid;
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

    public Usuario getDirector() {
        return director;
    }

    public void setDirector(Usuario director) {
        this.director = director;
    }

    public Date getFechaOficio() {
        return fechaOficio;
    }

    public void setFechaOficio(Date fechaOficio) {
        this.fechaOficio = fechaOficio;
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

    public Date getFecha1() {
        return fecha1;
    }

    public void setFecha1(Date fecha1) {
        this.fecha1 = fecha1;
    }

    public Date getFecha2() {
        return fecha2;
    }

    public void setFecha2(Date fecha2) {
        this.fecha2 = fecha2;
    }

    public Date getFecha3() {
        return fecha3;
    }

    public void setFecha3(Date fecha3) {
        this.fecha3 = fecha3;
    }

    public Productodetrabajo getProductoActual() {
        return productoActual;
    }

    public void setProductoActual(Productodetrabajo productoActual) {
        this.productoActual = productoActual;
    }

    public String getNombreJurado1() {
        return nombreJurado1;
    }

    public void setNombreJurado1(String nombreJurado1) {
        this.nombreJurado1 = nombreJurado1;
    }

    public String getNombreJurado2() {
        return nombreJurado2;
    }

    public void setNombreJurado2(String nombreJurado2) {
        this.nombreJurado2 = nombreJurado2;
    }
    
    public List<Usuario> complete(String query) {

        query = query.trim();
        query = query.toUpperCase();

        if (query.isEmpty()) {
            jurado1 = null;
        }
        List<Usuario> ls = ejbFacadeUsuario.buscarJurados(idSecretaria, query);
        List<Usuario> usus = new ArrayList();
        
        for (Usuario u : ls)
        {
            if(u.getPersonacedula().intValue() == director.getPersonacedula().intValue())
                continue;
            List<UsuarioRol> lstTrab = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 7);
            if (lstTrab.isEmpty())
                continue;
            usus.add(u);
        }
        if (jurado2 != null) {
            for (Usuario u : ls) {
                if (u.getPersonacedula().intValue() == jurado2.getPersonacedula().intValue())
                    usus.remove(u);
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

        List<Usuario> ls = ejbFacadeUsuario.buscarJurados(idSecretaria, query);
        List<Usuario> usus = new ArrayList();
        
        for (Usuario u : ls)
        {
            if(u.getPersonacedula().intValue() == director.getPersonacedula().intValue())
                continue;
            List<UsuarioRol> lstTrab = ejbFacadeUsuRol.findByUsuid_Rolid(u.getPersonacedula().intValue(), 7);
            if (lstTrab.isEmpty())
                continue;
            usus.add(u);
        }
        if (jurado1 != null) {
            for (Usuario u : ls) {
                if (u.getPersonacedula().intValue() == jurado1.getPersonacedula().intValue())
                    usus.remove(u);
            }
        }
        return usus;
    }
        
    public void handlerSelectDoc1(SelectEvent e) {
        jurado1 = (Usuario) e.getObject();
        nombreJurado1 = jurado1.getPersonanombres() + " " + jurado1.getPersonaapellidos();
    }

    public void handlerSelectDoc2(SelectEvent e) {
        jurado2 = (Usuario) e.getObject();
        nombreJurado2 = jurado2.getPersonanombres() + " " + jurado2.getPersonaapellidos();
    }

    public void handleUnSelectDoc1(UnselectEvent e) {
        jurado1 = null;
        nombreJurado1 = "";
    }

    public void handleUnSelectDoc2(UnselectEvent e) {
        jurado2 = null;
        nombreJurado2 = "";
    }
    
    public String obtenerDatos()
    {
        Map<String, String> map = new HashMap();
        
        map.put("numOficio", getNumOficio());
        map.put("titulo", getNombretg());
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
        map.put("iddirector", Integer.toString(director.getPersonacedula().intValue()));
        map.put("nombredirector", director.getPersonanombres()+" "+director.getPersonaapellidos());
        
        if (jurado1 == null) {
            if (map.containsKey("idJurado1")) {
                map.remove("idJurado1");
                map.remove("nombreJurado1");
            }
        } else {
            map.put("idJurado1", jurado1.getPersonacedula().intValue() + "");
            map.put("nombreJurado1", jurado1.getPersonanombres() + " " + jurado1.getPersonaapellidos());
        }

        if (jurado2 == null) {
            if (map.containsKey("idJurado2")) {
                map.remove("idJurado2");
                map.remove("nombreJurado2");
            }
        } else {
            map.put("idJurado2", jurado2.getPersonacedula().intValue() + "");
            map.put("nombreJurado2", jurado2.getPersonanombres() + " " + jurado2.getPersonaapellidos());
        }
        map.put("fechaOficio", getFechaOficio().toString());
        map.put("fecha1", getFecha1().toString());
        map.put("fecha2", getFecha2().toString());
        
        if(getFecha3() != null)
            map.put("fecha3", getFecha3().toString());
       
        Gson gson = new Gson();
        String contenido = gson.toJson(map, Map.class);
        return contenido;
    }
    
    public String guardar()
    {
        try {
            String contenido = obtenerDatos();
            Trabajodegrado trab = new Trabajodegrado(new BigDecimal(trabajoid), nombretg);
            
            UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fechaOficio);
            usuroltg.setTrabajoid(trab);

            if (jurado1 != null) {
                usuroltg.setRolid(new Rol(BigDecimal.valueOf(7)));        //Se asigna el primer jurado al TG
                usuroltg.setPersonacedula(jurado1);
                ejbFacadeUsuRolTrab.create(usuroltg);
            }
            
            if (jurado2 != null) {
                usuroltg.setRolid(new Rol(BigDecimal.valueOf(7)));        //Se asigna el segundo jurado al TG
                usuroltg.setPersonacedula(jurado2);
                ejbFacadeUsuRolTrab.create(usuroltg);
            }
            
            Productodetrabajo prod = new Productodetrabajo(BigDecimal.ZERO, BigInteger.ZERO, contenido);
            prod.setFormatoid(new Formatoproducto(BigDecimal.valueOf(8)));
            prod.setTrabajoid(trab);
            ejbFacadeProdTrab.create(prod);
            
            //AQUI SE ENVIA EL CORREO CEPEDA. Req. 56 y 57
            
//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("Usted ha sido seleccionado para evaluar el proyecto de grado: '" + nombretg+'"');
//
//            if (jurado1 != null) {
//                se.setTo(jurado1.getPersonacorreo());
//                se.enviarNotificacionAsignacionJurados(nombretg, fecha1, fecha2, fecha3);
//            }
//            if (jurado2 != null) {
//                se.setTo(jurado2.getPersonacorreo());
//                se.enviarNotificacionAsignacionJurados(nombretg, fecha1, fecha2, fecha3);
//            }
//            
//            Servicio_Email se1 = new Servicio_Email();
//            se1.setSubject("El trabajo de grado: '" + nombretg+'"'+" se le acaban de asignar jurados");
//            
//            if (director != null) {
//                se1.setTo(director.getPersonacorreo());
//                se1.enviarNotificacionAsignacionJuradosADirectorEstudiantes(nombretg, fecha1, fecha2, fecha3);
//            }
//            
//            if (est1 != null) {
//                se1.setTo(est1.getPersonacorreo());
//                se1.enviarNotificacionAsignacionJuradosADirectorEstudiantes(nombretg, fecha1, fecha2, fecha3);
//            }
//            
//            if (est2 != null) {
//                se1.setTo(est2.getPersonacorreo());
//                se1.enviarNotificacionAsignacionJuradosADirectorEstudiantes(nombretg, fecha1, fecha2, fecha3);
//            }

            
            
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Oficio de asignación de jurados, diligenciado con éxito."));
            return "fase-5";
        } 
        catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocurrio un problema al efectuar dicha operación.", ""));
            return "diligenciar-asignacion-jurados";
        }
    }
    
    public String editar()
    {
        try {
            UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fechaOficio);
            usuroltg.setTrabajoid(new Trabajodegrado(BigDecimal.valueOf(trabajoid), nombretg));
            usuroltg.setRolid(new Rol(BigDecimal.valueOf(7)));
            
            int idJurado1Ant = -1, idJurado2Ant = -1;
            
            Gson gson = new Gson();
            Map<String, String> mapedicion = gson.fromJson(productoActual.getProductocontenido(), new TypeToken<Map<String, String>>() {}.getType());
            
            if (mapedicion.containsKey("idJurado1")) {
               idJurado1Ant = Integer.parseInt(mapedicion.get("idJurado1"));
            }
            if (mapedicion.containsKey("idJurado2")) {
               idJurado2Ant = Integer.parseInt(mapedicion.get("idJurado2"));
            }
            
            if (jurado1 != null) {
                usuroltg.setPersonacedula(jurado1);
                if (idJurado1Ant == -1) { //agregando primer jurado
                    ejbFacadeUsuRolTrab.create(usuroltg);

                } else if (idJurado1Ant != jurado1.getPersonacedula().intValue()) { //Se elimina el anterior jurado y se agrega el nuevo.
                    List<UsuarioRolTrabajogrado> tmp = ejbFacadeUsuRolTrab.findByUsuid_Rolid_Trabid(idJurado1Ant, 7, trabajoid);
                    ejbFacadeUsuRolTrab.remove(tmp.get(0));
                    ejbFacadeUsuRolTrab.edit(usuroltg);
                }
            }
            if (jurado2 != null) {
                usuroltg.setPersonacedula(jurado2);
                if (idJurado2Ant == -1) { //agregando segundo jurado
                    ejbFacadeUsuRolTrab.create(usuroltg);

                } else if (idJurado2Ant != jurado2.getPersonacedula().intValue()) { //Se elimina el anterior jurado y se agrega el nuevo.
                    List<UsuarioRolTrabajogrado> tmp = ejbFacadeUsuRolTrab.findByUsuid_Rolid_Trabid(idJurado2Ant, 7, trabajoid);
                    ejbFacadeUsuRolTrab.remove(tmp.get(0));
                    ejbFacadeUsuRolTrab.edit(usuroltg);
                }
            }
                       
            String contenido = obtenerDatos();
            productoActual.setProductocontenido(contenido);            
            ejbFacadeProdTrab.edit(productoActual);
            
            //AQUI SE ENVIA EL CORREO CEPEDA. Req. 56 y 57
            
//            Servicio_Email se = new Servicio_Email();
//            se.setSubject("Usted ha sido seleccionado para evaluar el proyecto de grado: '" + nombretg+'"');
//
//            if (jurado1 != null) {
//                se.setTo(jurado1.getPersonacorreo());
//                se.enviarNotificacionAsignacionJurados(nombretg, fecha1, fecha2, fecha3);
//            }
//            if (jurado2 != null) {
//                se.setTo(jurado2.getPersonacorreo());
//                se.enviarNotificacionAsignacionJurados(nombretg, fecha1, fecha2, fecha3);
//            }
//            
//            Servicio_Email se1 = new Servicio_Email();
//            se1.setSubject("El trabajo de grado: '" + nombretg+'"'+" se le acaban de asignar jurados");
//            
//            if (director != null) {
//                se1.setTo(director.getPersonacorreo());
//                se1.enviarNotificacionAsignacionJuradosADirectorEstudiantes(nombretg, fecha1, fecha2, fecha3);
//            }
//            
//            if (est1 != null) {
//                se1.setTo(est1.getPersonacorreo());
//                se1.enviarNotificacionAsignacionJuradosADirectorEstudiantes(nombretg, fecha1, fecha2, fecha3);
//            }
//            
//            if (est2 != null) {
//                se1.setTo(est2.getPersonacorreo());
//                se1.enviarNotificacionAsignacionJuradosADirectorEstudiantes(nombretg, fecha1, fecha2, fecha3);
//            }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Oficio de asignación de jurados, editado con éxito."));
            return "fase-5";
        }
        catch (JsonSyntaxException | NumberFormatException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocurrio un problema al efectuar dicha operación.", ""));
            return "editar-asignacion-jurados";
        }
    }
    
    public Date getToday() {
        return new Date();
    }
}