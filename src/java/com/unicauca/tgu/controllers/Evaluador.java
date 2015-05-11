/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class Evaluador {

    @EJB
    ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    UsuarioFacade ejbFacadeUsu;

    /**
     * Creates a new instance of CoordinadorProg
     */
    public Evaluador() {
    }

    private List<Productodetrabajo> productosDeTrabajo() {
        List<Productodetrabajo> lst = ejbFacadeProdTrab.findAll();

        List<Productodetrabajo> lstProdTrab = new ArrayList();

        for (int i = 0; i < lst.size(); i++) {
            // ToDo la logica puede cambiar
            if (lst.get(i).getFormatoid().getFormatoid().equals(BigDecimal.valueOf(2))) {
                lstProdTrab.add(lst.get(i));
            }
        }
        return lstProdTrab;
    }

    private boolean trabajoDeGradoAsignado(String usuNombre, String productoContenido) {
        Gson gson = new Gson();

        Map<String, String> map = gson.fromJson(productoContenido, new TypeToken<Map<String, String>>() {
        }.getType());

        if (map.containsKey("iddoc1")) {
            Usuario doc1 = ejbFacadeUsu.find(new BigDecimal(map.get("iddoc1")));
            if (doc1.getUsuarionombre().equals(usuNombre)) {
                return true;
            }
        }
        if (map.containsKey("iddoc2")) {
            Usuario doc2 = ejbFacadeUsu.find(new BigDecimal(map.get("iddoc2")));
            if (doc2.getUsuarionombre().equals(usuNombre)) {
                return true;
            }
        }

        return false;
    }

    public List<Trabajodegrado> trabajosDeGrado(String usuNombre) {
        List<Productodetrabajo> lst = productosDeTrabajo();

        List<Trabajodegrado> lstTrabajosDeGrado = new ArrayList();

        for (int i = 0; i < lst.size(); i++) {
            String productoContenido = lst.get(i).getProductocontenido();
            if (trabajoDeGradoAsignado(usuNombre, productoContenido)) {
                lstTrabajosDeGrado.add(lst.get(i).getTrabajoid());
            }
        }
        return lstTrabajosDeGrado;
    }

    public void detalleTrabajoDeGrado(ActionEvent event) {
        
        TrabajodeGradoActual.id = 0;
        TrabajodeGradoActual.nombreTg = "Prueba 1";
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect("fases-trabajo-de-grado.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DirectorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
