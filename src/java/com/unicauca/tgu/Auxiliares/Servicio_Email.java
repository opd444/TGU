/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author pcblanco
 */
public class Servicio_Email {
    
    private String to;
    private String from;
    private String messagebody;
    private String subject;
    private String motpasse; // password
    private final String bodyformat = "<div style='background-color:#18457C; width:459px;'><img src='http://imageshack.com/a/img537/148/ODwI2A.png'>"
       + "<div style='color:black; font-size:16px;padding:15px'><br><h4 style='color:white;'>Cordial Saludo,</h4><br>"
       + "<p style='color:white; font-family: sans-serif; font-size: 14px;'> %s </p><br>"
       + "</div> <div style='color:white;font-size:11px; padding:15px'> <i>Por favor no responda a este correo electrónico.</i>" 
       + " <p>Este correo y sus anexos contienen información confidencial de la Universidad del Cauca, la cual solo está dirigida "
       + "a la persona o entidad listada arriba. Cualquier uso indebido de la información contenida en este correo por personas "
       + "diferentes a las dirigidas, es prohibido. Si recibe este correo por error, favor notificar al remitente y borrarlo.</p>"
       + "</div> <img src='http://imageshack.com/a/img913/3982/U2td48.png'> </div>";


    public Servicio_Email() {
        to = "orlandopaz@unicauca.edu.co";
        from = "proyectospopasoft@gmail.com";
        messagebody = "hello";
        subject = "HELLO";
        motpasse = "popasoft123";
    }

    public Servicio_Email(String to, String subject) {
        this.to = to;
        this.from = "proyectospopasoft@gmail.com";
        this.subject = subject;
        this.motpasse = "popasoft123";;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return messagebody;
    }

    public void setMessage(String message) {
        this.messagebody = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMotpasse() {
        return motpasse;
    }

    public void setMotpasse(String motpasse) {
        this.motpasse = motpasse;
    }
    
    public void sendEmail() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.user", from);

        Session session = Session.getDefaultInstance(properties);
                
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.to));
            message.setSubject(this.subject);
            message.setContent(this.messagebody, "text/html");
            
            
            Transport t = session.getTransport("smtp");
            t.connect(from,motpasse);
            t.sendMessage(message,message.getAllRecipients());
            
            t.close();
            
        } catch (MessagingException e) {            
            throw new RuntimeException(e);            
        }
    }

    public void enviarDiligenciadoFormatoA(String nombretg) {
       messagebody = String.format(bodyformat,  "La presentación de la idea del "
                     + "Trabajo de Grado: '<strong>"+nombretg+"</strong>' ha sido diligenciada con éxito.");
             sendEmail();
    }
    
    public void enviarEditadoFormatoA(String nombretg) {
        messagebody = String.format(bodyformat,  "La presentación de la idea del "
                    + "Trabajo de Grado: '<strong>"+nombretg+"</strong>' ha sido editada con éxito.");
        sendEmail();
    }
    
    public void enviarDiligenciadoAnteproyecto(String nombretg) {
          messagebody = String.format(bodyformat,  "El anteproyecto del "
                     + "Trabajo de Grado: '<strong>"+nombretg+"</strong>' ha sido diligenciado con éxito.");
             sendEmail();
    }
    
    public void enviarEditadoAnteproyecto(String nombretg) {
          messagebody = String.format(bodyformat,  "El anteproyecto del "
                     + "Trabajo de Grado: '<strong>"+nombretg+"</strong>' ha sido editado con éxito.");
             sendEmail();
    }
    
    public void enviarDiligenciadoRevisionIdea(String nombretg) {
          messagebody = String.format(bodyformat,  "La revisión de la idea del "
                     + "Trabajo de Grado: '<strong>"+nombretg+"</strong>' ha sido diligenciada con éxito.");
             sendEmail();
    }
    
    public void enviarEditadoRevisionIdea(String nombretg) {
          messagebody = String.format(bodyformat,  "La revisión de la idea del "
                     + "Trabajo de Grado: '<strong>"+nombretg+"</strong>' ha sido editada con éxito.");
             sendEmail();
    }
    
    public void enviarAsignacionEvaluacionanteproyecto(String nombretg) {
          messagebody = String.format(bodyformat,  "Usted fue asignado como evaluador "
                     + "del anteproyecto del trabajo de grado '<strong>"+nombretg+"</strong>'. A partir de la "
                  + "fecha tiene un plazo máximo  de 15 días para realizar la evaluación del mismo.");
             sendEmail();
    }
}