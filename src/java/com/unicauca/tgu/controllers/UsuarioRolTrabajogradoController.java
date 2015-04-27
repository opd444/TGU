package com.unicauca.tgu.controllers;

import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.Auxiliares.UsuarioComun;
import com.unicauca.tgu.FormatosTablas.FormatoTablaDirector;
import com.unicauca.tgu.FormatosTablas.FormatoTablaJefe;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.controllers.util.JsfUtil;
import com.unicauca.tgu.controllers.util.PaginationHelper;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "usuarioRolTrabajogradoController")
@SessionScoped
public class UsuarioRolTrabajogradoController implements Serializable {

    private UsuarioRolTrabajogrado current;
    private DataModel items = null;
    @EJB
    private com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade ejbFacade;

    @EJB
    private com.unicauca.tgu.jpacontroller.TrabajodegradoFacade ejbFacadetrabgrad;

    @EJB
    private com.unicauca.tgu.jpacontroller.UsuarioFacade ejbFacadeusuario;

    private PaginationHelper pagination;
    private int selectedItemIndex;

    private List<FormatoTablaDirector> trabs;
    //int modo;             // 0 para la seccion de trabajos en curso y 1 para trabajos terminados
    private boolean modo;             // 0 para la seccion de trabajos en curso y 1 para trabajos terminados
    private String titulotablaDirector;
    private String titulotablaJefe;
    private String titulotabla;
    private String nombrenuevoTG;
    private Date fecha;
    private List<FormatoTablaJefe> trabs1;
    private List<FormatoTablaJefe> trabs2;
    private int filas;
    private boolean numMaxTrabPermitidos;

    public UsuarioRolTrabajogradoController() {

    }

    public void trabajoGrado() {
        String usuarionombre = "fp";
        List<UsuarioRolTrabajogrado> urtgs = getFacade().findAll();
        UsuarioRolTrabajogrado usuarioroltrabajoid;
        for (int i = 0; i < urtgs.size(); i++) {
            if (urtgs.get(i).getPersonacedula().getUsuarionombre().equals(usuarionombre)) {
                current = usuarioroltrabajoid = urtgs.get(i);
            }
        }
    }

    public void trabajoDeGrado(BigDecimal trabajoid) {
        List<UsuarioRolTrabajogrado> urtgs = getFacade().findAll();
        UsuarioRolTrabajogrado usuarioroltrabajoid;
        for (int i = 0; i < urtgs.size(); i++) {
            if (urtgs.get(i).getTrabajoid().getTrabajoid().equals(trabajoid)) {
                current = urtgs.get(i);
            }
        }
    }

    public UsuarioRolTrabajogrado getCurrent() {
        trabajoGrado();
        return current;
    }

    public void setCurrent(UsuarioRolTrabajogrado current) {
        this.current = current;
    }

    @PostConstruct
    public void init() {
        modo = false;
        titulotablaDirector = "Trabajos de Grado en Curso";
        titulotablaJefe = "Lista de ideas por revisar";
        fecha = new Date();
    }

    public UsuarioRolTrabajogrado getSelected() {
        if (current == null) {
            current = new UsuarioRolTrabajogrado();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UsuarioRolTrabajogradoFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (UsuarioRolTrabajogrado) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new UsuarioRolTrabajogrado();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioRolTrabajogradoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (UsuarioRolTrabajogrado) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioRolTrabajogradoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (UsuarioRolTrabajogrado) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioRolTrabajogradoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = UsuarioRolTrabajogrado.class)
    public static class UsuarioRolTrabajogradoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuarioRolTrabajogradoController controller = (UsuarioRolTrabajogradoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuarioRolTrabajogradoController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.math.BigDecimal getKey(String value) {
            java.math.BigDecimal key;
            key = new java.math.BigDecimal(value);
            return key;
        }

        String getStringKey(java.math.BigDecimal value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof UsuarioRolTrabajogrado) {
                UsuarioRolTrabajogrado o = (UsuarioRolTrabajogrado) object;
                return getStringKey(o.getUsuarioroltrabajoid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + UsuarioRolTrabajogrado.class.getName());
            }
        }

    }

