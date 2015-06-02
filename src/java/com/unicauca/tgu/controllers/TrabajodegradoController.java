package com.unicauca.tgu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicauca.tgu.Auxiliares.TrabajodeGradoActual;
import com.unicauca.tgu.entities.FasesTrabajoDeGrado;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.entities.Trabajodegrado;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.TrabajodegradoFacade;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.entities.Usuario;
import com.unicauca.tgu.entities.UsuarioRolTrabajogrado;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import com.unicauca.tgu.jpacontroller.UsuarioRolTrabajogradoFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "trabajodegradoController")
@ViewScoped
public class TrabajodegradoController implements Serializable {

    private BigDecimal trabajoid;
    private Trabajodegrado trabajo;
    private FasesTrabajoDeGrado fases;

    private String usunomEval;
    @EJB
    private UsuarioFacade ejbFacadeUsu;
    @EJB
    private ProductodetrabajoFacade ejbFacadePro;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTrabFase;
    @EJB
    private UsuarioRolTrabajogradoFacade ejbFacadeUsuRolTrab;

    @EJB
    private com.unicauca.tgu.jpacontroller.TrabajodegradoFacade ejbFacade;

    @PostConstruct
    public void init() {
        trabajoid = BigDecimal.valueOf(TrabajodeGradoActual.id);      //se Recupera el Id del trabajo actual
        habilitarfases();
    }

    //Formato A
    public boolean getBtnDiligenciarFormatoA() {
        return verificarProductodeTrabajo(trabajoid.intValue(), 0);
    }

    public boolean getBtnDiligenciarRevisionFormatoA() {
        return verificarProductodeTrabajo(trabajoid.intValue(), 1);
    }

    public boolean getBtnVerFormatoA() {
        return !verificarProductodeTrabajo(trabajoid.intValue(), 0);
    }

