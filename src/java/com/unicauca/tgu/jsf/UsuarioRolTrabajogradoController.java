package com.unicauca.tgu.jsf;

import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jsf.util.JsfUtil;
import com.unicauca.tgu.jsf.util.PaginationHelper;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
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
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public UsuarioRolTrabajogradoController() {
    }
    
    public void trabajoGrado() {
        String usuarionombre = "fp";
        List<UsuarioRolTrabajogrado> urtgs = getFacade().findAll();
        UsuarioRolTrabajogrado usuarioroltrabajoid;
        for(int i=0 ; i < urtgs.size(); i++) {
            if(urtgs.get(i).getPersonacedula().getUsuarionombre().equals(usuarionombre))
                    current = usuarioroltrabajoid = urtgs.get(i);
        }                
    }
    public void trabajoDeGrado(BigDecimal trabajoid) {
        List<UsuarioRolTrabajogrado> urtgs = getFacade().findAll();
        UsuarioRolTrabajogrado usuarioroltrabajoid;
        for(int i=0 ; i < urtgs.size(); i++) {
            if(urtgs.get(i).getTrabajoid().getTrabajoid().equals(trabajoid))
                    current = urtgs.get(i);
        }   
    }
    public UsuarioRolTrabajogrado getCurrent() {
        trabajoGrado();
        return current;
    }

    public void setCurrent(UsuarioRolTrabajogrado current) {
        this.current = current;
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

}
