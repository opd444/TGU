package com.unicauca.tgu.FormatosTablas;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormatoTablaActa {
    
    private int idprod;
    private String numacta;
    private String fechaact;

    public String getNumacta() {
        return numacta;
    }

    public void setNumacta(String numacta) {
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

    public FormatoTablaActa(int idprod, String numacta, String fechaact) {
        this.idprod = idprod;
        this.numacta = numacta;
        this.fechaact = fechaact;
    }
}