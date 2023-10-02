package pe.edu.utp.outimportec.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "t_producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String nombre;

    @NotNull
    private BigDecimal precio;

    @NotNull
    private BigDecimal descuento;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String imagen;

    @NotNull
    private String nombreimagen;

    @NotNull
    private Integer categoria;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Transient
    private BigDecimal precioDescuento;

    @Transient
    private BigDecimal precioCarrito;

    public String recortar() {
        return this.descripcion.substring(0, Math.min(this.descripcion.length(), 44)) + (this.descripcion.length() > 44 ? "..." : "");
    }
    public String recortarnombre() {
        return this.nombre.substring(0, Math.min(this.nombre.length(), 44)) + (this.nombre.length() > 44 ? "..." : "");
    }
}
