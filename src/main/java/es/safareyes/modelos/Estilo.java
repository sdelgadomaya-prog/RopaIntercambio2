package es.safareyes.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "estilos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estilo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estilo")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
}