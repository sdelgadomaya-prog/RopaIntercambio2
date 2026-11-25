
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

    // Tipo: Intercambio o Prestamo
    @Column(name = "tipo_solicitud", nullable = false, length = 20)
    private String tipoSolicitud;

    // Fecha de creación
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Fecha de respuesta
    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;

    // Estado de la solicitud
    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "Pendiente";

    // Fechas del préstamo
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    // Persona que realiza la solicitud
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_perfil_origen", nullable = false)
    private Perfil perfilOrigen;

    // Persona que recibe la solicitud
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_perfil_destino", nullable = false)
    private Perfil perfilDestino;

    // NUEVO: Prenda que queremos conseguir
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_prenda_solicitada", nullable = false)
    private Prenda prendaSolicitada;

    // NUEVO: Prenda ofrecida en un intercambio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prenda_ofrecida")
    private Prenda prendaOfrecida;

    // Relación anterior: la conservamos temporalmente
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "solicitud_prenda",
            joinColumns = @JoinColumn(name = "id_solicitud"),
            inverseJoinColumns = @JoinColumn(name = "id_prenda")
    )
    private Set<Prenda> prendas = new HashSet<>();

    // Establecer la fecha automáticamente al crear
    @PrePersist
    void alCrear() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
}
