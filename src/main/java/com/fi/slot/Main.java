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


    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Main.class, args);
        StringBuilder earlierHighlightedString = new StringBuilder();
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
                StringBuilder availableList = new StringBuilder();
                boolean emailToBeSent= false;
                Elements elements = element.getElementsByTag("tr");
                emailMessage.append("<table style=\"\"><tbody>");
                for (Element element1 : elements) {
                    System.out.println(element1.text());
                    emailMessage.append("<tr>"+element1.html()+"</tr>");
                    if (element1.child(3).hasClass("earliest") && !(element1.getElementsByClass("earliest").text().equals("N/A"))) {
                        Elements earliest = element1.getElementsByClass("earliest");
                        if (checkDate(earliest) && !(earlierHighlightedString.toString()).contains(element1.html())) {
                          //  System.out.println("Book slot for - date - " + element1.text());

                            String allAvailableDates = getDetails(element1);
                            emailMessage.append("<tr style=\"background-color: yellow;\">" +element1.html()+"</tr>");
                            earlierHighlightedString.setLength(0);
                            earlierHighlightedString.append(element1.html());
                            availableList.append(allAvailableDates);
                            emailToBeSent = true;
                        }
                    }
                }
                if(emailToBeSent) {
                    emailMessage.append("</tbody></table>").append(availableList);
         //           System.out.println(emailMessage);
                    sendEmail(emailMessage);
                }
                Thread.sleep(120000);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String getDetails(Element element1) throws IOException {
        String href = element1.getElementsByTag("a").attr("href").toString();
        URL urlHref = new URL("https://visaslots.info" + href);
        HttpURLConnection connectionHref = (HttpURLConnection) urlHref.openConnection();
        connectionHref.setRequestMethod("GET");
        connectionHref.getResponseMessage();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connectionHref.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        Document document = Jsoup.parse(sb.toString());
        Elements allAvailableDates = document.getElementsByClass("updated");
        StringBuilder availDates = new StringBuilder();
        availDates.append("<summary role><strong>Latest Availability</strong></summary><table><thead><tr><th>Slots</th><th>Location</th><th>Visa</th><th>Year</th><th>Dates</th><th>Last Updated</th></tr></thead><tbody>");
        for(Element availableDates: allAvailableDates){

            availDates = availDates.append("<tr style=\"background-color: LightYellow;\">"+availableDates.parents().get(0).html()+"</tr>");
        }
        return availDates.append("</tbody></table>").toString();
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
            message.setContent("<html><head><style>table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head><body><h1> Slots - </h1>"+ emailMessage.toString() + "</body></html>","text/html");
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
            Date june1= dateFormat.parse("2024 May 19");
            Date august15= dateFormat.parse("2024 Aug 16");
            if(date.after(june1) && date.before(august15))
            {
                return true;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}