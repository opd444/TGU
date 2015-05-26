package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;


@ManagedBean
@SessionScoped
public class TrabajogradoFaseController {
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTraFase;
    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    
    public TrabajogradoFaseController() {
        
    }
    public void btnAvalar(TrabajodegradoController mgb) { // TODO Refactorizar con cosulta directa en el facade
        List<TrabajogradoFase> lst = ejbFacadeTraFase.findAll();
        
        for(int i = 0; i < lst.size(); i++) {
            if(lst.get(i).getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                if(lst.get(i).getFaseid().getFaseid().equals(BigDecimal.valueOf(2))) {
                    lst.get(i).setEstado(BigInteger.ONE);
                    ejbFacadeTraFase.edit(lst.get(i));
                    
                }
                if(lst.get(i).getFaseid().getFaseid().equals(BigDecimal.valueOf(3))) {
                    lst.get(i).setEstado(BigInteger.ZERO);
                    ejbFacadeTraFase.edit(lst.get(i));
                    
                }
            }
        }
        
        List<Productodetrabajo> lstProd = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 2);
        
        if (lstProd.size() > 0) {
            
            Productodetrabajo productoActual = lstProd.get(0);
            
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(productoActual.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());
            
            if (map.get("avalado") != null) {
                map.remove("avalado");
                map.put("avalado", "1");    //Se actualiza el campo "avalado" en aprobado.
                String contenido = gson.toJson(map, Map.class);
                productoActual.setProductocontenido(contenido);
                ejbFacadeProdTrab.edit(productoActual);
            }
        }
        
        mgb.incializar();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext requestContext =RequestContext.getCurrentInstance();
        context.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado", "Anteproyecto avalado con éxito."));
        requestContext.update("formularioavalar");
        
    }
}
