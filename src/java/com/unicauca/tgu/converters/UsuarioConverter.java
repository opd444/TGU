/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.converters;

import com.unicauca.tgu.controllers.FormatoA;
import com.unicauca.tgu.entities.Usuario;
import java.math.BigDecimal;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author pcblanco
 */
@FacesConverter("usuarioConverter")
public class UsuarioConverter implements Converter {
 
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                FormatoA formatoA = (FormatoA) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "formatoA");
                        /*fc.getExternalContext().getApplicationMap().get("formatoA");*/
                return formatoA.getEjbFacadeUsuario().find(new BigDecimal(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
        else {
            return null;
        }
    }
 
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
      if(object != null) {
            return String.valueOf(((Usuario) object).getPersonacedula().toPlainString());
        }
        else {
            return null;
        }
    }   
}   