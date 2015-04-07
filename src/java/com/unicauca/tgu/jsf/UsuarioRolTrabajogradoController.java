package com.unicauca.tgu.jsf;

import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.Auxiliares.UsuarioComun;
import com.unicauca.tgu.FormatosTablas.FormatoTablaDirector;
import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jsf.util.JsfUtil;
import com.unicauca.tgu.jsf.util.PaginationHelper;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;

import java.io.Serializable;
import java.math.BigDecimal;
//<<<<<<< HEAD
//=======
import java.util.ArrayList;
import java.util.Date;
//>>>>>>> origin/master
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
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
    
    @EJB
    private com.unicauca.tgu.jpacontroller.TrabajodegradoFacade ejbFacadetrabgrad;
    
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    Usuario UsuarioLog;
    private List<FormatoTablaDirector> trabs;
    int modo;             // 0 para la seccion de trabajos en curso y 1 para trabajos terminados
    private String titulotabla;
    private String nombrenuevoTG;
    private Date fecha;
   

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
    
    @PostConstruct
    public void init(){
       modo = 0;    
       titulotabla = "Trabajos de Grado en Curso";
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
         UsuarioLog = new Usuario();
        
       List<Trabajodegrado> trabstemp;
       
       if(modo==1)trabstemp= ejbFacadetrabgrad.getTrabajosTerminadosporDirectorId(UsuarioComun.id);
       else trabstemp = ejbFacadetrabgrad.getTrabajosEnCursoPorDirectorId(UsuarioComun.id);

       int cont;     
       FormatoTablaDirector f = new FormatoTablaDirector();
       
       for(Trabajodegrado t : trabstemp)
          { 
                 cont = 0;
                 f = new FormatoTablaDirector();                  //sacamos la informacion general tanto director y los estud.
                 f.setFecha(t.getUsuarioRolTrabajogradoList().get(0).getFechaasignacion());
                 f.setTrabajoGradoId(t.getUsuarioRolTrabajogradoList().get(0).getTrabajoid().getTrabajoid().intValue());
                 f.setTrabajoGrado(t.getUsuarioRolTrabajogradoList().get(0).getTrabajoid().getTrabajonombre());
         for(UsuarioRolTrabajogrado l : t.getUsuarioRolTrabajogradoList())
         
             {            
                  if(l.getRolid().getRolid().intValue() == 1 && cont ==0)
                      { 
                       f.setEst1(l.getPersonacedula().getPersonanombres()+" "+l.getPersonacedula().getPersonaapellidos());
                       f.setEst1Id(l.getPersonacedula().getPersonacedula().intValue());
                       cont ++;
                      }
                  else if(l.getRolid().getRolid().intValue() == 1 && cont ==1)
                   { 
                       f.setEst2(l.getPersonacedula().getPersonanombres()+" "+l.getPersonacedula().getPersonaapellidos());
                       f.setEst2Id(l.getPersonacedula().getPersonacedula().intValue());                      
                   }
             }
                if(modo==0) 
                   { 
                   List<TrabajogradoFase> tgfs =  t.getTrabajogradoFaseList();
                   int x = 999;
                   for(TrabajogradoFase tg: tgfs)
                    {
                      if(tg.getEstado().intValue() == 0 && tg.getFaseid().getFaseorden().intValue()<x)
                          {
                            f.setEstado(tg.getFaseid().getFasenombre());
                            x = tg.getFaseid().getFaseorden().intValue();
                          }
                    }   
                   }
                else f.setEstado("Finalizado");
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
    
    
    
    public void trabajosenCurso()
             {
               modo = 0;
               titulotabla = "Trabajos de Grado en Curso";
             }
    public void trabajosTerminados()
             {
               modo = 1;
               titulotabla = "Trabajos de Grado Terminados";
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
    
    public void crearTG()
       {
          Trabajodegrado trab = new Trabajodegrado();
          trab.setTrabajonombre(nombrenuevoTG);
          trab.setTrabajoid(BigDecimal.ZERO);
          ejbFacadetrabgrad.create(trab);
          
          trab = ejbFacadetrabgrad.findbyNombreTg(nombrenuevoTG);
                 
          TrabajodeGradoActual.id = trab.getTrabajoid().intValue();
          TrabajodeGradoActual.nombreTg = trab.getTrabajonombre();
          
          UsuarioRolTrabajogrado usuroltg = new UsuarioRolTrabajogrado(BigDecimal.ZERO, fecha);      //agregando director
          usuroltg.setRolid(new Rol(BigDecimal.ZERO));  
          usuroltg.setTrabajoid(trab);
          usuroltg.setPersonacedula(new Usuario(new BigDecimal(UsuarioComun.id)));
          
          ejbFacade.create(usuroltg);       
    }

}
