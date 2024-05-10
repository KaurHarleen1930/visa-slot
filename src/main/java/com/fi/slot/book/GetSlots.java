//package com.fi.slot.book;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Component
//@Configuration
//public class GetSlots {
//    @Autowired
//    private JavaMailSender emailSend;
//
//    public void getVisaSlots(){
//        try{
//            URL url= new URL("https://visaslots.info/");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.connect();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line);
//            }
//            reader.close();
//            Document document = Jsoup.parse(sb.toString());
//            //           System.out.println(document);
//            Element element = document.getElementById("vsloc-f1-f2");
//            sendEmail(element);
//            Elements elements = element.getElementsByTag("tr");
//            for(Element element1: elements){
//                System.out.println(element1.text());
//                if(element1.child(3).hasClass("earliest") && !(element1.getElementsByClass("earliest").text().equals("N/A"))){
//                    Elements earliest = element1.getElementsByClass("earliest");
//                    if(checkDate(earliest))
//                    {
//                        System.out.println("Book slot for - date - "+ element1.text());
//                        sendEmail(element1);
//                    }
//                }
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void sendEmail(Element element1) {
//        try{
//            MimeMessage message = emailSend.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            String emails[] = {"harleenhora48@gmail.com", "av2918@columbia.edu"};
//            helper.setTo(emails);
//            helper.setSubject("Slot Update, Book slot now!!");
//            helper.setText("Slots - "+ element1.text());
//            emailSend.send(message);
//            System.out.println("Email Sent");
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static boolean checkDate(Elements earliest) {
//        String datePresent = earliest.text();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd");
//        Date date;
//        try{
//            date = dateFormat.parse(datePresent);
//            Date june1= dateFormat.parse("2024 Jun 01");
//            Date august15= dateFormat.parse("2024 Aug 15");
//            if(date.after(june1)&& date.before(august15))
//            {
//                return true;
//            }
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//        return false;
//    }
//}
