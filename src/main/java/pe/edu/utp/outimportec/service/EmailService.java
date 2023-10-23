package pe.edu.utp.outimportec.service;

import org.springframework.stereotype.Component;

public interface EmailService {
    public void enviarEmailTexto(String email);
    public void enviarEmailTextoAdjunto(String email);
}
