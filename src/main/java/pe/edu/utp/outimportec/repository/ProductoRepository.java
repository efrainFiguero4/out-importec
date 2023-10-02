package pe.edu.utp.outimportec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.outimportec.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
