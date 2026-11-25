package es.safareyes.conversores;

import es.safareyes.dtos.CrearSolicitudDTO;
import es.safareyes.dtos.SolicitudDTO;
import es.safareyes.modelos.Solicitud;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SolicitudMapper {

    //convertimos el DTO de creación en una entidad
    @Mapping(target = "idSolicitud", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaRespuesta", ignore = true)
    @Mapping(target = "perfilOrigen", ignore = true)
    @Mapping(target = "perfilDestino", ignore = true)
    @Mapping(target = "prendaSolicitada", ignore = true)
    @Mapping(target = "prendaOfrecida", ignore = true)
    @Mapping(target = "prendas", ignore = true)
    Solicitud toEntity(CrearSolicitudDTO dto);

    //una solicitud en DTO de respuesta
    @Mapping(target = "idPrendaSolicitada",
            source = "prendaSolicitada.idPrenda")
    @Mapping(target = "idPrendaOfrecida",
            source = "prendaOfrecida.idPrenda")
    @Mapping(target = "idPerfilOrigen",
            source = "perfilOrigen.idPerfil")
    @Mapping(target = "idPerfilDestino",
            source = "perfilDestino.idPerfil")
    SolicitudDTO toDTO(Solicitud solicitud);
}
