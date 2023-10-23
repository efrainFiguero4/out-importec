package pe.edu.utp.outimportec.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pe.edu.utp.outimportec.service.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void enviarEmailTexto(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("efrain.figue.95@gmail.com");
        message.setTo("andreson561@hotmail.com");
        message.setSubject("Correo de prueba OUT IMPORTEC");
        message.setText("Correo de prueba OUT IMPORTEC");
        emailSender.send(message);
    }

    @Override
    public void enviarEmailTextoAdjunto(String email) {

    }
}
