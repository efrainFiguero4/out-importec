package pe.edu.utp.outimportec.service;

import pe.edu.utp.outimportec.model.Role;
import pe.edu.utp.outimportec.model.Usuario;

import java.util.List;


public interface ClienteService {

    public List<Usuario> listarTodos();

    public void guardar(Usuario usuario);

    public Usuario buscarPorId(Long id);

    public void eliminar(Long id);

    public List<Role> listarRoles();

    public Usuario findByUsername(String username);

}
