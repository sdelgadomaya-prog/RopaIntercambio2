package es.safareyes.dtos;
public record PerfilDTO(
        Long idPerfil,
        String nombreCompleto,
        String ciudad,
        String biografia,
        String estilos,
        String tallas,
        String reputacion,
        String prendas) {
}

