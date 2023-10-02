package pe.edu.utp.outimportec.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "t_contacto")
public class Contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String nombres;

    @NotNull
    private String apellidos;

    @NotNull
    private String telefono;

    @NotNull
    private String correo;

    @NotNull
    private String mensaje;
}
