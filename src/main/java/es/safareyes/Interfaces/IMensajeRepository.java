package es.safareyes.Interfaces;

import es.safareyes.dta.ConversacionProyeccion;
import es.safareyes.modelos.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMensajeRepository extends JpaRepository<Mensaje, Long> {

    //consulta nº7
    @Query(nativeQuery = true, value =
            "SELECT " +
                    "  m.id_mensaje AS \"idMensaje\", " +
                    "  s.id_solicitud AS \"idSolicitud\", " +
                    "  p_emisor.id_perfil AS \"idEmisor\", " +
                    "  CONCAT_WS(' ', p_emisor.nombre, p_emisor.apellido1) AS \"emisor\", " +
                    "  p_receptor.id_perfil AS \"idReceptor\", " +
                    "  CONCAT_WS(' ', p_receptor.nombre, p_receptor.apellido1) AS \"receptor\", " +
                    "  m.contenido AS \"contenido\", " +
                    "  m.fecha_envio AS \"fechaEnvio\", " +
                    "  m.hora_envio AS \"horaEnvio\" " +
                    "FROM solicitudes s " +
                    "  JOIN conversacion c " +
                    "    ON (c.id_perfil_1 = s.id_perfil_origen AND c.id_perfil_2 = s.id_perfil_destino) " +
                    "    OR (c.id_perfil_1 = s.id_perfil_destino AND c.id_perfil_2 = s.id_perfil_origen) " +
                    "  JOIN mensajes m ON m.id_conversacion = c.id_conversacion " +
                    "  JOIN perfil p_emisor ON p_emisor.id_perfil = m.id_perfil_remitente " +
                    "  JOIN perfil p_receptor ON p_receptor.id_perfil = " +
                    "       CASE WHEN c.id_perfil_1 = m.id_perfil_remitente " +
                    "            THEN c.id_perfil_2 ELSE c.id_perfil_1 END " +
                    "WHERE s.id_solicitud = :idSolicitud " +
                    "ORDER BY m.fecha_envio DESC, m.hora_envio DESC")
    List<ConversacionProyeccion> obtenerConversacion(@Param("idSolicitud") Long idSolicitud);
}