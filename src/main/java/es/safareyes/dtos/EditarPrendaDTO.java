package es.safareyes.dtos;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
public record EditarPrendaDTO(
        @Size(min = 1, max = 100)
        String titulo,
        String descripcion,

        @Size(max = 50)
        String marca,

        @Size(min = 1, max = 20)
        String talla,

        @Pattern(
                regexp = "Nuevo|Excelente|Bueno|Regular",
                message = "El estado no es válido")
        String estado,
        Boolean permitePrestamo,
        Boolean permiteIntercambio,

        @Positive
        Integer idCategoria) {
}
