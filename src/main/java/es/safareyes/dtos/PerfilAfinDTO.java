package es.safareyes.dtos;
public record PerfilAfinDTO(
        Long idPerfilAfin,
        String nombreCompleto,
        String ciudad,
        Long numEstilosComunes,
        String estilosComunes,
        String reputacion) {
}

