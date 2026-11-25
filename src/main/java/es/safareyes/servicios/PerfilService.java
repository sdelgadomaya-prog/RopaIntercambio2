package es.safareyes.servicios;

import es.safareyes.Interfaces.IPerfilRepository;
import es.safareyes.Interfaces.IEstiloRepository;
import es.safareyes.conversores.PerfilMapper;
import es.safareyes.dtos.PerfilAfinDTO;
import es.safareyes.dtos.PerfilDTO;
import es.safareyes.dtos.EditarPerfilDTO;
import es.safareyes.excepciones.ElementoNoEncontrado;
import es.safareyes.modelos.Perfil;
import es.safareyes.modelos.Estilo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerfilService {
    private final IPerfilRepository repository;
    private final IEstiloRepository estiloRepository;
    private final PerfilMapper mapper;

    // 7. GET - Consultar perfil público
    public PerfilDTO consultarPerfil(Long id) {
        return repository.obtenerPerfil(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No se ha encontrado el usuario con ID " + id));
    }

    // 8. PUT - Editar mi perfil
    @Transactional
    public void editarPerfil(Long idUsuario, EditarPerfilDTO dto) {
        //buscar el perfil del usuario
        Perfil perfil = repository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No se ha encontrado el perfil del usuario " + idUsuario));

        //actualizar los datos con MapStruct
        mapper.actualizar(perfil, dto);

        //modificar los estilos si se han enviado
        if (dto.idsEstilos() != null) {
            List<Long> ids = dto.idsEstilos();
            // validamos cantidad y evitar estilos repetidos
            if (ids.isEmpty() || ids.size() > 5 ||
                    new HashSet<>(ids).size() != ids.size()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Debes indicar entre 1 y 5 estilos diferentes");
            }

            // Buscar los estilos en PostgreSQL
            List<Estilo> estilos = estiloRepository.findAllById(ids);
            if (estilos.size() != ids.size()) {
                throw new ElementoNoEncontrado(
                        "Uno o varios estilos no existen");
            }
            //asignar los estilos al perfil
            perfil.setEstilos(new HashSet<>(estilos));
        }
        repository.save(perfil);
    }

    // 9. GET - Consultar perfiles afines
    public List<PerfilAfinDTO> consultarPerfilesAfines(
            Long idUsuario
    ) {
        //buscar el perfil del usuario actual
        Perfil perfil = repository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No se ha encontrado el perfil del usuario " + idUsuario));

        //buscar otros perfiles con estilos en común
        return mapper.toAfinDTO(
                repository.obtenerPerfilesAfines(
                        perfil.getIdPerfil())
        );
    }
}


