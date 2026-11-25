package es.safareyes.servicios;

import es.safareyes.Interfaces.IConversacionRepository;
import es.safareyes.Interfaces.IMensajeRepository;
import es.safareyes.dtos.ConsultarMensajeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import es.safareyes.Interfaces.IPerfilRepository;
import es.safareyes.Interfaces.ISolicitudRepository;

import es.safareyes.dtos.EnviarMensajeDTO;
import es.safareyes.excepciones.ElementoNoEncontrado;
import es.safareyes.modelos.Conversacion;
import es.safareyes.modelos.Mensaje;
import es.safareyes.modelos.Perfil;
import es.safareyes.modelos.Solicitud;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MensajeService {
    private final IMensajeRepository mensajeRepository;
    private final IConversacionRepository conversacionRepository;
    private final ISolicitudRepository solicitudRepository;
    private final IPerfilRepository perfilRepository;

    // 13. POST - Enviar un mensaje
    @Transactional
    public Long enviarMensaje(
            Long idUsuario,
            Long idSolicitud,
            EnviarMensajeDTO dto) {
        Perfil remitente = perfilRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No existe el perfil del usuario"
                ));

        //se busca la solicitud
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No existe la solicitud"));

        //comprobar que participa en la solicitud
        Long idRemitente = remitente.getIdPerfil();
        Long idOrigen = solicitud.getPerfilOrigen().getIdPerfil();
        Long idDestino = solicitud.getPerfilDestino().getIdPerfil();

        if (!idRemitente.equals(idOrigen)
                && !idRemitente.equals(idDestino)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No participas en esta solicitud"
            );
        }

        //comprobar el estado de la solicitud
        String estado = solicitud.getEstado();
        if (estado.equalsIgnoreCase("Rechazada")
                || estado.equalsIgnoreCase("Cancelada")
                || estado.equalsIgnoreCase("Caducada")) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "No se pueden enviar mensajes en esta solicitud");
        }

        //buscar una conversación entre ambos perfiles
        Conversacion conversacion = conversacionRepository
                .buscarConversacion(idOrigen, idDestino)
                .orElse(null);

        //si no existe, crearla
        if (conversacion == null) {
            conversacion = new Conversacion();
            conversacion.setPerfil1(solicitud.getPerfilOrigen());
            conversacion.setPerfil2(solicitud.getPerfilDestino());
            conversacion.setEstado("Activa");
            conversacion = conversacionRepository.save(conversacion);
        }

        //se crear el mensaje
        Mensaje mensaje = new Mensaje();
        mensaje.setContenido(dto.contenido().trim());
        mensaje.setRemitente(remitente);
        mensaje.setConversacion(conversacion);
        mensaje.setSolicitud(solicitud);

        //guardar en PostgreSQL
        Mensaje guardado = mensajeRepository.save(mensaje);
        return guardado.getIdMensaje();
    }

    // 14. GET - Consultar mensajes de una solicitud
    @Transactional(readOnly = true)
    public Page<ConsultarMensajeDTO> consultarMensajes(
            Long idUsuario,
            Long idSolicitud,
            int page,
            int size) {

        //comprobar que la página y el tamaño sean correctos
        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La página debe ser 0 o mayor y el tamaño entre 1 y 100");
        }

        //buscar el perfil del usuario
        Perfil perfil = perfilRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No existe el perfil del usuario"));

        //buscar la solicitud
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No existe la solicitud"));

        //comprobar que el usuario participa
        Long idPerfil = perfil.getIdPerfil();
        Long idOrigen = solicitud.getPerfilOrigen().getIdPerfil();
        Long idDestino = solicitud.getPerfilDestino().getIdPerfil();

        if (!idPerfil.equals(idOrigen)
                && !idPerfil.equals(idDestino)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No puedes consultar los mensajes de esta solicitud");
        }

        // Consultar los mensajes con paginación
        Page<Mensaje> mensajes = mensajeRepository
                .findBySolicitud_IdSolicitudOrderByFechaEnvioDescHoraEnvioDesc(
                        idSolicitud,
                        PageRequest.of(page, size));

        // convertir los mensajes a DTO
        return mensajes.map(mensaje -> new ConsultarMensajeDTO(
                mensaje.getIdMensaje(),
                mensaje.getRemitente().getIdPerfil(),
                mensaje.getContenido(),
                mensaje.getFechaEnvio(),
                mensaje.getHoraEnvio()));
    }

}
