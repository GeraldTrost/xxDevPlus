



package org.xxdevplus.utl;

import com.sun.mail.smtp.SMTPTransport;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail
{

    public void Send(String username, String password, String recipientEmail, String ccEmail, String title, String message) throws Exception
    {
     Properties props = System.getProperties();
     props.put("mail.smtps.host","smtp.gmail.com");
     props.put("mail.smtps.auth","true");
     Session session = Session.getInstance(props, null);
     Message msg = new MimeMessage(session);
     msg.setFrom(new InternetAddress(username));
     //msg.setRecipients(Message.RecipientType.TO,
     //InternetAddress.parse("tov.are.jacobsen@iss.no", false));
     msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(username, false));

     msg.setSubject("EMAILTEST " + System.currentTimeMillis());
     msg.setText("This is an email send by javax");
     msg.setHeader("X-Mailer", "Tov Are's program");
     msg.setSentDate(new Date());
     SMTPTransport t = (SMTPTransport)session.getTransport("smtps");
     t.connect("smtp.gmail.com", username, password);    //t.connect("smtp.gmail.com", "bernardangerer@gmail.com", "ornella007");
     t.sendMessage(msg, msg.getAllRecipients());
     System.out.println("Response: " + t.getLastServerResponse());
     t.close();

    }

}
