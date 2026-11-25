package es.safareyes.Interfaces;

import es.safareyes.modelos.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IValoracionRepository extends JpaRepository<Valoracion, Long> {

    // Comprobar si el usuario ya ha valorado esta solicitud
    boolean existsBySolicitud_IdSolicitudAndPerfilOrigen_IdPerfil(
            Long idSolicitud,
            Long idPerfil
    );

}