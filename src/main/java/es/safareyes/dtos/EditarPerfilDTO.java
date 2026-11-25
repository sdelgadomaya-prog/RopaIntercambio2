package es.safareyes.dtos;
import jakarta.validation.constraints.*;
import java.util.List;
public record EditarPerfilDTO(
        @Size(min = 1, max = 50)
        String nombre,

        @Size(min = 1, max = 50)
        String apellido1,

        @Size(max = 50)
        String apellido2,

        @Size(max = 100)
        String ciudad,

        String biografia,

        @Pattern(
                regexp = "XS|S|M|L|XL|XXL",
                message = "Talla de camiseta no válida")
        String tallaCamiseta,

        @Pattern(
                regexp = "3[4-9]|4[0-9]|50",
                message = "La talla de pantalón debe estar entre 34 y 50")
        String tallaPantalon,

        @Pattern(
                regexp = "3[5-9]|4[0-6]",
                message = "La talla de zapatos debe estar entre 35 y 46")
        String tallaZapatos,

        @Size(min = 1, max = 5)
        List<@NotNull @Positive Long> idsEstilos) {
}

