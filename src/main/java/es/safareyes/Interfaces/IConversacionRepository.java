
package es.safareyes.Interfaces;

import es.safareyes.modelos.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IConversacionRepository
        extends JpaRepository<Conversacion, Long> {

    // Buscar una conversación entre dos perfiles
    @Query("SELECT c FROM Conversacion c " +
            "WHERE (c.perfil1.idPerfil = :id1 AND c.perfil2.idPerfil = :id2) " +
            "OR (c.perfil1.idPerfil = :id2 AND c.perfil2.idPerfil = :id1)")
    Optional<Conversacion> buscarConversacion(
            @Param("id1") Long id1,
            @Param("id2") Long id2
    );
}
