package es.safareyes.controladores;

import es.safareyes.Interfaces.IUsuarioRepository;
import es.safareyes.dtos.InformeActividadProyeccion;
import es.safareyes.excepciones.ElementoNoEncontrado;
import es.safareyes.modelos.Usuario;
import es.safareyes.servicios.InformeService;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/informes")
@RequiredArgsConstructor
public class InformeController {

    private final InformeService informeService;
    private final IUsuarioRepository usuarioRepository;

    //16. GET - Consultar informe de actividad
    @GetMapping("/actividad")
    public ResponseEntity<List<InformeActividadProyeccion>> consultarActividad(
            @RequestParam Long idUsuario,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta) {

        //buscamos al usuario
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ElementoNoEncontrado("No existe el usuario"));

        //comprobamos los permisos
        String rol = usuario.getRol();

        if (!"ADMIN".equalsIgnoreCase(rol)
                && !"MODERADOR".equalsIgnoreCase(rol)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo ADMIN o MODERADOR pueden consultar informes");
        }

        //tenemos el informe
        return ResponseEntity.ok(
                informeService.obtenerInformeActividad(desde, hasta));
    }
}