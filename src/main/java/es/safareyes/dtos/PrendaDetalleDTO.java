package es.safareyes.dtos;
import java.time.LocalDateTime;
public record PrendaDetalleDTO(
        Long idPrenda,
        String titulo,
        String descripcion,
        String marca,
        String talla,
        String estado,
        Boolean disponibilidad,
        Boolean permiteIntercambio,
        Boolean permitePrestamo,
        LocalDateTime fechaPublicacion,
        String categoria,
        Long idPropietario,
        String propietario,
        Double mediaPuntuacion,
        Long numValoraciones,
        String fotos) {
}

