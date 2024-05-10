package com.fi.slot;



//import jakarta.mail.MessagingException;
//import jakarta.mail.Session;
//import jakarta.mail.Transport;
//import jakarta.mail.internet.InternetAddress;
//import jakarta.mail.internet.MimeMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

//import jakarta.mail.Message;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


@SpringBootApplication
@Configurable
@RestController
public class Main {


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        while(true) {
            try {
                URL url = new URL("https://visaslots.info/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                Document document = Jsoup.parse(sb.toString());
                //           System.out.println(document);
                Element element = document.getElementById("vsloc-f1-f2");
                //           sendEmail(element);
                StringBuilder emailMessage = new StringBuilder();
                boolean emailToBeSent= false;
                Elements elements = element.getElementsByTag("tr");
                for (Element element1 : elements) {
                    System.out.println(element1.text());
                    emailMessage.append(element1.text()+"\n");
                    if (element1.child(3).hasClass("earliest") && !(element1.getElementsByClass("earliest").text().equals("N/A"))) {
                        Elements earliest = element1.getElementsByClass("earliest");
                        if (checkDate(earliest)) {
                            System.out.println("Book slot for - date - " + element1.text());
                            emailMessage.append("***** BOOK SLOT FOR - DATE DETAILS *****\n" +element1.text()+"\n");
                            emailToBeSent = true;
                        }
                    }
                }
                if(emailToBeSent) {
                    sendEmail(emailMessage);
                }
                Thread.sleep(120000);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static void sendEmail(StringBuilder emailMessage) {
        try{

            String to[] = {"harleenhora48@gmail.com", "av2918@columbia.edu", "chhayatundwal192@gmail.com"};
         //   String to="harleenhora48@gmail.com";
            String from = "hkaur193007@gmail.com";
            String password = "cqmc gqgp ivku wdhg";
            String host = "smtp.gmail.com";
            Properties properties= System.getProperties();
            properties.put("mail.smtp.host",host);
            properties.put("mail.smtp.port","587");
            properties.put("mail.smtp.auth","true");
            properties.put("mail.smtp.starttls.enable","true");
            Session session= Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            message.addRecipients(Message.RecipientType.TO, new InternetAddress(to[0]).toString());
            message.addRecipients(Message.RecipientType.TO, new InternetAddress(to[1]).toString());
            message.addRecipients(Message.RecipientType.TO, new InternetAddress(to[2]).toString());
            message.setSubject("Slot Update, Book slot now!!");
            message.setText("Slots - "+ emailMessage.toString());
            Transport.send(message);
//            helper.setTo(emails);
//            helper.setSubject("Slot Update, Book slot now!!");
//            helper.setText("Slots - "+ element1.text());
//            emailSend.send(message);
            System.out.println("Email Sent");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean checkDate(Elements earliest) {
        String datePresent = earliest.text();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd");
        Date date;
        try{
            date = dateFormat.parse(datePresent);
            Date june1= dateFormat.parse("2024 Jun 1");
            Date august15= dateFormat.parse("2024 Aug 15");
            if(date.after(june1)&& date.before(august15))
            {
                return true;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}