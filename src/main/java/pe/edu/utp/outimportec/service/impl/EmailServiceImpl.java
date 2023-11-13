package pe.edu.utp.outimportec.service.impl;

import com.lowagie.text.DocumentException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pe.edu.utp.outimportec.utils.FileUtil.*;
import static pe.edu.utp.outimportec.utils.Utils.*;

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

            helper.setTo(pago.getCorreo());
            helper.setSubject("Comprobante de Pago");
            helper.setText(getBody(pago), true);

            Resource attachment = new ByteArrayResource(obtenerArchivoPdf(pago));
            helper.addAttachment("Boleta de venta.pdf", attachment);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String getBody(Pago pago) {
        try {
            Template template = configuration.getTemplate("invoice.html");
            StringWriter writer = new StringWriter();
            template.process(getModel(pago, true), writer);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Strings.EMPTY;
    }

    private String crearHtmlItems(List<Carrito> items) {
        return items.stream().map(i -> {
            String plantilla = "<tr>\n" + "      <th>${descripcion}</th>\n" +
                    "      <th style=\"text-align: center\">${cantidad}</th>\n" +
                    "      <th>s/. ${subtotal}</th>\n" +
                    "      <th>s/. ${precio}</th>\n" +
                    "    </tr>";
            Map<String, Object> dataModel = converMap(i);
            dataModel.put("descripcion", i.getProducto().getNombre() + "\n - " + i.getProducto().getDescripcion());
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

    private byte[] obtenerArchivoPdf(Pago pago) {
        byte[] json = StringFromJson(pago.getPedido().getProductos());
        Map<String, Object> model = getModel(pago, false);
        return generarReporte(json, model);
    }

    private Map<String, Object> getModel(Pago pago, boolean items) {

        Map<String, Object> model = new HashMap<>();

        model.put("id", pago.getId().toString());
        model.put("fechaPago", convertDate(pago.getFechaPago()));
        model.put("nombre", pago.getUsuario().getNombre());
        model.put("apellidos", pago.getUsuario().getApellidos());
        model.put("direccion", pago.getDireccion());
        model.put("telefono", pago.getTelefono());
        model.put("correo", pago.getCorreo());
        model.put("subTotal", pago.getPedido().getSubTotal().toString());
        model.put("igv", pago.getPedido().getIgv().toString());
        model.put("montoTotal", pago.getPedido().getMontoTotal().toString());
        model.put("metodoPago", pago.getPedido().getMetodoPago());
        model.put("usuario", pago.getUsuario().getUsername());
        if (items)
            model.put("items", crearHtmlItems(pago.getPedido().getProductos()));
        return model;
    }
}
