package es.safareyes.modelos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prenda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prenda")
    private Long idPrenda;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "disponibilidad", nullable = false)
    private Boolean disponibilidad = true;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "talla", length = 20)
    private String talla;

    @Column(name = "permite_prestamo", nullable = false)
    private Boolean permitePrestamo = false;

    @Column(name = "permite_intercambio", nullable = false)
    private Boolean permiteIntercambio = true;

    // Valores permitidos: Nuevo, Excelente, Bueno, Regular
    @Column(name = "estado", nullable = false, length = 30)
    private String estado = "Bueno";

    @Column(name = "fecha_publicacion", nullable = false, updatable = false)
    private LocalDateTime fechaPublicacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "prenda", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    @JsonIgnore
    private List<Foto> fotos = new ArrayList<>();

    @PrePersist
    void alCrear() {
        if (fechaPublicacion == null) {
            fechaPublicacion = LocalDateTime.now();
        }
    }
}