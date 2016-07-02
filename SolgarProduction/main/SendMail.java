package main;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendMail {

	private static final String username = "hakan.kayakutlu";
	private static final String password = "3563755";
	private static final String email_from = "hakan.kayakutlu@yandex.ru";
	
    public static void main(String[] args) {    

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.yandex.ru");
        props.put("mail.smtp.port", "587");//587 25

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            @Override
			protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email_from));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse("hakan.kayakutlu@gmail.com"));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler,"
                + "\n\n Solgar information email!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void sendEmailToReceipents(String receiver,String receiver1,String receiver2,String Subject,String emailText) throws Exception { 

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.yandex.ru");
        props.put("mail.smtp.port", "587");//587 25

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            @Override
			protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email_from));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(receiver));
            message.setSubject(Subject);
            message.setText(emailText);

            Transport.send(message);
            if(receiver1.length()>0){
            	message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(receiver1));
            	Transport.send(message);
            }
            if(receiver2.length()>0){
            	message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(receiver2));
            	Transport.send(message);
            }

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
}