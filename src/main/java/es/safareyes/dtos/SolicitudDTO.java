package es.safareyes.dtos;
import java.time.LocalDate;
import java.time.LocalDateTime;
public record SolicitudDTO(
        Long idSolicitud,
        String tipoSolicitud,
        String estado,
        Long idPrendaSolicitada,
        Long idPrendaOfrecida,
        Long idPerfilOrigen,
        Long idPerfilDestino,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        LocalDateTime fechaCreacion) {
}
