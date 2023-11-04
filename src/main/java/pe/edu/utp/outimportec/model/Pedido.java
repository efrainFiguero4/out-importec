package pe.edu.utp.outimportec.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToMany
    private List<Carrito> productos;

    private BigDecimal montoTotal;
    private BigDecimal subTotal;
    private BigDecimal igv;
    private String metodoPago;
    private String direccion;
    private String telefono;
    private String correo;

    @JsonFormat(shape = STRING, pattern = "dd/MM/yyyy")
    private Date fechaPedido;

    @NotNull
    private Long idUsuario;

}
