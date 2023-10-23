package pe.edu.utp.outimportec.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.outimportec.model.Carrito;
import pe.edu.utp.outimportec.model.Pedido;
import pe.edu.utp.outimportec.model.Usuario;
import pe.edu.utp.outimportec.repository.CarritoRepository;
import pe.edu.utp.outimportec.repository.PedidoRepository;
import pe.edu.utp.outimportec.service.ClienteService;
import pe.edu.utp.outimportec.service.EmailService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class DashboardController {

    private final PedidoRepository pedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ClienteService clienteService;
    private final EmailService emailService;

    @GetMapping(value = "/pedidostotales", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> productos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        this.emailService.enviarEmailTexto("");

        List<Carrito> carritos = new ArrayList<>();
        pedidos.forEach(p -> carritos.addAll(p.getProductos()));

        Map<String, List<Carrito>> group = carritos.parallelStream().collect(groupingBy(c -> c.getProducto().getNombre(), toList()));

        List<Map<String, Object>> groups = group.entrySet().stream().map(g -> {
            Map<String, Object> maps = new HashMap<>();
            maps.put("descripcion", g.getKey());
            BigDecimal montoTotal = g.getValue().stream().map(n -> n.getProducto().getPrecio().multiply(new BigDecimal(n.getCantidad()))).reduce(BigDecimal.ZERO, BigDecimal::add);
            maps.put("montototal", montoTotal);
            return maps;
        }).collect(toList());
        return ResponseEntity.ok(groups);
    }

    @GetMapping(value = "/bolsa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Integer>> getCarrito(Principal principal) {
        Usuario user = clienteService.findByUsername(principal.getName());
       Integer cantidad = carritoRepository.obtenerCantidadCarritoXUsuarioId(user.getId());
        return ResponseEntity.ok(Collections.singletonMap("total", cantidad));
    }

}
