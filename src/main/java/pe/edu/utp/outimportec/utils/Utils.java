package pe.edu.utp.outimportec.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pe.edu.utp.outimportec.model.Pedido;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Utils {

    public static <T> Map<String, Object> converMap(T pedido) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(pedido, new TypeReference<Map<String, Object>>() {
        });
    }

    public static String convertDate(Date fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(fecha);
    }
}
