package es.safareyes.Interfaces;

import es.safareyes.dta.ConsultaSolicitudProyeccion;
import es.safareyes.modelos.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISolicitudRepository extends JpaRepository<Solicitud, Long> {

    //consulta nº6: solicitudes enviadas (las que él ha solicitado)
    @Query(nativeQuery = true, value =
            "SELECT " +
                    "  s.id_solicitud AS \"idSolicitud\", " +
                    "  s.tipo_solicitud AS \"tipoSolicitud\", " +
                    "  s.estado AS \"estado\", " +
                    "  s.fecha_creacion AS \"fechaCreacion\", " +
                    "  s.fecha_respuesta AS \"fechaRespuesta\", " +
                    "  s.fecha_inicio AS \"fechaInicio\", " +
                    "  s.fecha_fin AS \"fechaFin\", " +
                    "  s.id_perfil_origen AS \"idPerfilOrigen\", " +
                    "  s.id_perfil_destino AS \"idPerfilDestino\" " +
                    "FROM solicitudes s " +
                    "WHERE s.id_perfil_origen = :idPerfil " +
                    "  AND (CAST(:estado AS VARCHAR) IS NULL OR s.estado = CAST(:estado AS VARCHAR)) " +
                    "ORDER BY s.fecha_creacion DESC")
    List<ConsultaSolicitudProyeccion> obtenerSolicitudesEnviadas(
            @Param("idPerfil") Long idPerfil, @Param("estado") String estado);

    //consulta nº6: solicitudes recibidas (le han solicitado)
    @Query(nativeQuery = true, value =
            "SELECT " +
                    "  s.id_solicitud AS \"idSolicitud\", " +
                    "  s.tipo_solicitud AS \"tipoSolicitud\", " +
                    "  s.estado AS \"estado\", " +
                    "  s.fecha_creacion AS \"fechaCreacion\", " +
                    "  s.fecha_respuesta AS \"fechaRespuesta\", " +
                    "  s.fecha_inicio AS \"fechaInicio\", " +
                    "  s.fecha_fin AS \"fechaFin\", " +
                    "  s.id_perfil_origen AS \"idPerfilOrigen\", " +
                    "  s.id_perfil_destino AS \"idPerfilDestino\" " +
                    "FROM solicitudes s " +
                    "WHERE s.id_perfil_destino = :idPerfil " +
                    "  AND (CAST(:estado AS VARCHAR) IS NULL OR s.estado = CAST(:estado AS VARCHAR)) " +
                    "ORDER BY s.fecha_creacion DESC")
    List<ConsultaSolicitudProyeccion> obtenerSolicitudesRecibidas(
            @Param("idPerfil") Long idPerfil, @Param("estado") String estado);
}