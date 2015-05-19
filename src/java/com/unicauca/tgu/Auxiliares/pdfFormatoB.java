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
import com.unicauca.tgu.controllers.FormatoB;
import com.unicauca.tgu.entities.Productodetrabajo;
import com.unicauca.tgu.jpacontroller.ProductodetrabajoFacade;
import com.unicauca.tgu.jpacontroller.UsuarioFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
public class pdfFormatoB {

    private String txttitulo;
    private String txtestudiante1;
    private String txtestudiante2;
    private String txtdirector;
    private int txtelementoConsideradoA;
    private int txtelementoConsideradoB;
    private int txtelementoConsideradoC;
    private int txtelementoConsideradoD;
    private int txtelementoConsideradoE;
    private int txtelementoConsideradoF;
    private int txtelementoConsideradoG;
    private int txtelementoConsideradoH;
    private String txtobservaciones;
    private Date txtfecha;
    private String txtevaluador;

    private String pdfFileName = "Formato-B.pdf";
    @EJB
    private ProductodetrabajoFacade ejbFacadeProdTrab;
    @EJB
    private UsuarioFacade ejbFacadeUsu;

    public pdfFormatoB() {
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    /**
     * pdfFormatoBEvaluador: genera el pdf del Formato B para el evaluador que
     * que tiene la sesión iniciada actualmente.
     *
     * @param evaluador
     */
    public void pdfFormatoBEvaluador(String evaluador) {
        com.unicauca.tgu.entities.Usuario usuEvaluador = ejbFacadeUsu.buscarPorUsuarionombre(evaluador);
        String nombres = usuEvaluador.getPersonanombres();
        String apellidos = usuEvaluador.getPersonaapellidos();
        this.txtevaluador = nombres + " " + apellidos;

        prepararContenidoFormatoB();

        generarPDF();
    }
    public void pdfFormatoBEvaluadorX(int x) {
        if(x == 1) {
            pdfFormatoBEvaluador1();
        }
        if(x == 2) {
            pdfFormatoBEvaluador2();
        }
    }
    /**
     * pdfFormatoBEvaluador: genera el pdf del Formato B para el evaluador que
     * primero diligencio el formato B
     */
    public void pdfFormatoBEvaluador1() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.findAll();

        for (Productodetrabajo ProdTrab : lstProdTrab) {
            // Buscando el Formato B
            if (ProdTrab.getFormatoid().getFormatoid().equals(BigDecimal.valueOf(3)) && ProdTrab.getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {

                Productodetrabajo prodtrab = ProdTrab;

                Gson gson = new Gson();
                Map<String, String> decoded = gson.fromJson(prodtrab.getProductocontenido(), new TypeToken<Map<String, String>>() {
                }.getType());

                if (decoded.get("evaluador") != null) {
                    txtevaluador = decoded.get("evaluador");
                    prepararContenidoFormatoB();
                    generarPDF();
                    return;
                }
            }
        }
    }

    /**
     * pdfFormatoBEvaluador: genera el pdf del Formato B para el evaluador que
     * primero diligencio el formato B
     */
    public void pdfFormatoBEvaluador2() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.findAll();

        int evaluador2 = 0;

        for (Productodetrabajo ProdTrab : lstProdTrab) {
            // Buscando el Formato B
            if (ProdTrab.getFormatoid().getFormatoid().equals(BigDecimal.valueOf(3)) && ProdTrab.getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {

                Productodetrabajo prodtrab = ProdTrab;

                Gson gson = new Gson();
                Map<String, String> decoded = gson.fromJson(prodtrab.getProductocontenido(), new TypeToken<Map<String, String>>() {
                }.getType());

                if (decoded.get("evaluador") != null) {
                    txtevaluador = decoded.get("evaluador");
                    evaluador2 += 1;
                    if (evaluador2 == 2) {
                        prepararContenidoFormatoB();
                        generarPDF();
                        return;
                    }
                }
            }
        }
    }

    public void generarPDF() {

        try {
            File file = File.createTempFile("Formato-B", ".pdf");

            pdfFileName = file.getName();

            Document document = new Document();
            document.setPageSize(PageSize.LETTER);
            document.setMargins((float) 30, (float) 30, (float) 25, (float) 25);
            document.addTitle("Formato B");
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
            Phrase formatoB = new Phrase("FORMATO B:", fontTG);
            Font font = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
            Phrase phraseFormatoB = new Phrase("    EVALUACIÓN DEL ANTEPROYECTO", font);
            Paragraph pphformatoB = new Paragraph();
            pphformatoB.add(formatoB);
            pphformatoB.add(phraseFormatoB);
            document.add(pphformatoB);
            document.add(new Paragraph(""));
            document.add(new Paragraph("\n"));

            Paragraph pphTitulo = new Paragraph();
            Chunk tituloTitulo = new Chunk("TITULO:", fontTG);
            pphTitulo.add(tituloTitulo);

            Chunk titulo = new Chunk(txttitulo, font);
            titulo.setUnderline(0.2f, -2f);
            pphTitulo.add(" ");
            pphTitulo.add(titulo);
            document.add(pphTitulo);
            /**/
            Paragraph pphEstudiantes = new Paragraph();
            Chunk estudiantesTitulo = new Chunk("ESTUDIANTES:", fontTG);
            pphEstudiantes.add(estudiantesTitulo);
            Chunk estudiantes = new Chunk(" ", font);
            if(txtestudiante1 != null) {
                estudiantes = new Chunk(txtestudiante1, font);
            }
            if(txtestudiante1 != null && txtestudiante2 != null) {
                estudiantes = new Chunk(txtestudiante1 + " y " + txtestudiante2, font);
            }            
            estudiantes.setUnderline(0.2f, -2f);
            pphEstudiantes.add(" ");
            pphEstudiantes.add(estudiantes);
            document.add(pphEstudiantes);
            /**/
            Paragraph pphDirTra = new Paragraph();
            Chunk directorTitulo = new Chunk("DIRECTOR:", fontTG);
            pphDirTra.add(directorTitulo);

            Chunk dirTra = new Chunk(txtdirector, font);
            dirTra.setUnderline(0.2f, -2f);
            pphDirTra.add(" ");
            pphDirTra.add(dirTra);
            document.add(pphDirTra);
            /**/
            document.add(new Paragraph(""));
            document.add(new Paragraph("\n"));
            Paragraph pphElementos = new Paragraph("ELEMENTOS CONSIDERADOS:", fontTG);
            document.add(pphElementos);
            document.add(new Paragraph(""));
            document.add(new Paragraph("\n"));
            /* elementos considerados */
            /* elemento considerado a */
            Paragraph pphElementoA = new Paragraph("A) PRESENTACIÓN Y APORTES (¿Están indicados con claridad?):", font);
            Chunk elementoA = new Chunk(elementocosiderado(txtelementoConsideradoA), font);
            elementoA.setUnderline(0.2f, -2f);
            pphElementoA.add(" ");
            pphElementoA.add(elementoA);
            document.add(pphElementoA);
            /* elemento considerado b */
            Paragraph pphElementoB = new Paragraph("B) OBJETIVOS (¿Establecidos claramente?):", font);
            Chunk elementoB = new Chunk(elementocosiderado(txtelementoConsideradoB), font);
            elementoB.setUnderline(0.2f, -2f);
            pphElementoB.add(" ");
            pphElementoB.add(elementoB);
            document.add(pphElementoB);
            /* elemento considerado c */
            Paragraph pphElementoC = new Paragraph("C) METODOLOGÍA (¿Se indica explícitamente?):", font);
            Chunk elementoC = new Chunk(elementocosiderado(txtelementoConsideradoC), font);
            elementoC.setUnderline(0.2f, -2f);
            pphElementoC.add(" ");
            pphElementoC.add(elementoC);
            document.add(pphElementoC);
            /* elemento considerado d */
            Paragraph pphElementoD = new Paragraph("D) CONDICIONES DE ENTREGA (¿Se establecen claramente?):", font);
            Chunk elementoD = new Chunk(elementocosiderado(txtelementoConsideradoD), font);
            elementoD.setUnderline(0.2f, -2f);
            pphElementoD.add(" ");
            pphElementoD.add(elementoD);
            document.add(pphElementoD);
            /* elemento considerado e */
            Paragraph pphElementoE = new Paragraph("E) MONOGRAFÍA (¿Tiene una estructura adecuada?):", font);
            Chunk elementoE = new Chunk(elementocosiderado(txtelementoConsideradoE), font);
            elementoE.setUnderline(0.2f, -2f);
            pphElementoE.add(" ");
            pphElementoE.add(elementoE);
            document.add(pphElementoE);
            /* elemento considerado f */
            Paragraph pphElementoF = new Paragraph("F)CRONOGRAMA (¿Se indica de forma clara y completa?):", font);
            Chunk elementoF = new Chunk(elementocosiderado(txtelementoConsideradoF), font);
            elementoF.setUnderline(0.2f, -2f);
            pphElementoF.add(" ");
            pphElementoF.add(elementoF);
            document.add(pphElementoF);
            /* elemento considerado g */
            Paragraph pphElementoG = new Paragraph("G) PATROCINIO (¿Existe?):", font);
            Chunk elementoG = new Chunk(elementocosiderado(txtelementoConsideradoG), font);
            elementoG.setUnderline(0.2f, -2f);
            pphElementoG.add(" ");
            pphElementoG.add(elementoG);
            document.add(pphElementoG);
            /* elemento considerado h */
            Paragraph pphElementoH = new Paragraph("H) CONCEPTO GENERAL", font);
            Chunk elementoH = new Chunk(getAprobado(), font);
            pphElementoH.add(" ");
            pphElementoH.add(elementoH);
            document.add(pphElementoH);
            /* do it */
            document.add(new Paragraph(""));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("OBSERVACIONES:", fontTG));

            Paragraph pphObservaciones = new Paragraph();
            pphObservaciones.setFont(font);
            Chunk observaciones = new Chunk(txtobservaciones, font);
            observaciones.setUnderline(0.2f, -2f);
            pphObservaciones.add(" ");
            pphObservaciones.add(observaciones);
            document.add(pphObservaciones);
            /**/
            Paragraph pphFecha = new Paragraph();
            Chunk fechaTitulo = new Chunk("FECHA:", fontTG);
            pphFecha.add(fechaTitulo);

            Chunk fecha = new Chunk(txtfecha.toString(), font);
            fecha.setUnderline(0.2f, -2f);
            pphFecha.add(" ");
            pphFecha.add(fecha);
            document.add(pphFecha);
            /**/
            document.add(new Paragraph(""));
            document.add(new Paragraph("\n"));
            Paragraph pphNomEval = new Paragraph("NOMBRE DEL EVALUADOR:", font);
            Chunk nomEval = new Chunk(txtevaluador, font);
            nomEval.setUnderline(0.2f, -2f);
            pphNomEval.add(" ");
            pphNomEval.add(nomEval);
            document.add(pphNomEval);
            /**/
            Paragraph pphFirma = new Paragraph();
            Chunk firmaTitulo = new Chunk("FIRMA:", fontTG);
            Chunk firmaRaya = new Chunk("  ______________________", font);
            pphFirma.add(firmaTitulo);
            pphFirma.add(firmaRaya);
            document.add(pphFirma);
            /**/
            document.close();
            RequestContext.getCurrentInstance().update("frmDescargarFormatoA");
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(PDFBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prepararContenidoFormatoB() {
        List<Productodetrabajo> lstProdTrab = ejbFacadeProdTrab.findAll();

        for (Productodetrabajo ProdTrab : lstProdTrab) {
            // Buscando el Formato B para el evaluador actual
            if (ProdTrab.getFormatoid().getFormatoid().equals(BigDecimal.valueOf(3)) && ProdTrab.getTrabajoid().getTrabajoid().equals(BigDecimal.valueOf(TrabajodeGradoActual.id))) {
                Productodetrabajo prodtrab = ProdTrab;
                Gson gson = new Gson();
                Map<String, String> decoded = gson.fromJson(prodtrab.getProductocontenido(), new TypeToken<Map<String, String>>() {
                }.getType());
                // Buscando el Formato B que diligencio el evaluador
                if (decoded.get("evaluador") != null && decoded.get("evaluador").equals(txtevaluador)) {
                    if (decoded.get("titulo") != null) {
                        txttitulo = decoded.get("titulo");
                    }
                    if (decoded.get("estudiante1") != null) {
                        txtestudiante1 = decoded.get("estudiante1");
                    }
                    if (decoded.get("estudiante2") != null) {
                        txtestudiante2 = decoded.get("estudiante2");
                    }
                    if (decoded.get("director") != null) {
                        txtdirector = decoded.get("director");
                    }
                    if (decoded.get("elementoConsideradoA") != null) {
                        txtelementoConsideradoA = Integer.valueOf(decoded.get("elementoConsideradoA"));
                    }
                    if (decoded.get("elementoConsideradoB") != null) {
                        txtelementoConsideradoB = Integer.valueOf(decoded.get("elementoConsideradoB"));
                    }
                    if (decoded.get("elementoConsideradoC") != null) {
                        txtelementoConsideradoC = Integer.valueOf(decoded.get("elementoConsideradoC"));
                    }
                    if (decoded.get("elementoConsideradoD") != null) {
                        txtelementoConsideradoD = Integer.valueOf(decoded.get("elementoConsideradoD"));
                    }
                    if (decoded.get("elementoConsideradoE") != null) {
                        txtelementoConsideradoE = Integer.valueOf(decoded.get("elementoConsideradoE"));
                    }
                    if (decoded.get("elementoConsideradoF") != null) {
                        txtelementoConsideradoF = Integer.valueOf(decoded.get("elementoConsideradoF"));
                    }
                    if (decoded.get("elementoConsideradoG") != null) {
                        txtelementoConsideradoG = Integer.valueOf(decoded.get("elementoConsideradoG"));
                    }
                    if (decoded.get("elementoConsideradoH") != null) {
                        txtelementoConsideradoH = Integer.valueOf(decoded.get("elementoConsideradoH"));
                    }
                    if (decoded.get("observaciones") != null) {
                        txtobservaciones = decoded.get("observaciones");
                    }
                    if (decoded.get("fecha") != null) {
                        txtfecha = new Date();
                    }
                    if (decoded.get("evaluador") != null) {
                        txtevaluador = decoded.get("evaluador");
                    }
                    return;
                }
            }
        }
    }

    public String elementocosiderado(int elemento) {
        if (elemento == 1) {
            return "Si";
        }
        if (elemento == 0) {
            return "No";
        }
        return null;
    }

    public String getAprobado() {
        if (txtelementoConsideradoH == 1) {
            return "Aprobado (X)    No Aprobado ( )";
        }
        if (txtelementoConsideradoH == 0) {
            return "Aprobado ( )    No Aprobado (X)";
        }
        return null;
    }
}
