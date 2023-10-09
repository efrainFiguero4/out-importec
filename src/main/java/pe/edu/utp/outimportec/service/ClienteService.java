package pe.edu.utp.outimportec.service;

import pe.edu.utp.outimportec.model.Role;
import pe.edu.utp.outimportec.model.Usuario;

import java.util.List;


public interface ClienteService {

    List<Usuario> listarTodos();

    void guardar(Usuario usuario);

    Usuario buscarPorId(Long id);

    void eliminar(Long id);

    List<Role> listarRoles();

    Usuario findByUsername(String username);

}
