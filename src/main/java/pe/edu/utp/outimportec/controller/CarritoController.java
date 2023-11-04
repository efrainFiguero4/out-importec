package pe.edu.utp.outimportec.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.outimportec.model.Carrito;
import pe.edu.utp.outimportec.model.Producto;
import pe.edu.utp.outimportec.model.Usuario;
import pe.edu.utp.outimportec.repository.CarritoRepository;
import pe.edu.utp.outimportec.repository.ProductoRepository;
import pe.edu.utp.outimportec.service.ClienteService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.List;

import static pe.edu.utp.outimportec.utils.Constants.MENSAJE;

@Log4j2
@Controller
@AllArgsConstructor
public class CarritoController {
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final ClienteService clienteService;

    @GetMapping("/carrito")
    public String carrito(Model model, Principal principal) {
        Usuario user = clienteService.findByUsername(principal.getName());
        List<Carrito> carritos = carritoRepository.findByIdUsuarioAndStatus(user.getId(), Carrito.ESTADO.PENDIENTE.name());
        BigDecimal sumPrecio = carritos.stream().map(Carrito::getPrecio).reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("carritos", carritos);
        model.addAttribute("total", sumPrecio);

        return "carrito/create";
    }

    @GetMapping("/carrito/agregar/{idProducto}")
    public String obtenerCarrito(@PathVariable Long idProducto, Principal principal, RedirectAttributes redirect) {
        Usuario user = clienteService.findByUsername(principal.getName());
        Producto producto = productoRepository.getOne(idProducto);
        BigDecimal subtotal = producto.getPrecio().divide(new BigDecimal("1.18"), RoundingMode.HALF_UP);
        carritoRepository.saveAndFlush(Carrito.builder()
                .cantidad(1)
                .precio(producto.getPrecio())
                .subtotal(subtotal)
                .igv(producto.getPrecio().subtract(subtotal))
                .producto(producto)
                .idUsuario(user.getId())
                .build());
        redirect.addFlashAttribute(MENSAJE, "Producto agregado correcamente");

        return "redirect:/carrito";
    }

    @GetMapping("/carrito/eliminar/{idcarrito}")
    public String eliminarCarrito(@PathVariable Long idcarrito, RedirectAttributes redirect) {
        carritoRepository.deleteById(idcarrito);
        redirect.addFlashAttribute(MENSAJE, "EL producto se ha quitado del carrito de compras.");
        return "redirect:/carrito";
    }

    @GetMapping(value = "/carrito/update/{idcarrito}/{cantidad}")
    public ResponseEntity<String> actualizarCantidadProducto(@PathVariable Long idcarrito, @PathVariable Integer cantidad) {
        log.info("carrito: {} ,cantidad: {} ", idcarrito, cantidad);
        if (cantidad > 0) {
            Carrito carrito = carritoRepository.getOne(idcarrito);
            carrito.setCantidad(cantidad);
            carrito.setPrecio(carrito.getProducto().getPrecio().multiply(new BigDecimal(cantidad)));
            BigDecimal subtotal = carrito.getPrecio().divide(new BigDecimal("1.18"), RoundingMode.HALF_UP);

            carrito.setSubtotal(subtotal);
            carrito.setIgv(carrito.getPrecio().subtract(subtotal));
            carritoRepository.save(carrito);
        }
        return ResponseEntity.ok("OK");
    }
}