    public boolean getBtnEditarFormatoA() {

        List<Productodetrabajo> lst = ejbFacadePro.findAll();
        List<Productodetrabajo> lstProd = obtenerProductodeTrabajoporFormato(trabajoid.intValue(), 0);
        List<Productodetrabajo> lstProd2 = obtenerProductodeTrabajoporFormato(trabajoid.intValue(), 1);

        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).getTrabajoid().getTrabajoid().equals(trabajoid)) {
                if (lstProd.size() > 0) {
                    if (lstProd2.size() > 0) {
                        return getBtnEditarRevisionFormatoA();
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }
        return true;
    }
    
     
    public boolean getBtnVerRevisionFormatoA() {
        return !verificarProductodeTrabajo(trabajoid.intValue(), 1);
    }

    public List<Productodetrabajo> obtenerProductodeTrabajoporFormato(int idtrabajo, int idformato) {
        return ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(idtrabajo, idformato);
    }

    public boolean verificarProductodeTrabajo(int idtrabajo, int idformato) {
        List<Productodetrabajo> Lst = obtenerProductodeTrabajoporFormato(idtrabajo, idformato);
        return Lst.size() > 0;
    }

    public boolean getBtnEditarRevisionFormatoA() {
        List<Productodetrabajo> lstProd = obtenerProductodeTrabajoporFormato(trabajoid.intValue(), 1);

        if (lstProd.size() > 0) {
            if (lstProd.get(0).getProductoaprobado().equals(BigInteger.ONE)) //Si ha sido aprobado el prod del formato 1
            {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
    
    public boolean getBtnDiligenciarFormatoF() {
        return verificarProductodeTrabajo(trabajoid.intValue(), 6);
     }
    
    public boolean getBtnEditarFormatoF() {
        return !getBtnDiligenciarFormatoF();
     }
    
    public boolean getBtnVerFormatoF() {
        return getBtnEditarFormatoF();
     }
    
    public boolean getBtnDescargarFormatoF() {
        return getBtnEditarFormatoF();
     }

    public boolean getBtnDiligenciarAnteproyecto() {
        List<Productodetrabajo> lstProd = obtenerProductodeTrabajoporFormato(TrabajodeGradoActual.id, 2);
        if (lstProd.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getBtnEditarAnteproyecto() {
        List<Productodetrabajo> lstProd = obtenerProductodeTrabajoporFormato(TrabajodeGradoActual.id, 2);
        if (lstProd.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getBtnVerAnteproyecto() {
        List<Productodetrabajo> lstProd = obtenerProductodeTrabajoporFormato(TrabajodeGradoActual.id, 2);
        if (lstProd.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getBtnVerAsignacionEval() {
        List<Productodetrabajo> lstProd1 = ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(trabajoid.intValue(), 4);
        if (lstProd1.size() > 0) {
            return true;
        } else {
            List<Productodetrabajo> lstProd2 = ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(trabajoid.intValue(), 3);
            if (lstProd2.size() == 2) {
                return true;
            } else {
                List<Productodetrabajo> lstProd3 = obtenerProductodeTrabajoporFormato(TrabajodeGradoActual.id, 2);
                if (lstProd3.size() > 0) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    public String getTituloBtnAsignacionEval() {
        List<Productodetrabajo> lst = ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 2);

        if (lst.size() > 0) {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(lst.get(0).getProductocontenido(), new TypeToken<Map<String, String>>() {
            }.getType());

            if (map.containsKey("iddoc1") || map.containsKey("iddoc2")) {
                return "Editar Asignación de Evaluadores";
            }
        }
        return "Asignación de Evaluadores";
    }

    public void incializar() {
        habilitarfases();
    }

    public Trabajodegrado getTrabajo() {
        return new Trabajodegrado(trabajoid, TrabajodeGradoActual.nombreTg);
    }

    public void setTrabajo(Trabajodegrado trabajo) {

        this.trabajo = trabajo;
    }

    public BigDecimal getTrabajoid() {
        return trabajoid;
    }

    public void setTrabajoid(BigDecimal trabajoid) {
        this.trabajoid = trabajoid;
    }

    public void trabajoAsignado(BigDecimal trabajoid) {
        this.trabajoid = trabajoid;
    }

    public FasesTrabajoDeGrado getFases() {
        return fases;
    }

    public void setFases(FasesTrabajoDeGrado fases) {
        this.fases = fases;
    }

    public TrabajodegradoController() {
    }

    private TrabajodegradoFacade getFacade() {
        return ejbFacade;
    }

    private void habilitarfases() {
        int fase = 0;
        List<TrabajogradoFase> traFase = ejbFacadeTrabFase.ObtenerTrabajoFrasePor_trabajoID(trabajoid.intValue());
        for (TrabajogradoFase traFase1 : traFase) {
            if (traFase1.getTrabajoid().getTrabajoid().equals(trabajoid)) {
                if (traFase1.getEstado().equals(BigInteger.ONE)) {
                    fase = traFase1.getFaseid().getFaseid().intValue();
                }
            }
        }
        fases = new FasesTrabajoDeGrado(fase + 1);
    }

    // Formato B
    public boolean getBtnDiligenciarFormatoB() {
        List<Productodetrabajo> lstProdTrab = ejbFacadePro.findAll();

        for (int i = 0; i < lstProdTrab.size(); i++) {
            if (lstProdTrab.get(i).getFormatoid().getFormatoid().equals(BigDecimal.valueOf(3))
                    && lstProdTrab.get(i).getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {

                Productodetrabajo prodtrab = lstProdTrab.get(i);
                Gson gson = new Gson();
                Map<String, String> decoded = gson.fromJson(prodtrab.getProductocontenido(), new TypeToken<Map<String, String>>() {
                }.getType());

                Usuario usu = ejbFacadeUsu.buscarPorUsuarionombre(usunomEval);
                String nombres = usu.getPersonanombres();
                String apellidos = usu.getPersonaapellidos();
                String evaluador = nombres + " " + apellidos;

                if (decoded.get("evaluador") != null && decoded.get("evaluador").equals(evaluador)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean getBtnEditarFormatoB() {
        return !getBtnDiligenciarFormatoB();
    }

    public boolean getBtnVerFormatoB() {
        return !getBtnDiligenciarFormatoB();
    }

    public boolean getBtnDescargarFormatoB() {
        return !getBtnDiligenciarFormatoB();
    }

    public String getUsunomEval() {
        return usunomEval;
    }

    public void setUsunomEval(String usunomEval) {
        this.usunomEval = usunomEval;
    }

    // Formato B
    public int numFormatosB() {
        List<UsuarioRolTrabajogrado> lst = ejbFacadeUsuRolTrab.findAll();

        int num = 0;

        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).getRolid().getRolid().equals(BigDecimal.valueOf(4))
                    && lst.get(i).getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                num += 1;
            }
        }
        return num;
    }

    public boolean btnVerFormatoB1() {
        if (numFormatosB() >= 1) {

            return false;
        }
        return true;
    }

    public boolean btnVerFormatoB2() {
        if (numFormatosB() == 2) {
            return false;
        }
        return true;
    }

    // Formato: Carta Remision de Anteproyecto
    public boolean getBtnDiligenciarRemisionAnteproyecto() {
        if (getBtnVerRemisionAnteproyecto()) {
            List<Productodetrabajo> lstProductos = ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(trabajoid.intValue(), 3);

            if (lstProductos.size() == 2) //Deben haber 2 productos: El 1er Formato B del evaluador 1 y el 2 formato B del evaluador 2
            {
                for (Productodetrabajo p : lstProductos) {
                    Gson gson = new Gson();
                    Map<String, String> decoded = gson.fromJson(p.getProductocontenido(), new TypeToken<Map<String, String>>() {
                    }.getType());
                    int aprobacionGeneral = 0;
                    if (decoded.get("elementoConsideradoH") != null) {
                        aprobacionGeneral = Integer.parseInt(decoded.get("elementoConsideradoH"));
                    }
                    if (aprobacionGeneral == 0) //Si dicho formato B no ha sido aprobado
                    {
                        return true;    //El boton se oculta
                    }
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    
    public boolean getBtnEditarRemisionAnteproyecto()
    {
        List<Productodetrabajo> lstProductos = ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(trabajoid.intValue(), 4);
        if (lstProductos.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getBtnVerRemisionAnteproyecto() {
        List<Productodetrabajo> lstProductos = ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(trabajoid.intValue(), 4);
        if (lstProductos.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    //Formato 5: Acta de resolución de aprobación
    public boolean getBtnVerAsignarActaResolucion() {
        List<Productodetrabajo> lstProductos = ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(trabajoid.intValue(), 4);
        if (lstProductos.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Formato G TODO: falta revisar las precondiciones para diligencar el
     * Formato G si estas existen.
     */
    public boolean getBtnDiligenciarFormatoG() {
        List<Productodetrabajo> lstProdTrab = ejbFacadePro.findAll();

        for (int i = 0; i < lstProdTrab.size(); i++) {
            if (lstProdTrab.get(i).getFormatoid().getFormatoid().equals(BigDecimal.valueOf(7))
                    && lstProdTrab.get(i).getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {

                return true;
            }
        }
        return false;
    }

    public boolean getBtnEditarFormatoG() {
        return !getBtnDiligenciarFormatoG();
    }

    public boolean getBtnVerFormatoG() {
        return !getBtnDiligenciarFormatoG();
    }

    public boolean getBtnDescargarFormatoG() {
        return !getBtnDiligenciarFormatoG();
    }
    
    /**
     * FORMATO: Oficio de Asignación de Jurados, realizado por la Secretaria.
     * @return Boolean
     */
    
    public boolean getBtnDiligenciarOficioAsigJurados()
    {
        if(getBtnEditarOficioAsigJurados() == false)
        {
            List<Productodetrabajo> lstProductos = ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(trabajoid.intValue(), 7);
            if (lstProductos.size() > 0) {
                return true;
            } else {
                return false;
            }
        }
        else
            return false;
    }

    public boolean getBtnEditarOficioAsigJurados() {
        List<Productodetrabajo> lstProductos = ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(trabajoid.intValue(), 8);
        if (lstProductos.size() > 0)
            return true;
        else
            return false;
    }

    public boolean getBtnVerOficioAsigJurados() {
        List<Productodetrabajo> lstProductos = ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(trabajoid.intValue(), 8);
        if (lstProductos.size() > 0)
            return true;
        else
            return false;
    }
    
    public boolean getBtnDescargarOficioAsigJurados() {
        List<Productodetrabajo> lstProductos = ejbFacadePro.ObtenerProdsTrabajoPor_trabajoID_formatoID(trabajoid.intValue(), 8);
        if (lstProductos.size() > 0)
            return true;
        else
            return false;
    }
}
