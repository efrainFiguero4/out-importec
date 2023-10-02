package pe.edu.utp.outimportec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.outimportec.model.Usuario;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Usuario, Long> {

    public Usuario findByUsername(String username);

    public Optional<Usuario> findByUsernameAndPassword(String username, String password);
}
