package es.safareyes.dtos;
import java.time.LocalDate;
import java.time.LocalTime;
public record ConsultarMensajeDTO(
        Long idMensaje,
        Long idPerfilRemitente,
        String contenido,
        LocalDate fechaEnvio,
        LocalTime horaEnvio) {
}
