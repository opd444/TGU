/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.FormatosTablas;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pcblanco
 */
public class FormatoTablaActa {
    
    private int idprod;
    private int numacta;
    private String fechaact;

    public int getNumacta() {
        return numacta;
    }

    public void setNumacta(int numacta) {
        this.numacta = numacta;
    }

    public String getFechaact() {
        
        try {
            DateFormat d = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
            Date result =  d.parse(fechaact);
            
            DateFormat df = DateFormat.getDateInstance(DateFormat.FULL); 
            return df.format(result);
        } catch (ParseException ex) {
            Logger.getLogger(FormatoTablaActa.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public void setFechaact(String fechaact) {
        this.fechaact = fechaact;
    }

    public int getIdprod() {
        return idprod;
    }

    public void setIdprod(int idprod) {
        this.idprod = idprod;
    }

    public FormatoTablaActa(int idprod, int numacta, String fechaact) {
        this.idprod = idprod;
        this.numacta = numacta;
        this.fechaact = fechaact;
    }
    
    
    
    
    
    
}
