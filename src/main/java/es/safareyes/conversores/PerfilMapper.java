package es.safareyes.conversores;

import es.safareyes.dtos.PerfilDTO;
import es.safareyes.dtos.EditarPerfilDTO;
import es.safareyes.dtos.PerfilAfinDTO;
import es.safareyes.dtos.ConsultaPerfilProyeccion;
import es.safareyes.dtos.ConsultaPerfilAfinProyeccion;
import es.safareyes.modelos.Perfil;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE)
public interface PerfilMapper {

    //7 GET - Consultar perfil público
    PerfilDTO toDTO(ConsultaPerfilProyeccion perfil);

    //8 PUT - Editar mi perfil
    @Mapping(target = "estilos", ignore = true)
    void actualizar(@MappingTarget Perfil perfil,
            EditarPerfilDTO dto);

    //9(1) GET - Convertir un perfil afín a DTO
    PerfilAfinDTO toAfinDTO(
            ConsultaPerfilAfinProyeccion perfil);

    //9(2) GET - Convertir una lista de perfiles afines
    List<PerfilAfinDTO> toAfinDTO(
            List<ConsultaPerfilAfinProyeccion> perfiles);
}


