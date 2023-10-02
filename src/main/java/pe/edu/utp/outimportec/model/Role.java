package pe.edu.utp.outimportec.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_rol")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
}
