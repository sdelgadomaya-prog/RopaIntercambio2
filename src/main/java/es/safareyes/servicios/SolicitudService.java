package es.safareyes.servicios;

import es.safareyes.Interfaces.IPerfilRepository;
import es.safareyes.dtos.CambiarEstadoSolicitudDTO;
import java.util.ArrayList;
import java.time.LocalDateTime;
import es.safareyes.dtos.ConsultaSolicitudProyeccion;
import es.safareyes.Interfaces.IPrendaRepository;
import es.safareyes.Interfaces.ISolicitudRepository;
import es.safareyes.conversores.SolicitudMapper;
import es.safareyes.dtos.CrearSolicitudDTO;
import es.safareyes.dtos.SolicitudDTO;
import es.safareyes.excepciones.ElementoNoEncontrado;
import es.safareyes.modelos.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudService {
    private final ISolicitudRepository repository;
    private final IPrendaRepository prendaRepository;
    private final IPerfilRepository perfilRepository;
    private final SolicitudMapper mapper;

    //10 POST - Crear una solicitud
    @Transactional
    public SolicitudDTO crearSolicitud(
            Long idUsuario,
            CrearSolicitudDTO dto)
    {
        //buscar el perfil de quien solicita
        Perfil origen = perfilRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No existe el perfil del usuario"));

        //buscar la prenda solicitada
        Prenda solicitada = prendaRepository.findById(
                dto.idPrendaSolicitada()).orElseThrow(() -> new ElementoNoEncontrado(
                "No existe la prenda solicitada"));

        //comprobar que no sea nuestra
        if (solicitada.getPerfil().getIdPerfil()
                .equals(origen.getIdPerfil())) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "No puedes solicitar tu propia prenda");
        }

        //comprobar que esté disponible
        comprobarDisponible(solicitada);

        //comprobar que no exista otra solicitud pendiente
        boolean repetida = repository
                .existsByPerfilOrigen_IdPerfilAndPrendaSolicitada_IdPrendaAndEstado(
                        origen.getIdPerfil(),
                        solicitada.getIdPrenda(),
                        "Pendiente");
        if (repetida) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya tienes una solicitud pendiente para esta prenda");
        }

        // 6. Convertir los datos en una solicitud
        Solicitud solicitud = mapper.toEntity(dto);
        solicitud.setPerfilOrigen(origen);
        solicitud.setPerfilDestino(solicitada.getPerfil());
        solicitud.setPrendaSolicitada(solicitada);
        solicitud.setEstado("Pendiente");

        //comprobar el tipo de solicitud
        if (dto.tipoSolicitud().equals("Intercambio")) {

            //comprobar que acepta intercambios
            if (!Boolean.TRUE.equals(
                    solicitada.getPermiteIntercambio())) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "La prenda no admite intercambios");
            }

            if (dto.idPrendaOfrecida() == null
                    || dto.fechaInicio() == null
                    || dto.fechaFin() == null) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "El intercambio necesita una prenda ofrecida y dos fechas");
            }

