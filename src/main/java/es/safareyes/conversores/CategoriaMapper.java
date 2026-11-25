package es.safareyes.conversores;

import es.safareyes.dtos.CategoriaDTO;
import es.safareyes.dtos.ConsultaCategoriaCantidadProyeccion;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaDTO toDTO(
            ConsultaCategoriaCantidadProyeccion categoria);

    List<CategoriaDTO> toDTO(
            List<ConsultaCategoriaCantidadProyeccion> categorias);
}



