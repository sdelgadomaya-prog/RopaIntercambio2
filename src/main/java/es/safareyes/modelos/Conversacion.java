package es.safareyes.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "conversacion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Conversacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conversacion")
    private Long idConversacion;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Valores permitidos: Activa, Archivada, Bloqueada
    @Column(name = "estado", nullable = false, length = 20)
    @Builder.Default
    private String estado = "Activa";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil_1", nullable = false)
    private Perfil perfil1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil_2", nullable = false)
    private Perfil perfil2;

    @PrePersist
    void alCrear() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

}