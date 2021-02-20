package bartos.lukasz.service.emailServices;

import bartos.lukasz.exception.EmailException;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import static j2html.TagCreator.*;

@Service
public class EmailService {
    
    private static String ATTACHMENT_PATH = "Ticket.pdf";
    private static String FILE_NAME = "Ticket.pdf";
    private static String LOGIN_FILE = "Ticket.pdf";

    public static void send(String to, String emailAddress, String password) {
        try {
            System.out.println("Sending email ...");
            Session session = createSession(emailAddress, password);
            MimeMessage mimeMessage = new MimeMessage(session);
            prepareEmailMessage(mimeMessage, prepareMimeBodyPart(ATTACHMENT_PATH, FILE_NAME, renderHtmlContent()), to, emailAddress);
            Transport.send(mimeMessage);
            System.out.println("Email has been sent.");
        } catch (Exception e) {
            throw new EmailException(e.getStackTrace());
        }
    }

    private static void prepareEmailMessage(MimeMessage mimeMessage, Multipart multipart, String to, String emailAddress) {
        try {
            mimeMessage.setContent(multipart);
            mimeMessage.setFrom(new InternetAddress(emailAddress));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject("ticket");
        } catch (Exception e) {
            throw new EmailException(e.getStackTrace());
        }
    }

    private static Session createSession(String emailAddress, String emailPassword) {
        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, emailPassword);
            }
        });
    }

    private static Multipart prepareMimeBodyPart(String path, String fileName, String html) {
        try {
            BodyPart mimeBodyPart1 = new MimeBodyPart();
            mimeBodyPart1.setContent(html, "text/html; charset=utf-8");

            MimeBodyPart mimeBodyPart2 = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(path);
            mimeBodyPart2.setDataHandler(new DataHandler(dataSource));
            mimeBodyPart2.setFileName(fileName);

            return prepareMultipart(mimeBodyPart1, mimeBodyPart2);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Multipart prepareMultipart(BodyPart mimeBodyPart1, MimeBodyPart mimeBodyPart2) {
        try {
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart1);
            multipart.addBodyPart(mimeBodyPart2);
            return multipart;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String renderHtmlContent() {
        return html(
                style(
                        ".contactText{ font-size: 10px; } .dangerText{ font-size: 7px; color: red; }"
                ),
                head(
                        title("Ticket")
                        //link().withRel("stylesheet").withHref("C:\\IntelijProjects\\Cinema\\style.css")
                ),
                body(
                        h1("Hi!"),
                        h2("We sent your ticket reserved on Super Hyper Cinema page"),
                        h3("In the attachment you will find the sent ticket in the form of a pdf file. " +
                                "If it is paid, you can use it to skip the ticket lines at the cinema and enter the room right away.\n" +
                                "We wish you a nice seance"),
                        hr(),
                        img().withSrc("https://www.radiopiekary.pl/wp-content/uploads/2018/11/Helios_Katowice_Dream_1a-960x640.jpg"),
                        hr(),
                        p(attrs(".contactText"),
                                "In case of any problems, please contact us at the e-mail address: \"lalala\" or the telephone number: xxx.\n" +
                                "We are not responsible for the loss of the ticket. The return is possible up to an hour before the screening."),
                        p(attrs(".dangerText"),
                                "* by not coming to the previously ordered place, you agree to pay the damages to the cinema in the amount of 100 times your salary")
                )).renderFormatted();
    }
}
