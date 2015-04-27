/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public PDFBean() {
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public void generarPDF() {
        try {
            File file = File.createTempFile("Formato-A", ".pdf");

            pdfFileName = file.getName();

            Document document = new Document();

            PdfWriter pdfwriter = PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            Font bold = new Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD);

            URL url = FacesContext.getCurrentInstance().getExternalContext().getResource("/resources/img/escudo-unicauca.jpg");
            Image imgLogoUnicauca = Image.getInstance(url);
            imgLogoUnicauca.scaleAbsolute(118f, 131f);

            PdfPTable tableEncabezado = new PdfPTable(2);

            tableEncabezado.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tableEncabezado.setWidthPercentage(100);
            tableEncabezado.setSpacingAfter(5);

            PdfPCell cell1 = new PdfPCell(imgLogoUnicauca);
            cell1.setBorder(Rectangle.NO_BORDER);

            PdfPCell cell2 = new PdfPCell(new Paragraph("Universidad del Cauca\nFacultad de Ingeniería Electronica y\nTelecomunicaciones\nCI-FIET"));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell cell3 = new PdfPCell(new Paragraph("FORMATO A: PRESENTACIÓN DE LA PROPUESTA DE TRABAJO DE GRADO AL\nDEPARTAMENTO", bold));
            cell3.setBorder(Rectangle.NO_BORDER);

            PdfPCell cell4 = new PdfPCell(new Paragraph("", bold));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);

            tableEncabezado.addCell(cell1);
            tableEncabezado.addCell(cell2);
            tableEncabezado.addCell(cell3);
            tableEncabezado.addCell(cell4);

            document.add(tableEncabezado);

            document.close();
            RequestContext.getCurrentInstance().update("frmDescargarFormatoA");
        } catch (DocumentException ex) {
            Logger.getLogger(PDFBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
