package es.safareyes.servicios;

import es.safareyes.Interfaces.ICategoriaRepository;
import es.safareyes.conversores.CategoriaMapper;
import es.safareyes.dtos.CategoriaDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoriaService {
    private ICategoriaRepository repository;
    private CategoriaMapper mapper;

    public List<CategoriaDTO> consultarTodas() {
        return mapper.toDTO(
                repository.obtenerCategoriasConCantidad()
        );
    }
}




