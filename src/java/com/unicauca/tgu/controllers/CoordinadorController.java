/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author pcblanco
 */
@ManagedBean
@ViewScoped
public class CoordinadorController {

    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    
    private List<Productodetrabajo> anteproys;
    
    public CoordinadorController() {
    }

    public List<Productodetrabajo> getAnteproys() {
        
        List<Productodetrabajo> tem = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_formatoID(2);
        
         if (tem.size() > 0) {               
             
            Map<String, String> mapaux;
            String eval1;
            Gson gson = new Gson();
             for(Productodetrabajo t : tem)
               {                   
                mapaux = gson.fromJson(t.getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());
                
                
               }  
         }
       anteproys = tem;     
             
        
        return anteproys;
    }

    public void setAnteproys(List<Productodetrabajo> anteproys) {
        this.anteproys = anteproys;
    }
    
    
    
    
}
