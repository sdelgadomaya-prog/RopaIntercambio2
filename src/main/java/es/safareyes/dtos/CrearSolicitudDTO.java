package es.safareyes.dtos;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record CrearSolicitudDTO(
        //la prenda que queremos solicitar
        @NotNull
        @Positive
        Long idPrendaSolicitada,

        //intercambio o préstamo
        @NotBlank
        @Pattern(
                regexp = "Intercambio|Prestamo",
                message = "El tipo debe ser Intercambio o Prestamo")
        String tipoSolicitud,

        //la prenda que ofrecemos en un intercambio
        @Positive
        Long idPrendaOfrecida,

        //fechas del préstamo
        LocalDate fechaInicio,
        LocalDate fechaFin) {
}


