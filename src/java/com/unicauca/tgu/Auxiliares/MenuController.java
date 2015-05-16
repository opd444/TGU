/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import com.unicauca.tgu.entities.Rol;
import com.unicauca.tgu.entities.UsuarioRol;
import com.unicauca.tgu.jpacontroller.RolFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolFacade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class MenuController implements Serializable {

    @EJB
    UsuarioFacade ejbFacadeUsu;
    @EJB
    RolFacade ejbFacadeRol;
    @EJB
    UsuarioRolFacade ejbFacadeUsuRol;

    private MenuModel model;
    private DefaultMenuItem selectedItem;

    public MenuController() {
    }

    @PostConstruct
    protected void initialize() {
        model = new DefaultMenuModel();

        com.unicauca.tgu.entities.Usuario usu = ejbFacadeUsu.buscarPorUsuarionombre("aescobar");
        List<Rol> roles = new ArrayList();

        List<UsuarioRol> lstUsuRol = ejbFacadeUsuRol.findAll();

        BigInteger personacedula = usu.getPersonacedula().toBigInteger();

        for (UsuarioRol usurolItem : lstUsuRol) {

            if (usurolItem.getPersonacedula().equals(personacedula)) {
                
                int rolid = usurolItem.getRolid().intValue();
                Rol rol = buscarPorRolId(BigDecimal.valueOf(rolid));
//                System.out.println(rol.getRolnombre());
                roles.add(rol);
            }
        }        

        DefaultSubMenu perfiles = new DefaultSubMenu("Perfiles");

        for (Rol rolItem : roles) {
            DefaultMenuItem item = new DefaultMenuItem(rolItem.getRolnombre());
            perfiles.addElement(item);
        }

        model.addElement(perfiles);
    }

    private Rol buscarPorRolId(BigDecimal rolid) {
        List<Rol> roles = ejbFacadeRol.findAll();

        for (Rol rolItem : roles) {
            if (rolItem.getRolid().equals(rolid)) {
                return rolItem;
            }
        }
        return null;
    }

    public MenuModel getModel() {
        return model;
    }

    public DefaultMenuItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(DefaultMenuItem selectedItem) {
        this.selectedItem = selectedItem;
    }    
}
