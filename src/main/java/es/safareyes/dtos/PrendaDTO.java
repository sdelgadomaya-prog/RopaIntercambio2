package es.safareyes.dtos;
import java.time.LocalDateTime;
public record PrendaDTO(
        Long idPrenda,
        String titulo,
        String marca,
        String talla,
        String estado,
        String categoria,
        String propietaria,
        String ciudad,
        Boolean permitePrestamo,
        Boolean permiteIntercambio,
        LocalDateTime fechaPublicacion) {
}
