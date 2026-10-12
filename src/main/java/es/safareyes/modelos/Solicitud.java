package es.safareyes.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "solicitudes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Long idSolicitud;

    // Valores permitidos: Intercambio, Prestamo
    @Column(name = "tipo_solicitud", nullable = false, length = 20)
    private String tipoSolicitud;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;

    // Valores permitidos: Pendiente, Aceptada, Rechazada, Cancelada, Finalizada
    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "Pendiente";

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_perfil_origen", nullable = false)
    private Perfil perfilOrigen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_perfil_destino", nullable = false)
    private Perfil perfilDestino;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "solicitud_prenda",
            joinColumns = @JoinColumn(name = "id_solicitud"),
            inverseJoinColumns = @JoinColumn(name = "id_prenda"))
    private Set<Prenda> prendas = new HashSet<>();

    @PrePersist
    void alCrear() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
}