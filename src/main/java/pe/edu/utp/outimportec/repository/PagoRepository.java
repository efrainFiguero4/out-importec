package pe.edu.utp.outimportec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.outimportec.model.Pago;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuarioId(Long idUsuario);

    @Query(value = "SELECT p FROM Pago p WHERE p.usuario.id=?1 And p.fechaPago>=?2 and p.fechaPago<=?3")
//    @Query(nativeQuery = true, value = "select * from t_pago where usuario_id = ?1 and fecha_pago >=?2 and  fecha_pago <=?3")
    List<Pago> findByUsuarioId(Long usuario, Timestamp desde, Timestamp hasta);

}
