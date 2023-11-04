package pe.edu.utp.outimportec.service;

import org.springframework.stereotype.Component;
import pe.edu.utp.outimportec.model.Pago;

public interface EmailService {
    public void enviarEmailTexto(Pago pago);
    public void enviarEmailTextoAdjunto(String to, String subject, String body, String attachmentPath);
}
