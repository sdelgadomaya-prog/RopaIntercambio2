package es.safareyes.dtos;
import java.time.LocalDate;
import java.time.LocalDateTime;
public interface ConsultaSolicitudProyeccion {
    Long getIdSolicitud();
    String getTipoSolicitud();
    String getEstado();
    LocalDateTime getFechaCreacion();
    LocalDateTime getFechaRespuesta();
    LocalDate getFechaInicio();
    LocalDate getFechaFin();
    Long getIdPerfilOrigen();
    Long getIdPerfilDestino();
}