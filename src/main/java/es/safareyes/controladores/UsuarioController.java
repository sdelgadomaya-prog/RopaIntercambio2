package es.safareyes.controladores;

import es.safareyes.dtos.PerfilDTO;
import es.safareyes.dtos.EditarPerfilDTO;
import es.safareyes.dtos.PerfilAfinDTO;
import es.safareyes.servicios.PerfilService;

import es.safareyes.dtos.RegistroUsuarioDTO;
import es.safareyes.servicios.RegistroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final PerfilService service;
    private final RegistroService registroService;


    // 7. GET - Consultar perfil público
    @GetMapping("/{id}")
    public ResponseEntity<PerfilDTO> consultarPerfil(@PathVariable Long id) {
        return ResponseEntity.ok(service.consultarPerfil(id));
    }

    // 8. PUT - Editar mi perfil
    @PutMapping("/me")
    public ResponseEntity<Void> editarMiPerfil(@RequestParam Long idUsuario, @Valid @RequestBody EditarPerfilDTO dto) {
        service.editarPerfil(idUsuario, dto);
        return ResponseEntity.noContent().build();
    }

    // 9. GET - Consultar perfiles afines
    @GetMapping("/afines")
    public ResponseEntity<List<PerfilAfinDTO>> consultarAfines(@RequestParam Long idUsuario) {
        return ResponseEntity.ok(service.consultarPerfilesAfines(idUsuario));
    }


    //registrar un nuevo usuario
    @PostMapping("/registro")
    public ResponseEntity<Long> registrarUsuario(
            @Valid @RequestBody RegistroUsuarioDTO dto) {
        Long id = registroService.registrar(dto);
        return ResponseEntity.status(201).body(id);
    }

}


