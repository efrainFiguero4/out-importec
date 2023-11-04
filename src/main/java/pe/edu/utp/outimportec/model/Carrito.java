package pe.edu.utp.outimportec.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_carrito")
public class Carrito {
    public enum ESTADO {PENDIENTE, PAGADO, FACTURADO}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Producto producto;

    @NotNull
    private Long idUsuario;

    @NotNull
    private Integer cantidad;

    private BigDecimal precio;
    private BigDecimal subtotal;
    private BigDecimal igv;

    @Builder.Default
    private String status = ESTADO.PENDIENTE.name();
}
