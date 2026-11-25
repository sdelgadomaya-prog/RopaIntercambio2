package es.safareyes.servicios;

import es.safareyes.Interfaces.IPerfilRepository;
import es.safareyes.Interfaces.ISolicitudRepository;
import es.safareyes.Interfaces.IValoracionRepository;

import es.safareyes.dtos.CrearValoracionDTO;
import es.safareyes.excepciones.ElementoNoEncontrado;
import es.safareyes.modelos.Perfil;
import es.safareyes.modelos.Solicitud;
import es.safareyes.modelos.Valoracion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ValoracionService {

    private final IValoracionRepository valoracionRepository;
    private final ISolicitudRepository solicitudRepository;
    private final IPerfilRepository perfilRepository;

    //15. POST - Crear una valoración
    @Transactional
    public Long crearValoracion(
            Long idUsuario,
            Long idSolicitud,
            CrearValoracionDTO dto) {

        //buscamos el perfil del usuario
        Perfil autor = perfilRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No existe el perfil del usuario"));

        //buscamosla solicitud
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No existe la solicitud"));

        // 3. Comprobar que participa en la solicitud
        Long idAutor = autor.getIdPerfil();
        Long idOrigen = solicitud.getPerfilOrigen().getIdPerfil();
        Long idDestino = solicitud.getPerfilDestino().getIdPerfil();
        if (!idAutor.equals(idOrigen)
                && !idAutor.equals(idDestino)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No participas en esta solicitud");
        }

        // 4. Solo se puede valorar una solicitud finalizada
        if (!solicitud.getEstado().equalsIgnoreCase("Finalizada")) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Solo se pueden valorar solicitudes finalizadas");
        }

        //comprobar que no haya valorado antes
        boolean repetida = valoracionRepository
                .existsBySolicitud_IdSolicitudAndPerfilOrigen_IdPerfil(
                        idSolicitud,
                        idAutor);
        if (repetida) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya has valorado esta solicitud");
        }

        //identificar a la persona que recibe la valoración
        Perfil destinatario;
        if (idAutor.equals(idOrigen)) {
            destinatario = solicitud.getPerfilDestino();
        } else {
            destinatario = solicitud.getPerfilOrigen();
        }

        //crear la valoración
        Valoracion valoracion = new Valoracion();
        valoracion.setSolicitud(solicitud);
        valoracion.setPerfilOrigen(autor);
        valoracion.setPerfilDestino(destinatario);
        valoracion.setPuntuacion(dto.puntuacion());
        valoracion.setComentario(dto.comentario());

        //guardar en PostgreSQL
        Valoracion guardada = valoracionRepository.save(valoracion);
        return guardada.getIdValoracion();
    }
}
