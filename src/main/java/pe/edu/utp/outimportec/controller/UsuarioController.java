package pe.edu.utp.outimportec.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.outimportec.config.UsuarioValidator;
import pe.edu.utp.outimportec.model.Role;
import pe.edu.utp.outimportec.model.Usuario;
import pe.edu.utp.outimportec.repository.RoleRepository;
import pe.edu.utp.outimportec.service.ClienteService;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import static pe.edu.utp.outimportec.utils.Constants.ERROR;
import static pe.edu.utp.outimportec.utils.Constants.MENSAJE;

@Log4j2
@Controller
@AllArgsConstructor
public class UsuarioController {

    private final ClienteService clienteService;
    private final RoleRepository roleRepository;
    private final UsuarioValidator usuarioValidator;

    /**
     * @Comentario: Mantenimiento de Mi Cuenta
     */
    @GetMapping("/micuenta")
    public String obtenerMiCuenta(Model model, Principal principal) {
        Usuario usuario = clienteService.findByUsername(principal.getName());
        usuario.setIsUpdate(true);
        model.addAttribute("usuario", usuario);
        return "usuario/usuario";
    }

    @PostMapping("/micuenta")
    public String modificarCuenta(@ModelAttribute Usuario usuario, RedirectAttributes redirect, BindingResult bindingResult) {
        usuarioValidator.validate(usuario, bindingResult);
        if (bindingResult.hasErrors()) {
            redirect.addFlashAttribute(ERROR, bindingResult.getAllErrors().get(0).getCode());
        }
        redirect.addFlashAttribute(MENSAJE, "Datos correctamente actulizados.");
        clienteService.guardar(usuario);
        return "redirect:/micuenta";
    }


    /**
     * @Comentario: Mantenimiento de clientes
     */
    @GetMapping("/clientes/lista")
    public String listarClientes(Model model) {
        List<Usuario> listadoUsuarios = clienteService.listarTodos();

        model.addAttribute("titulo", "Lista de cliente");
        model.addAttribute("clientes", listadoUsuarios);
        return "clientes/listar";
    }

    @GetMapping("/clientes/create")
    public String obtenerFomularioCliente(Model model) {
        Usuario usuario = new Usuario(false);
        List<Role> listadoRoles = clienteService.listarRoles();

        model.addAttribute("titulo", "REGISTRAR USUARIO");
        model.addAttribute("cliente", usuario);
        model.addAttribute("roles", listadoRoles);
        return "clientes/formulario";
    }

    @PostMapping("/clientes/save")
    public String registrarActualizarCliente(@ModelAttribute Usuario cliente, BindingResult bindingResult, RedirectAttributes redirect) {
        if (Objects.nonNull(cliente.getId())) {
            cliente.setIsUpdate(true);
            cliente.setCodigoRole(cliente.getRole().getCodigo());
        }
        else {
            Usuario exist = clienteService.findByUsername(cliente.getUsername());
            if (Objects.nonNull(exist)) {
                redirect.addFlashAttribute(ERROR, "El usuario ya se encuentra registrado");
                return "redirect:/clientes/create";
            }
        }

        usuarioValidator.validate(cliente, bindingResult);
        if (bindingResult.hasErrors()) {
            redirect.addFlashAttribute(ERROR, bindingResult.getAllErrors().get(0).getCode());
            return "redirect:/clientes/create";
        }
        String sms = Objects.isNull(cliente.getId()) ? "creado" : "actualizado";
        redirect.addFlashAttribute(MENSAJE, "Usuario " + sms + " correctamente.");
        clienteService.guardar(cliente);
        return "redirect:/clientes/lista";
    }

    @GetMapping("/clientes/edit/{id}")
    public String obtenerClientePorId(@PathVariable("id") Long idCliente, Model model) {
        Usuario usuario = clienteService.buscarPorId(idCliente);
        List<Role> listadoRoles = clienteService.listarRoles();
        usuario.setCodigoRole(usuario.getRole().getCodigo());

        model.addAttribute("titulo", "ACTUALIZAR USUARIO");
        model.addAttribute("cliente", usuario);
        model.addAttribute("roles", listadoRoles);
        return "clientes/formulario";
    }

    @GetMapping("/clientes/delete/{id}")
    public String eliminarClientePorId(@PathVariable("id") Long idCliente) {
        clienteService.eliminar(idCliente);
        System.out.println("Registro Eliminado:");
        return "redirect:/clientes/lista";
    }
}
