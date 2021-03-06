/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author seven
 */
@ManagedBean
@SessionScoped
public class PDFBean {

    private String pdfFileName = "Formato-A.pdf";

    private String txtTitulo; //
    private String txtNumEst; //
    private String txtDirTra; //
    private String txtObjetivos; //
    private String txtAportes; //
    private String txtTiempo; //
    private String txtCondiciones; //
    private String txtRecursos; //
    private String txtFinanciacion; //
    private String txtObservaciones; //
    private Date fechaFormato; //  
    SimpleDateFormat formateador;
    
    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    
    public PDFBean() {
        fechaFormato = new Date();
        formateador = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy");
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public void obtenerDatosFormatoA() {
        
        List<Productodetrabajo> lst = ejbFacadeProdTrab.ObtenerProdsTrabajoPor_trabajoID_formatoID(TrabajodeGradoActual.id, 0);
        
        if (lst.size() > 0) { //verificar si ya hay guardardo un formato A para este trabajo de grado

            Productodetrabajo formatoactual = lst.get(0);
            String contenido = formatoactual.getProductocontenido();
            int numEst = 0;

            Gson gson = new Gson();

            Map<String, String> decoded = gson.fromJson(contenido,
                    new TypeToken<Map<String, String>>() {
                    }.getType());

            if (decoded.get("nombre") != null) {
                txtTitulo = decoded.get("nombre");
            }
//        //decoded.get("idestud1");
            if (decoded.get("idestud1") != null) {
//                int x = Integer.parseInt(decoded.get("idestud1"));
//                com.unicauca.tgu.entities.Usuario est1 = ejbFacadeUsuario.find(BigDecimal.valueOf(x));
                numEst += 1;
            }
//        //decoded.get("idestud2");
            if (decoded.get("idestud2") != null) {
//                int x = Integer.parseInt(decoded.get("idestud2"));
//                com.unicauca.tgu.entities.Usuario est2 = ejbFacadeUsuario.find(BigDecimal.valueOf(x));
                numEst += 1;
            }
            txtNumEst = String.valueOf(numEst);
//        //decoded.get("iddirector");
            if (decoded.get("nombredirector") != null) {
                txtDirTra = decoded.get("nombredirector");
            }
            if (decoded.get("objetivos") != null) {
                txtObjetivos = decoded.get("objetivos");
            }
            if (decoded.get("aportes") != null) {
                txtAportes = decoded.get("aportes");
            }
            if (decoded.get("tiempo") != null) {
                txtTiempo = decoded.get("tiempo");
            }
            if (decoded.get("condiciones") != null) {
                txtCondiciones = decoded.get("condiciones");
            }            
            if (decoded.get("recursos") != null) {
                txtRecursos = decoded.get("recursos");
            }
            if (decoded.get("financiacion") != null) {
                txtFinanciacion = decoded.get("financiacion");
            }
            if (decoded.get("observaciones") != null) {
                txtObservaciones = decoded.get("observaciones");
            }
            if (decoded.get("fecha") != null) {
                String fechaAux = decoded.get("fecha");
                try {
                    fechaFormato = formateador.parse(fechaAux);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void generarPDF() {
                obtenerDatosFormatoA();
        try {
            File file = File.createTempFile("Formato-A", ".pdf");

            pdfFileName = file.getName();

            Document document = new Document();
            document.setPageSize(PageSize.LETTER);
            document.setMargins((float) 30, (float) 30, (float) 25, (float) 25);
            document.addTitle("Formato A");
            document.addAuthor("Universidad del Cauca");

            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            /**/
            Font bold = new Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD);

            URL url = FacesContext.getCurrentInstance().getExternalContext().getResource("/resources/img/escudo-unicauca.jpg");
            Image imgLogoUnicauca = Image.getInstance(url);
            imgLogoUnicauca.scaleAbsolute(75f, 100f);

            /**/
            Font fontUC = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph pphUC = new Paragraph("Universidad del Cauca", fontUC);
            pphUC.setAlignment(Element.ALIGN_CENTER);
//            document.add(pphUC);
            Font fontFIET = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Paragraph pphFIET = new Paragraph("Facultad de Ingeniería Electrónica y Telecomunicaciones", fontFIET);
            pphFIET.setAlignment(Element.ALIGN_CENTER);
//            document.add(pphFIET);            
            Paragraph pphCIFIET = new Paragraph("CI-FIET", fontFIET);
            pphCIFIET.setAlignment(Element.ALIGN_CENTER);
            /* -------------------------------------------------------------- */
            PdfPTable tableEncabezado = new PdfPTable(2);

            tableEncabezado.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tableEncabezado.setWidthPercentage(100);
            tableEncabezado.setSpacingAfter(5);

            PdfPCell cell1 = new PdfPCell(imgLogoUnicauca);
            cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setVerticalAlignment(Element.ALIGN_CENTER);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell2 = new PdfPCell(new Paragraph());
            cell2.addElement(pphUC);
            cell2.addElement(pphFIET);
            cell2.addElement(pphCIFIET);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setVerticalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

            tableEncabezado.addCell(cell1);
            tableEncabezado.addCell(cell2);

            document.add(tableEncabezado);

            /* -------------------------------------------------------------- */
            Font fontTG = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Paragraph pphTG = new Paragraph("TRABAJOS DE GRADO", fontTG);
            pphTG.setAlignment(Element.ALIGN_CENTER);
            document.add(pphTG);
            document.add(new Paragraph("\n"));
            /**/
            Phrase formatoA = new Phrase("FORMATO A:", fontTG);
            Font font = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
            Phrase phraseFormatoA = new Phrase("    PRESENTACIÓN DE LA PROPUESTA DE TRABAJO DE GRADO AL DEPARTAMENTO", font);
            Paragraph pphformatoA = new Paragraph();
            pphformatoA.add(formatoA);
            pphformatoA.add(phraseFormatoA);
            document.add(pphformatoA);
            document.add(new Paragraph(""));
            document.add(new Paragraph("\n"));
            
            Paragraph pphTitulo = new Paragraph("TITULO:", font);
            Chunk titulo = new Chunk(txtTitulo, font);
            titulo.setUnderline(0.2f, -2f);
            pphTitulo.add(" ");
            pphTitulo.add(titulo);
            document.add(pphTitulo);
            /**/
            Paragraph pphNumEst = new Paragraph("NUMERO DE ESTUDIANTES:", font);
            Chunk numEst = new Chunk(txtNumEst, font);
            numEst.setUnderline(0.2f, -2f);
            pphNumEst.add(" ");
            pphNumEst.add(numEst);
            document.add(pphNumEst);
            /**/
            Paragraph pphDirTra = new Paragraph("DIRECTOR DEL TRABAJO:", font);
            Chunk dirTra = new Chunk(txtDirTra, font);
            dirTra.setUnderline(0.2f, -2f);
            pphDirTra.add(" ");
            pphDirTra.add(dirTra);
            document.add(pphDirTra);
            /* do it*/
            document.add(new Paragraph("OBJETIVOS:", font));

            Paragraph pphObjetivos = new Paragraph();
            pphObjetivos.setFont(font);
            Chunk objetivos = new Chunk(txtObjetivos, font);
            objetivos.setUnderline(0.2f, -2f);
            pphObjetivos.add(" ");
            pphObjetivos.add(objetivos);
            document.add(pphObjetivos);
            /* do it */
            document.add(new Paragraph("APORTES O CONTRIBUCION A LAS LINEAS DE INVESTIGACIÓN Y DESARROLLO O TEMAS DE INTERES DEL DEPARTAMENTO:", font));

            Paragraph pphAportes = new Paragraph();
            pphAportes.setFont(font);
            Chunk aportes = new Chunk(txtAportes, font);
            aportes.setUnderline(0.2f, -2f);
            pphAportes.add(" ");
            pphAportes.add(aportes);
            document.add(pphAportes);
            /**/
            Paragraph pphTiempo = new Paragraph("TIEMPO ESTIMADO DE REALIZACIÓN:", font);
            Chunk tiempo = new Chunk(txtTiempo, font);
            tiempo.setUnderline(0.2f, -2f);
            pphTiempo.add(" ");
            pphTiempo.add(tiempo);
            document.add(pphTiempo);
            /* do it */
            document.add(new Paragraph("CONDICIONES DE ENTREGA:", font));

            Paragraph pphCondiciones = new Paragraph();
            pphCondiciones.setFont(font);
            Chunk condiciones = new Chunk(txtCondiciones, font);
            condiciones.setUnderline(0.2f, -2f);
            pphCondiciones.add(" ");
            pphCondiciones.add(condiciones);
            document.add(pphCondiciones);
            /* do it */
            document.add(new Paragraph("RECURSOS REQUERIDOS:", font));

            Paragraph pphRecursos = new Paragraph();
            pphRecursos.setFont(font);
            Chunk recursos = new Chunk(txtRecursos, font);
            recursos.setUnderline(0.2f, -2f);
            pphRecursos.add(" ");
            pphRecursos.add(recursos);
            document.add(pphRecursos);
            /* do it*/
            document.add(new Paragraph("DEFINICION DE FUENTES DE FINANCIACION:", font));

            Paragraph pphFinanciacion = new Paragraph();
            pphFinanciacion.setFont(font);
            Chunk financiacion = new Chunk(txtFinanciacion, font);
            financiacion.setUnderline(0.2f, -2f);
            pphFinanciacion.add(" ");
            pphFinanciacion.add(financiacion);
            document.add(pphFinanciacion);
            /* do it */
            document.add(new Paragraph("OBSERVACIONES:", font));

            Paragraph pphObservaciones = new Paragraph();
            pphObservaciones.setFont(font);
            Chunk observaciones = new Chunk(txtObservaciones, font);
            observaciones.setUnderline(0.2f, -2f);
            pphObservaciones.add(" ");
            pphObservaciones.add(observaciones);
            document.add(pphObservaciones);
            /**/
            Paragraph pphFecha = new Paragraph("FECHA:", font);
            Chunk fecha = new Chunk(formateador.format(fechaFormato), font);
            fecha.setUnderline(0.2f, -2f);
            pphFecha.add(" ");
            pphFecha.add(fecha);
            document.add(pphFecha);
            /**/
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("FIRMA:  ________________________", font));
            document.add(new Paragraph("        (Proponente(s) del trabajo de grado)", font));

            /**/
            document.close();
            RequestContext.getCurrentInstance().update("frmDescargarFormatoA");
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(PDFBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
