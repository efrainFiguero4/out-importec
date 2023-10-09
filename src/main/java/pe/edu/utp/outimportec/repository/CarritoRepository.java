package pe.edu.utp.outimportec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.outimportec.model.Carrito;

import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    List<Carrito> findByIdUsuarioAndStatus(Long idUsuario, String status);

    @Query(value = "SELECT COUNT(1) FROM Carrito o WHERE o.idUsuario=?1 AND o.status='PENDIENTE'")
    Integer obtenerCantidadCarritoXUsuarioId(Long idUsuario);
}
