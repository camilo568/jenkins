package com.psl.integrador.service;

import com.psl.integrador.model.Collaborator;
import com.psl.integrador.model.Topic;
import com.psl.integrador.model.enums.Role;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final String fromEmail = "praxistestmailintegrator2018@gmail.com";
    private final String password = "Praxis2018";

    Authenticator auth = new Authenticator() {
        //override the getPasswordAuthentication method
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(fromEmail, password);
        }
    };

    @Override
    public void sendNotification(Map<Collaborator, Role> collaboratorRoleMap,Topic topic,int tipo) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        for(Map.Entry<Collaborator, Role> entry : collaboratorRoleMap.entrySet()) {
            Collaborator collaborator = entry.getKey();
            Role role = entry.getValue();
            System.out.println("---------> Start");
            String toEmail = collaborator.getEmail();
            Session session = Session.getInstance(props,auth);
            if(role == Role.student) {
                switch (tipo){
                    case 1: sendEmail(session, toEmail, "Nuevo grupo " + topic.getName(), bodyOpenLearn(collaborator.getName(),
                            topic.getName(), topic.getChat(),fromEmail));
                            break;
                    case 2:
                        sendEmail(session,toEmail,"El grupo " + topic.getName() + " se ha cerrado.",notOpen(collaborator.getName(),
                                topic.getName(),fromEmail));
                        break;
                    case 3:
                        sendEmail(session,toEmail,"El grupo " + topic.getName() + " se ha cerrado.",closed(collaborator.getName(),
                                topic.getName(),fromEmail));
                        break;
                }

            }else{
                switch (tipo){
                    case 1: sendEmail(session, toEmail, "Nuevo grupo " + topic.getName(), bodyOpenTeach(collaborator.getName(),
                            topic.getName(), topic.getChat(),fromEmail));
                            break;
                    case 2:
                        sendEmail(session,toEmail,"El grupo " + topic.getName() + " se ha cerrado.",notOpen(collaborator.getName(),
                                topic.getName(),fromEmail));
                        break;
                    case 3:
                        sendEmail(session,toEmail,"El grupo " + topic.getName() + " se ha cerrado.",closed(collaborator.getName(),
                                topic.getName(),fromEmail));
                        break;
                }

            }
            System.out.println("Sending email to " + collaborator.getName() + " " + collaborator.getEmail() + " " + role);
        }
    }

    private static void sendEmail(Session session,String toEmail,String subject,String body){

        MimeMessage msg = new MimeMessage(session);
        try {
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress("praxistestmailintegrator2018@gmail.com","NoReply-JD"));
            msg.setReplyTo(InternetAddress.parse("praxistestmailintegrator2018@gmail.com", false));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    private String bodyOpenLearn(String name,String topicName,String chat,String mail){
        String cuerpo = "Hola " + name + "\n"
                +"Hace un tiempo nos comentaste tu interes en aprender " + topicName +", hemos creado un grupo un \n"
                + "Skype para que compartas informacion con otras personas que tambien tienen interes en este tema. \n"
                + "Aqui puedes acceder al link de skype "+ chat + "\n"
                + "Adicionalmente, estos compañeros te podran proporcionar guia: \n"
                + "Si tienes alguna duda escribenos a: " + mail
                + "Logistica actividades PSL";
        return cuerpo;
    }
    private String bodyOpenTeach(String name,String topicName,String chat,String mail){
        String cuerpo = "Hola " + name + "\n"
                +"Hace un tiempo nos comentaste tu interes en guiar el tema " + topicName +", hemos creado un grupo un \n"
                + "Skype para que ayudes a tus compañeros que tienen interes en aprender sobre el tema \n"
                + "Aqui puedes acceder al link de skype "+ chat + "\n"
                + "Si tienes alguna duda escrienos a "+ mail +"\n"
                + "Logistica actividades PSL";
        return cuerpo;
    }
    private String notOpen(String name,String topicName,String mail){
        String cuerpo = "Hola "+ name+ "\n"
                +"Hace un tiempo nos comentaste tu interes en el tema " + topicName+", lamentablemente no encontramos el \n"
                + "numero suficiente de personas para abrir el grupo. No te desanimes! Te invitamos a inscribirte \n"
                + "a otros grupos ya activos. \n"
                + "Si tienes alguna duda escrienos a "+ mail +"\n"
                + "Logistica actividades PSL";
        return cuerpo;
    }
    private String closed(String name, String topicName,String mail){
        String cuerpo = "Hola " + name + "\n"
                +"Queremos informarte que el grupo " + topicName + "se ha cerrado, \n"
                + "si consideras que deberia reactivarse escribenos a " + mail + ".\n"
                +"Logistica actividades PSL";
        return cuerpo;
    }

}
