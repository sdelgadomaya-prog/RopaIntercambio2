package es.safareyes.controladores;

import es.safareyes.dtos.CrearSolicitudDTO;
import es.safareyes.dtos.CrearValoracionDTO;
import es.safareyes.servicios.ValoracionService;
import es.safareyes.dtos.ConsultarMensajeDTO;
import org.springframework.data.domain.Page;
import es.safareyes.dtos.EnviarMensajeDTO;
import es.safareyes.servicios.MensajeService;
import org.springframework.http.HttpStatus;
import java.util.Map;
import es.safareyes.dtos.CambiarEstadoSolicitudDTO;
import es.safareyes.dtos.SolicitudDTO;
import es.safareyes.servicios.SolicitudService;
import es.safareyes.dtos.ConsultaSolicitudProyeccion;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {
    private final SolicitudService service;
    private final MensajeService mensajeService;
    private final ValoracionService valoracionService;

    // 10. POST - Crear una solicitud
    @PostMapping
    public ResponseEntity<SolicitudDTO> crearSolicitud(
            @RequestParam Long idUsuario,
            @Valid @RequestBody CrearSolicitudDTO dto) {
        SolicitudDTO resultado = service.crearSolicitud(idUsuario, dto);
        return ResponseEntity.created(URI.create("/api/v1/solicitudes/" + resultado.idSolicitud())).body(resultado);
    }

    // 11. GET - Consultar solicitudes enviadas o recibidas
    @GetMapping
    public ResponseEntity<List<ConsultaSolicitudProyeccion>> consultarSolicitudes(
            @RequestParam Long idUsuario,
            @RequestParam String bandeja,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(service.consultarSolicitudes(idUsuario, bandeja, estado));
    }

    // 12. PATCH - Cambiar estado de una solicitud
    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Long idUsuario,
            @Valid @RequestBody CambiarEstadoSolicitudDTO dto) {

        SolicitudDTO resultado = service.cambiarEstado(idUsuario, id, dto);
        return ResponseEntity.ok(resultado);
    }

    // 13. POST - Enviar mensaje
    @PostMapping("/{id}/mensajes")
    public ResponseEntity<Map<String, Object>> enviarMensaje(
            @PathVariable Long id,
            @RequestParam Long idUsuario,
            @Valid @RequestBody EnviarMensajeDTO dto) {
        Long idMensaje = mensajeService.enviarMensaje(idUsuario, id, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "mensaje", "Mensaje enviado correctamente",
                        "idMensaje", idMensaje
                ));
    }

    // 14. GET - Consultar mensajes de una solicitud
    @GetMapping("/{id}/mensajes")
    public ResponseEntity<Page<ConsultarMensajeDTO>> consultarMensajes(
            @PathVariable Long id,
            @RequestParam Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ConsultarMensajeDTO> mensajes = mensajeService.consultarMensajes(idUsuario, id, page, size);
        return ResponseEntity.ok(mensajes);
    }

    // 15. POST - Crear una valoración
    @PostMapping("/{id}/valoraciones")
    public ResponseEntity<Map<String, Object>> crearValoracion(
            @PathVariable Long id,
            @RequestParam Long idUsuario,
            @Valid @RequestBody CrearValoracionDTO dto) {
        Long idValoracion = valoracionService.crearValoracion(idUsuario, id, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "mensaje", "Valoración creada correctamente",
                        "idValoracion", idValoracion
                ));
    }
}