
package es.safareyes.dtos;

import jakarta.validation.constraints.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegistroUsuarioDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String contrasena;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido1;

    private String apellido2;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Past
    private LocalDate fechaNacimiento;

    @NotNull
    @Size(min = 1, max = 5)
    private List<@NotNull @Positive Long> idsEstilos;

}