//comprobamos que las fechas sean correctas
            if (dto.fechaInicio().isBefore(LocalDate.now())
                    || !dto.fechaFin().isAfter(dto.fechaInicio())) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Las fechas del intercambio no son válidas");
            }

            Prenda ofrecida = prendaRepository.findById(
                    dto.idPrendaOfrecida()).orElseThrow(() -> new ElementoNoEncontrado(
                    "No existe la prenda ofrecida"));

            //la prenda ofrecida debe ser nuestra
            if (!ofrecida.getPerfil().getIdPerfil()
                    .equals(origen.getIdPerfil())) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "La prenda ofrecida no es tuya");
            }

            comprobarDisponible(ofrecida);
            solicitud.setPrendaOfrecida(ofrecida);
            solicitud.getPrendas().add(ofrecida);

        } else if (dto.tipoSolicitud().equals("Prestamo")) {
            // Comprobar que admite préstamos
            if (!Boolean.TRUE.equals(
                    solicitada.getPermitePrestamo())) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "La prenda no admite préstamos");
            }

            // Un préstamo necesita fechas, no otra prenda
            if (dto.fechaInicio() == null
                    || dto.fechaFin() == null
                    || dto.idPrendaOfrecida() != null) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "El préstamo necesita fechas y no una prenda ofrecida");
            }

            LocalDate hoy = LocalDate.now(
                    ZoneId.of("Europe/Madrid"));

            long dias = ChronoUnit.DAYS.between(
                    dto.fechaInicio(),
                    dto.fechaFin());
            // Comprobar las fechas
            if (dto.fechaInicio().isBefore(hoy)
                    || dias < 1
                    || dias > 30) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Las fechas deben ser válidas y el préstamo no puede superar 30 días");
            }

            // Máximo de 3 préstamos activos
            long activos = repository
                    .countByPerfilOrigen_IdPerfilAndTipoSolicitudAndEstadoIn(
                            origen.getIdPerfil(),
                            "Prestamo",
                            List.of("Aceptada", "Retrasada"));
            if (activos >= 3) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Ya tienes 3 préstamos activos");
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Tipo de solicitud no válido");
        }

        //mantener la relación antigua
        solicitud.getPrendas().add(solicitada);
        //guardar en PostgreSQL
        Solicitud guardada = repository.save(solicitud);
        //devolver el DTO de respuesta
        return mapper.toDTO(guardada);
    }


    //11 GET - Consultar solicitudes
    @Transactional(readOnly = true)
    public List<ConsultaSolicitudProyeccion> consultarSolicitudes(
            Long idUsuario,
            String bandeja,
            String estado) {

        //buscar el perfil del usuario
        Perfil perfil = perfilRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No existe el perfil del usuario"));

        // Solicitudes que hemos enviado
        if (bandeja.equalsIgnoreCase("enviadas")) {
            return repository.obtenerSolicitudesEnviadas(
                    perfil.getIdPerfil(), estado);
        }

        // Solicitudes que hemos recibido
        if (bandeja.equalsIgnoreCase("recibidas")) {
            return repository.obtenerSolicitudesRecibidas(
                    perfil.getIdPerfil(), estado);
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La bandeja debe ser enviadas o recibidas");
    }


    // 12. PATCH - Cambiar el estado de una solicitud
    @Transactional
    public SolicitudDTO cambiarEstado(
            Long idUsuario,
            Long idSolicitud,
            CambiarEstadoSolicitudDTO dto) {

        //buscar al usuario
        Perfil perfil = perfilRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No existe el perfil del usuario"));

        //buscar la solicitud
        Solicitud solicitud = repository.findById(idSolicitud)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No existe la solicitud"));

        String estadoActual = solicitud.getEstado();
        String nuevoEstado = dto.estado().trim();

        //comprobar quién realiza la operación
        boolean esSolicitante = solicitud.getPerfilOrigen()
                .getIdPerfil().equals(perfil.getIdPerfil());
        boolean esPropietario = solicitud.getPerfilDestino()
                .getIdPerfil().equals(perfil.getIdPerfil());
        if (!esSolicitante && !esPropietario) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No participas en esta solicitud");
        }

        // ACEPTAR SOLICITUD
        if (estadoActual.equals("Pendiente")
                && nuevoEstado.equalsIgnoreCase("Aceptada")) {

            if (!esPropietario) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Solo el propietario puede aceptar");
            }

            // Comprobar que las prendas siguen disponibles
            comprobarDisponible(solicitud.getPrendaSolicitada());
            if (solicitud.getPrendaOfrecida() != null) {
                comprobarDisponible(solicitud.getPrendaOfrecida());
            }
            solicitud.setEstado("Aceptada");
            solicitud.setFechaRespuesta(LocalDateTime.now());
            // La prenda deja de estar disponible
            Prenda prenda = solicitud.getPrendaSolicitada();
            prenda.setDisponibilidad(false);

            if (solicitud.getTipoSolicitud().equals("Intercambio")) {
                prenda.setEstadoPublicacion(
                        EstadoPublicacionPrenda.RESERVADA);

                // También reservamos la prenda ofrecida
                Prenda ofrecida = solicitud.getPrendaOfrecida();
                ofrecida.setDisponibilidad(false);
                ofrecida.setEstadoPublicacion(
                        EstadoPublicacionPrenda.RESERVADA);
                prendaRepository.save(ofrecida);

            } else {
                prenda.setEstadoPublicacion(
                        EstadoPublicacionPrenda.PRESTADA);
            }
            prendaRepository.save(prenda);

            // Rechazar las otras solicitudes pendientes
            List<Solicitud> otras = repository
                    .findByPrendaSolicitada_IdPrendaAndEstado(
                            prenda.getIdPrenda(),
                            "Pendiente");

            for (Solicitud otra : otras) {
                if (!otra.getIdSolicitud().equals(idSolicitud)) {
                    otra.setEstado("Rechazada");
                    otra.setFechaRespuesta(LocalDateTime.now());
                    repository.save(otra);
                }
            }

            // Buscar otras solicitudes relacionadas con las prendas reservadas
            List<Solicitud> pendientes = new ArrayList<>();

            // Solicitudes donde la prenda solicitada se ofreció a cambio
            pendientes.addAll(
                    repository.findByPrendaOfrecida_IdPrendaAndEstado(
                            prenda.getIdPrenda(), "Pendiente"));

            // En los intercambios también hay que revisar la prenda ofrecida
            if (solicitud.getPrendaOfrecida() != null) {
                Long idOfrecida = solicitud.getPrendaOfrecida().getIdPrenda();
                pendientes.addAll(
                        repository.findByPrendaSolicitada_IdPrendaAndEstado(
                                idOfrecida, "Pendiente"));

                pendientes.addAll(
                        repository.findByPrendaOfrecida_IdPrendaAndEstado(
                                idOfrecida, "Pendiente"));
            }

            // Rechazar las demás solicitudes pendientes
            for (Solicitud otra : pendientes) {
                if (!otra.getIdSolicitud().equals(idSolicitud)) {
                    otra.setEstado("Rechazada");
                    otra.setFechaRespuesta(LocalDateTime.now());
                }
            }
            repository.saveAll(pendientes);

            // RECHAZAR SOLICITUD
        } else if (estadoActual.equals("Pendiente")
                && nuevoEstado.equalsIgnoreCase("Rechazada")) {
            if (!esPropietario) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Solo el propietario puede rechazar");
            }
            solicitud.setEstado("Rechazada");
            solicitud.setFechaRespuesta(LocalDateTime.now());

            // CANCELAR SOLICITUD
        } else if (estadoActual.equals("Pendiente")
                && nuevoEstado.equalsIgnoreCase("Cancelada")) {
            if (!esSolicitante) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Solo el solicitante puede cancelar");
            }

            solicitud.setEstado("Cancelada");

            // FINALIZAR SOLICITUD
        } else if ((estadoActual.equals("Aceptada")
                || estadoActual.equals("Retrasada"))
                && nuevoEstado.equalsIgnoreCase("Finalizada")) {
            if (!esPropietario) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Solo el propietario puede finalizar");
            }

            solicitud.setEstado("Finalizada");

            // La prenda vuelve a estar disponible
            Prenda prenda = solicitud.getPrendaSolicitada();
            prenda.setDisponibilidad(true);
            prenda.setEstadoPublicacion(
                    EstadoPublicacionPrenda.DISPONIBLE);
            prendaRepository.save(prenda);

            // En intercambios se devuelve también la ofrecida
            if (solicitud.getPrendaOfrecida() != null) {
                Prenda ofrecida = solicitud.getPrendaOfrecida();
                ofrecida.setDisponibilidad(true);
                ofrecida.setEstadoPublicacion(
                        EstadoPublicacionPrenda.DISPONIBLE);
                prendaRepository.save(ofrecida);
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "No se permite ese cambio de estado");
        }
        Solicitud guardada = repository.save(solicitud);
        return mapper.toDTO(guardada);
    }

    //el metodo auxiliar para comprobar la disponibilidad
    private void comprobarDisponible(Prenda prenda) {
        if (!Boolean.TRUE.equals(prenda.getDisponibilidad())
                || prenda.getEstadoPublicacion()
                != EstadoPublicacionPrenda.DISPONIBLE) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "La prenda no está disponible");
        }
    }
}