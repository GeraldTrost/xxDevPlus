package org.xxdevplus.utl;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import java.net.InetAddress;
import java.util.Properties;
import java.util.Date;

import javax.mail.*;

import javax.mail.internet.*;

import com.sun.mail.smtp.*;


public class GMail {

    public static void main(String args[]) throws Exception {

        Properties props = System.getProperties();
        props.put("mail.smtps.host","smtp.gmail.com");
        props.put("mail.smtps.auth","true");

        Session session = Session.getInstance(props, null);
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress("bernardangerer@gmail.com"));

        //msg.setRecipients(Message.RecipientType.TO,
        //InternetAddress.parse("tov.are.jacobsen@iss.no", false));

        msg.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse("bernardangerer@gmail.com", false));

        msg.setSubject("EMAILTEST "+System.currentTimeMillis());
        msg.setText("This is an email send by javax");
        msg.setHeader("X-Mailer", "Tov Are's program");
        msg.setSentDate(new Date());
        SMTPTransport t =
            (SMTPTransport)session.getTransport("smtps");
        t.connect("smtp.gmail.com", "bernardangerer@gmail.com", "ornella007");
        t.sendMessage(msg, msg.getAllRecipients());
        System.out.println("Response: " + t.getLastServerResponse());
        t.close();
    }
}