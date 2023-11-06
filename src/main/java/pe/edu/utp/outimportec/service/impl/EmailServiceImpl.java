package pe.edu.utp.outimportec.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pe.edu.utp.outimportec.model.Carrito;
import pe.edu.utp.outimportec.model.Pago;
import pe.edu.utp.outimportec.service.EmailService;
import javax.mail.internet.InternetAddress;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pe.edu.utp.outimportec.utils.Utils.converMap;
import static pe.edu.utp.outimportec.utils.Utils.convertDate;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final Configuration configuration;


    @Override
    public void enviarEmailTexto(Pago pago) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            InternetAddress remitente = new InternetAddress("notificaciones@outimportec.com", "Notificaciones Out Importec");

            helper.setTo(pago.getCorreo());
//            helper.setFrom(remitente.getAddress());
            helper.setSubject("Comprobante de Pago");
            helper.setText(getBody(pago), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String getBody(Pago pago) {
        Map<String, Object> model = converMap(pago.getPedido());
        model.put("id", pago.getId().toString());
        model.put("fechaPago", convertDate(pago.getFechaPago()));
        model.put("nombre", pago.getUsuario().getNombre());
        model.put("apellidos", pago.getUsuario().getApellidos());
        model.put("items", crearHtmlItems(pago.getPedido().getProductos()));

        try {
            Template template = configuration.getTemplate("invoice.html");
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Strings.EMPTY;
    }

    private String crearHtmlItems(List<Carrito> items) {
        return items.stream().map(i -> {
            String plantilla = "<tr>\n" +
                    "      <th>${descripcion}</th>\n" +
                    "      <th style=\"text-align: center\">${cantidad}</th>\n" +
                    "      <th>s/. ${subtotal}</th>\n" +
                    "      <th>s/. ${precio}</th>\n" +
                    "    </tr>";
            Map<String, Object> dataModel = converMap(i);
            dataModel.put("descripcion", i.getProducto().getNombre() + "\n - "+i.getProducto().getDescripcion());
            StringReader stringReader = new StringReader(plantilla);
            try {
                Template template = new Template("plantilla", stringReader, configuration);
                StringWriter stringWriter = new StringWriter();
                template.process(dataModel, stringWriter);
                return stringWriter.toString();
            } catch (TemplateException | IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.joining());
    }

}
