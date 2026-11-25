
package es.safareyes.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AgregarFotoDTO(
        @NotBlank
        @Pattern(
                regexp = "https?://.+",
                message = "Debes indicar una URL válida de fotografía")
        String url) {
}
