package pe.edu.utp.outimportec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_cliente")
public class Usuario {

    public Usuario(boolean update) {
        this.isUpdate = update;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellidos;
    @NotBlank
    private String correo;
    @NotBlank
    private String telefono;
    @NotBlank
    private String direccion;

    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordencode;

    @ManyToOne
    private Role role;

    @Transient
    private String codigoRole;

    @Transient
    private Boolean isUpdate = Boolean.FALSE;

    @Transient
    private String confirmarpassword;

    private Integer estado;
}
