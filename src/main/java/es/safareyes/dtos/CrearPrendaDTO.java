package es.safareyes.dtos;
import jakarta.validation.constraints.*;
public record CrearPrendaDTO(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 100)
        String titulo,
        String descripcion,

        @Size(max = 50)
        String marca,

        @NotBlank(message = "La talla es obligatoria")
        @Size(max = 20)
        String talla,

        @NotBlank(message = "El estado es obligatorio")
        @Pattern(
                regexp = "Nuevo|Excelente|Bueno|Regular",
                message = "El estado no es válido")
        String estado,
        Boolean permitePrestamo,
        Boolean permiteIntercambio,

        @NotNull(message = "La categoría es obligatoria")
        @Positive
        Integer idCategoria,

        @NotNull(message = "El perfil es obligatorio")
        @Positive
        Long idPerfil) {
}

