package pe.edu.utp.outimportec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.outimportec.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByCodigo(String codigo);
}
