package es.safareyes.dtos;
import jakarta.validation.constraints.NotBlank;

public record CambiarEstadoSolicitudDTO(
        @NotBlank(message = "El estado es obligatorio")
        String estado) {

}