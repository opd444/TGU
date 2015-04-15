package com.unicauca.tgu.controllers;

import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.FasesTrabajoDeGrado;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.controllers.util.JsfUtil;
import com.unicauca.tgu.controllers.util.PaginationHelper;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "trabajodegradoController")
@SessionScoped
public class TrabajodegradoController implements Serializable {
    private BigDecimal trabajoid;
    private Trabajodegrado trabajo;
    private FasesTrabajoDeGrado fases;
    
    @EJB
    private ProductodetrabajoFacade ejbFacadePro;
    
    public boolean getBtnDiligenciarFormatoA() {
        List<Productodetrabajo> lst = ejbFacadePro.findAll();
        
        for(int i=0; i<lst.size(); i++) {
            if(lst.get(i).getTrabajoid().getTrabajoid().equals(trabajoid))
                return true;            
        }
        return false;
    }    
    public boolean getBtnVerFormatoA() {
        List<Productodetrabajo> lst = ejbFacadePro.findAll();
        
        for(int i=0; i<lst.size(); i++) {
            if(lst.get(i).getTrabajoid().getTrabajoid().equals(trabajoid))
                return false;            
        }
        return true;
    }

    public Trabajodegrado getTrabajo() {
        List<Trabajodegrado> tg = getFacade().findAll();
        for(int i=0; i < tg.size(); i++) {
            if(tg.get(i).getTrabajoid().equals(trabajoid)){
                int fase = -1;
                fases = new FasesTrabajoDeGrado(fase);
                TrabajodeGradoActual.id = tg.get(i).getTrabajoid().intValue();
                TrabajodeGradoActual.nombreTg = tg.get(i).getTrabajonombre();
                return tg.get(i);
            }
        }
        
        return null;
    }

    public void setTrabajo(Trabajodegrado trabajo) {
        this.trabajo = trabajo;
    }
    
    

    public BigDecimal getTrabajoid() {
        return trabajoid;
    }
    
    public void setTrabajoid(BigDecimal trabajoid) {
        this.trabajoid = trabajoid;
    }

    public FasesTrabajoDeGrado getFases() {
        return fases;
    }

    public void setFases(FasesTrabajoDeGrado fases) {
        this.fases = fases;
    }
        
    private Trabajodegrado current;
    private DataModel items = null;
    @EJB
    private com.unicauca.tgu.jpacontroller.TrabajodegradoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public TrabajodegradoController() {
    }

    public Trabajodegrado getSelected() {
        if (current == null) {
            current = new Trabajodegrado();
            selectedItemIndex = -1;
        }
        return current;
    }
    public void setSelected(Trabajodegrado selected) {
        this.current = selected;
    }

    private TrabajodegradoFacade getFacade() {
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
        current = (Trabajodegrado) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Trabajodegrado();
        selectedItemIndex = -1;
        return "Create";
    }
    protected void setEmbeddableKeys() {
    }
    private void persist(PersistAction persistAction, String successMessage) {
        if (current != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(current);
                } else {
                    getFacade().remove(current);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public String create() {
//        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CiudadCreated"));
//        if (!JsfUtil.isValidationFailed()) {
//            items = null;    // Invalidate list of items to trigger re-query.
//        }
        
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TrabajodegradoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }        
    }
    public void crearTrabajoDeGrado() {
        
        try {
            getFacade().create(current);
            getItems();
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TrabajodegradoCreated"));
            
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            
        }        
    }

    public String prepareEdit() {
        current = (Trabajodegrado) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TrabajodegradoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Trabajodegrado) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TrabajodegradoDeleted"));
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

    @FacesConverter(forClass = Trabajodegrado.class)
    public static class TrabajodegradoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TrabajodegradoController controller = (TrabajodegradoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "trabajodegradoController");
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
            if (object instanceof Trabajodegrado) {
                Trabajodegrado o = (Trabajodegrado) object;
                return getStringKey(o.getTrabajoid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Trabajodegrado.class.getName());
            }
        }

    }

}
