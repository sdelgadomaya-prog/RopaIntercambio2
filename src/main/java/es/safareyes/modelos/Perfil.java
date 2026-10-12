package es.safareyes.modelos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.*;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "perfil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Long idPerfil;


    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellido1", nullable = false, length = 50)
    private String apellido1;

    @Column(name = "apellido2", length = 50)
    private String apellido2;

    @Column(name = "biografia", columnDefinition = "TEXT")
    private String biografia;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "talla_zapatos", length = 10)
    private String tallaZapatos;

    @Column(name = "talla_pantalon", length = 10)
    private String tallaPantalon;

    @Column(name = "talla_camiseta", length = 10)
    private String tallaCamiseta;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "perfil_estilos",
            joinColumns = @JoinColumn(name = "id_perfil"),
            inverseJoinColumns = @JoinColumn(name = "id_estilo"))
    private Set<Estilo> estilos = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "perfil1", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Conversacion> conversacionesIniciadas = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "perfil2", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Conversacion> conversacionesRecibidas = new ArrayList<>();

}

