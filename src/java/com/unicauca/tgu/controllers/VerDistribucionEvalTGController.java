package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.FormatosTablas.TablaDistribucionEvaluacion;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.UsuarioRol;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class VerDistribucionEvalTGController
{
    @EJB
    private TrabajodegradoFacade ejbFacadeTG;
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuRolTG;
    @EJB
    private UsuarioRolFacade ejbFacadeUsuRol;
    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    
    
    private List<TablaDistribucionEvaluacion> tbl;

    public VerDistribucionEvalTGController() {
        tbl = new ArrayList();
    }

    public List<TablaDistribucionEvaluacion> getTrabajosDeGrado() {
        tbl = this.llenarTabla();
        return tbl;
    }

    public List<TablaDistribucionEvaluacion> llenarTabla()
    {
        SimpleDateFormat formateador = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        
        List<UsuarioRol> usuRol_lst = ejbFacadeUsuRol.findByRolid(BigInteger.valueOf(7));
        List<Productodetrabajo> prod_lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_formatoID(8);
        TablaDistribucionEvaluacion t;
        
        
        for (UsuarioRol ur_item : usuRol_lst)
        {
            for(Productodetrabajo pro_item : prod_lst)
            {
                
                List<UsuarioRolTrabajogrado> usuRolTG_lst = ejbFacadeUsuRolTG.findbyRolId_TrabId(ur_item.getPersonacedula().intValue(), pro_item.getTrabajoid().getTrabajoid().intValue());
                
                if(!usuRolTG_lst.isEmpty())
                {
                    try
                    {
                        t = new TablaDistribucionEvaluacion();
                        t.setJurado(usuRolTG_lst.get(0).getPersonacedula().getPersonanombres()+" "+usuRolTG_lst.get(0).getPersonacedula().getPersonaapellidos());
                        t.setTrabajoGrado(pro_item.getTrabajoid().getTrabajonombre());

                        Gson gson = new Gson();
                        Map<String, String> map = gson.fromJson(pro_item.getProductocontenido(), new TypeToken<Map<String, String>>() {}.getType());
                    
                    
                        if (map.containsKey("fecha1")) {
                            t.setFecha1(formateador.parse(map.get("fecha1")));
                        }
                        if (map.containsKey("fecha2")) {
                            t.setFecha2(formateador.parse(map.get("fecha2")));
                        }
                        if (map.containsKey("fecha3")) {
                            t.setFecha3(formateador.parse(map.get("fecha3")));
                        }

                        tbl.add(t);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        return tbl;
    }
}
