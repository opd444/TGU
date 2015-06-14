/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import com.unicauca.tgu.entities.Fase;
import com.unicauca.tgu.entities.FasesTrabajoDeGrado;
import com.unicauca.tgu.entities.FasesTrabajoDeGrado1;
import com.unicauca.tgu.entities.TrabajogradoFase;
import com.unicauca.tgu.jpacontroller.FaseFacade;
import com.unicauca.tgu.jpacontroller.TrabajogradoFaseFacade;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.component.button.Button;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.panelgrid.PanelGrid;

/**
 *
 * @author seven
 */
@ManagedBean
@ViewScoped
public class PanelFasesController {

    private Panel panel;
    private PanelGrid panelGrid;
    @EJB
    private FaseFacade ejbFacadeFase;
    @EJB
    private TrabajogradoFaseFacade ejbFacadeTGFase;

    private List<Fase> fases;
    
    private FasesTrabajoDeGrado1 fasesTG;

    public PanelFasesController() {
    }

    @PostConstruct
    public void init() {
        fases = ejbFacadeFase.faseOrderByOrden();

        panel = new Panel();
        panel.setHeader("Fases del Trabajo de Grado");

        panelGrid = new PanelGrid();
        int columns = fases.size();
        panelGrid.setColumns(columns);
        String panelGridStyle = "margin: 0 auto;";
        panelGrid.setStyle(panelGridStyle);
        panelGrid.setStyleClass("ui-noborder");
        // Cargando las imagenes de las fases según su orden
        String graphicImageStyle = "width: 96px;";

        for (Fase fase : fases) {
            GraphicImage graphicImage = new GraphicImage();

            graphicImage.setValue("../resources/img/" + fase.getFaseorden().toString() + ".png");
            graphicImage.setStyle(graphicImageStyle);
            panelGrid.getChildren().add(graphicImage);
        }

        panel.getChildren().add(panelGrid);
    }

    public Panel getPaneltoFase(int orden) {
        // Creando los nombres de las fases
        String buttonStyle = "width: 105px; height: 96px; font-size: 13px;";
        String buttonSelectedStyle = "width: 105px; height: 96px; font-size: 13px; background-color:greenyellow;";
        
        habilitarfases();

        for (Fase fase : fases) {
            Button button = new Button();
            button.setValue(fase.getFasenombre());
            button.setDisabled(fasesTG.getEnensimafase(fase.getFaseorden().intValue()));
            if (!(fase.getFaseorden().equals(BigInteger.valueOf(orden)))) {
                button.setStyle(buttonStyle);
            } else {
                button.setStyle(buttonSelectedStyle);
            }

            button.setOutcome("fase-" + fase.getFaseorden().toString());

            panelGrid.getChildren().add(button);
        }
        
        return panel;
    }
    
    private void habilitarfases() {
        BigDecimal trabajoid = BigDecimal.valueOf(TrabajodeGradoActual.id);
        
        List<TrabajogradoFase> _TGFases = ejbFacadeTGFase.ObtenerTrabajoFrasePor_trabajoID(trabajoid.intValue());
        
        int fase = 0;
        
        for (TrabajogradoFase _TGFase : _TGFases) {
            if (_TGFase.getTrabajoid().getTrabajoid().equals(trabajoid)) {
                if (_TGFase.getEstado().equals(BigInteger.ONE)) {
                    fase = _TGFase.getFaseid().getFaseid().intValue();
                }
            }
        }
        fasesTG = new FasesTrabajoDeGrado1(fase + 1);
    }
}