    public List<FormatoTablaDirector> getTrabs() {
        trabs = new ArrayList();

        List<Trabajodegrado> trabstemp;

        if (modo == true) {
            trabstemp = ejbFacadetrabgrad.getTrabajosTerminadosporDirectorId(UsuarioComun.id);
        } else {
            trabstemp = ejbFacadetrabgrad.getTrabajosEnCursoPorDirectorId(UsuarioComun.id);
        }

        int cont;
        FormatoTablaDirector f = new FormatoTablaDirector();

        for (Trabajodegrado t : trabstemp) {
            cont = 0;
            f = new FormatoTablaDirector();                  //sacamos la informacion general tanto director y los estud.
            f.setFecha(t.getUsuarioRolTrabajogradoList().get(0).getFechaasignacion());
            f.setTrabajoGradoId(t.getUsuarioRolTrabajogradoList().get(0).getTrabajoid().getTrabajoid().intValue());
            f.setTrabajoGrado(t.getUsuarioRolTrabajogradoList().get(0).getTrabajoid().getTrabajonombre());
            for (UsuarioRolTrabajogrado l : t.getUsuarioRolTrabajogradoList()) {
                if (l.getRolid().getRolid().intValue() == 1 && cont == 0) {
                    f.setEst1(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                    f.setEst1Id(l.getPersonacedula().getPersonacedula().intValue());
                    cont++;
                } else if (l.getRolid().getRolid().intValue() == 1 && cont == 1) {
                    f.setEst2(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                    f.setEst2Id(l.getPersonacedula().getPersonacedula().intValue());
                }
            }
            if (modo == false) {
                List<TrabajogradoFase> tgfs = t.getTrabajogradoFaseList();
                int x = 999;
                for (TrabajogradoFase tg : tgfs) {
                    if (tg.getEstado().intValue() == 0 && tg.getFaseid().getFaseorden().intValue() < x) {
                        f.setEstado(tg.getFaseid().getFasenombre());
                        x = tg.getFaseid().getFaseorden().intValue();
                    }
                }
            } else {
                f.setEstado("Finalizado");
            }
            trabs.add(f);
        }
        return trabs;
    }

    public void setTrabs(List<FormatoTablaDirector> trabs) {
        this.trabs = trabs;
    }

    public String getTitulotabla() {
        return titulotabla;
    }

    public void setTitulotabla(String titulotabla) {
        this.titulotabla = titulotabla;
    }

    public void trabajosenCurso() {
        modo = false;
        titulotablaDirector = "Trabajos de Grado en Curso";
    }

    public void trabajosTerminados() {
        modo = true;
        titulotablaDirector = "Trabajos de Grado Terminados";
    }

    public String getNombrenuevoTG() {
        return nombrenuevoTG;
    }

    public void setNombrenuevoTG(String nombrenuevoTG) {
        this.nombrenuevoTG = nombrenuevoTG;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void crearTG() {
        Trabajodegrado trab = new Trabajodegrado();
        trab.setTrabajonombre(nombrenuevoTG.trim());
        trab.setTrabajoid(BigDecimal.ZERO);
        ejbFacadetrabgrad.create(trab);

        trab = ejbFacadetrabgrad.findbyNombreTg(nombrenuevoTG.trim());

        UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);      //agregando director
        usuroltg.setRolid(new Rol(BigDecimal.ZERO));
        usuroltg.setTrabajoid(trab);
        usuroltg.setPersonacedula(new Usuario(new BigDecimal(UsuarioComun.id)));

        ejbFacade.create(usuroltg);
    }

    public boolean isModo() {
        return modo;
    }

    public void setModo(boolean modo) {
        this.modo = modo;
    }

    public List<FormatoTablaJefe> getTrabsJefe() {

        trabs1 = new ArrayList();

        List<Trabajodegrado> trabstemp;

        if (true == modo) {
            trabstemp = ejbFacadetrabgrad.getIdeasRevisadasJefe();
        } else {
            trabstemp = ejbFacadetrabgrad.getIdeasPorRevisarJefe();
        }

        int cont;
        FormatoTablaJefe f;

        for (Trabajodegrado t : trabstemp) {
            cont = 0;
            f = new FormatoTablaJefe();                  //sacamos la informacion general tanto jefe depto, director y los estud.

            List<UsuarioRolTrabajogrado> lst = ejbFacade.findbytrabajoId(t.getTrabajoid().intValue());

            if (lst.size() > 0) {
                f.setFecha(lst.get(0).getFechaasignacion());
                f.setTrabajoGradoId(lst.get(0).getTrabajoid().getTrabajoid().intValue());
                f.setTrabajoGrado(lst.get(0).getTrabajoid().getTrabajonombre());

                for (UsuarioRolTrabajogrado l : lst) {
                    if (l.getRolid().getRolid().intValue() == 0) //director
                    {
                        f.setDirector(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                        f.setDirectorId(l.getPersonacedula().getPersonacedula().intValue());
                    } else if (l.getRolid().getRolid().intValue() == 1 && cont == 0) //Estudiante 1
                    {
                        f.setEst1(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                        f.setEst1Id(l.getPersonacedula().getPersonacedula().intValue());
                        cont++;
                    } else if (l.getRolid().getRolid().intValue() == 1 && cont == 1) //estudiante 2
                    {
                        f.setEst2(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                        f.setEst2Id(l.getPersonacedula().getPersonacedula().intValue());
                    }
                }

                for (TrabajogradoFase tf : t.getTrabajogradoFaseList()) {
                    if (tf.getFaseid().getFaseid().equals(BigDecimal.ONE)) {
                        f.setAprobado(tf.getEstado().intValue());
                        break;
                    }
                }
                trabs1.add(f);
            }
        }
        return trabs1;
    }

    public void setTrabsJefe(List<FormatoTablaJefe> trabs1) {
        this.trabs1 = trabs1;
    }

    public List<FormatoTablaJefe> getHistorialRevisadas() {
        trabs2 = new ArrayList();

        List<Productodetrabajo> lst2 = ejbFacade.obtenerFormatosporId(0, 0);
        for (Productodetrabajo l : lst2) {

            List<UsuarioRolTrabajogrado> lst = ejbFacade.findbytrabajoId(l.getTrabajoid().getTrabajoid().intValue());

            FormatoTablaJefe f = new FormatoTablaJefe();
            if (lst2.size() > 0) {
                f.setFecha(lst.get(0).getFechaasignacion());
                f.setTrabajoGradoId(lst.get(0).getTrabajoid().getTrabajoid().intValue());
                f.setTrabajoGrado(lst.get(0).getTrabajoid().getTrabajonombre());
                f.setAprobado(lst2.get(0).getProductoaprobado().intValue());

                int cont = 0;
                for (UsuarioRolTrabajogrado x : lst) {

                    if (x.getRolid().getRolid().intValue() == 1) {
                        f.setDirector(x.getPersonacedula().getPersonanombres() + " " + x.getPersonacedula().getPersonaapellidos());
                    } else if (x.getRolid().getRolid().intValue() == 2 && cont == 0) {
                        f.setEst1(x.getPersonacedula().getPersonanombres() + " " + x.getPersonacedula().getPersonaapellidos());
                        f.setEst1Id(x.getPersonacedula().getPersonacedula().intValue());
                        cont++;
                    } else if (x.getRolid().getRolid().intValue() == 2) {
                        f.setEst2(x.getPersonacedula().getPersonanombres() + " " + x.getPersonacedula().getPersonaapellidos());
                        f.setEst2Id(x.getPersonacedula().getPersonacedula().intValue());
                    }
                }
            }
            trabs2.add(f);
        }

        return trabs2;
    }

    public void contenidoTgDirector(ActionEvent event) //guardar informacion del trabajo de grado que se esta tratando
    {
        //Agregamos los datos del trabajo de grado para no enviar por url.                          
        TrabajodeGradoActual.id = (Integer) event.getComponent().getAttributes().get("idtrabajo");
        TrabajodeGradoActual.nombreTg = (String) event.getComponent().getAttributes().get("nombretrab");

        //Agregamos el primer estudiante a la clase estatica 
        int idusu = (Integer) event.getComponent().getAttributes().get("est1");
        if (idusu != -1) {
            TrabajodeGradoActual.est1 = ejbFacadeusuario.buscarporUsuid(idusu).get(0);
        }

        //Agregamos el segundo estudiante si hay uno
        idusu = (Integer) event.getComponent().getAttributes().get("est2");
        if (idusu != -1) {
            TrabajodeGradoActual.est2 = ejbFacadeusuario.buscarporUsuid(idusu).get(0);
        }

        TrabajodeGradoActual.director = ejbFacadeusuario.buscarporUsuid(UsuarioComun.id).get(0);

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect("fases-trabajo-de-grado.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UsuarioRolTrabajogradoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void contenidoTgJefe(ActionEvent event) //guardar informacion del trabajo de grado que se esta tratando
    {
        //Agregamos los datos del trabajo de grado para no enviar por url.                          
        TrabajodeGradoActual.id = (Integer) event.getComponent().getAttributes().get("idtrabajo");
        TrabajodeGradoActual.nombreTg = (String) event.getComponent().getAttributes().get("nombretrab");

        //Agregamos el primer estudiante a la clase estatica 
        int idusu = (Integer) event.getComponent().getAttributes().get("est1");
        if (idusu != -1) {
            TrabajodeGradoActual.est1 = ejbFacadeusuario.buscarporUsuid(idusu).get(0);
        }

        //Agregamos el segundo estudiante si hay uno
        idusu = (Integer) event.getComponent().getAttributes().get("est2");
        if (idusu != -1) {
            TrabajodeGradoActual.est2 = ejbFacadeusuario.buscarporUsuid(idusu).get(0);
        }

        idusu = (Integer) event.getComponent().getAttributes().get("iddirector");
        if (idusu != -1) {
            TrabajodeGradoActual.director = ejbFacadeusuario.buscarporUsuid(idusu).get(0);
        }

        TrabajodeGradoActual.director = ejbFacadeusuario.buscarporUsuid(UsuarioComun.id).get(0);

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect("fases-trabajo-de-grado.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UsuarioRolTrabajogradoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public void numTrabajos() {
        if (modo == true) {
            filas = 8;
        } else {
            filas = 3;
        }
    }

    public boolean isNumMaxTrabPermitidos() {
        return numMaxTrabPermitidos;
    }

    public void setNumMaxTrabPermitidos(boolean numTrabajosPermitidos) {
        this.numMaxTrabPermitidos = numTrabajosPermitidos;
    }

    public String getTitulotablaDirector() {
        return titulotablaDirector;
    }

    public String getTitulotablaJefe() {
        return titulotablaJefe;
    }

    public List<FormatoTablaDirector> getTrabsDirector() {
        trabs = new ArrayList();

        List<Trabajodegrado> trabstemp;

        if (modo == true) {
            trabstemp = ejbFacadetrabgrad.getTrabajosTerminadosporDirectorId(UsuarioComun.id);
        } else {
            trabstemp = ejbFacadetrabgrad.getTrabajosEnCursoPorDirectorId(UsuarioComun.id);

            if (trabstemp.size() <= 3) {
                numMaxTrabPermitidos = false;
            } else {
                numMaxTrabPermitidos = true;
            }
        }

        int cont;
        FormatoTablaDirector f;

        for (Trabajodegrado t : trabstemp) {
            cont = 0;
            f = new FormatoTablaDirector();                  //sacamos la informacion general tanto director y los estud.
            f.setFecha(t.getUsuarioRolTrabajogradoList().get(0).getFechaasignacion());
            f.setTrabajoGradoId(t.getUsuarioRolTrabajogradoList().get(0).getTrabajoid().getTrabajoid().intValue());
            f.setTrabajoGrado(t.getUsuarioRolTrabajogradoList().get(0).getTrabajoid().getTrabajonombre());
            for (UsuarioRolTrabajogrado l : t.getUsuarioRolTrabajogradoList()) {
                if (l.getRolid().getRolid().intValue() == 1 && cont == 0) {
                    f.setEst1(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                    f.setEst1Id(l.getPersonacedula().getPersonacedula().intValue());
                    cont++;
                } else if (l.getRolid().getRolid().intValue() == 1 && cont == 1) {
                    f.setEst2(l.getPersonacedula().getPersonanombres() + " " + l.getPersonacedula().getPersonaapellidos());
                    f.setEst2Id(l.getPersonacedula().getPersonacedula().intValue());
                }
            }
            if (modo == false) {
                List<TrabajogradoFase> tgfs = t.getTrabajogradoFaseList();
                int x = 999;
                boolean ban = false;
                for (TrabajogradoFase tg : tgfs) {
                    if((tg.getEstado()==BigInteger.ONE) && tg.getFaseid().getFaseorden().intValue()<x) {
                    //if (tg.getFaseid().getFaseorden().intValue() < x) {
                        ban = true;
                        f.setEstado(tg.getFaseid().getFasenombre());
                        x = tg.getFaseid().getFaseorden().intValue();
                    }
                }

                if (!ban) {
                    f.setEstado("Presentación de la idea");
                }

            } else {
                f.setEstado("Finalizado");
            }
            trabs.add(f);
        }
        return trabs;
    }
}
