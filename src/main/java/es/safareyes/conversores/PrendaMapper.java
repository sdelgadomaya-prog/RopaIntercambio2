package es.safareyes.conversores;

import es.safareyes.dtos.PrendaDTO;
import es.safareyes.dtos.PrendaDetalleDTO;
import es.safareyes.dtos.CrearPrendaDTO;
import es.safareyes.dtos.EditarPrendaDTO;
import es.safareyes.dtos.ConsultaPrendasPagProyeccion;
import es.safareyes.dtos.ConsultaPrendaDetalleProyeccion;
import es.safareyes.modelos.Prenda;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE)
public interface PrendaMapper {

    //convertimos una prenda del listado a DTO
    PrendaDTO toDTO(ConsultaPrendasPagProyeccion prenda);

    //una lista de prendas a DTOs
    List<PrendaDTO> toDTO(
            List<ConsultaPrendasPagProyeccion> prendas);

    // los detalles de una prenda a DTO
    PrendaDetalleDTO toDetalleDTO(
            ConsultaPrendaDetalleProyeccion prenda);

    //el DTO de creación en entidad
    @Mapping(target = "idPrenda", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "perfil", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    @Mapping(target = "disponibilidad", ignore = true)
    @Mapping(target = "fechaPublicacion", ignore = true)
    @Mapping(target = "estadoPublicacion", ignore = true)
    Prenda toEntity(CrearPrendaDTO dto);

    //una prenda existente
    @Mapping(target = "idPrenda", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "perfil", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    @Mapping(target = "disponibilidad", ignore = true)
    @Mapping(target = "fechaPublicacion", ignore = true)
    @Mapping(target = "estadoPublicacion", ignore = true)
    void actualizar(@MappingTarget Prenda prenda,
            EditarPrendaDTO dto);
}



