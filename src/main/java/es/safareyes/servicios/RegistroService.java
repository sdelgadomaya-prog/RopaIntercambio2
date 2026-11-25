
package es.safareyes.servicios;

import es.safareyes.Interfaces.IPerfilRepository;
import es.safareyes.Interfaces.IUsuarioRepository;
import es.safareyes.dtos.RegistroUsuarioDTO;
import es.safareyes.modelos.Perfil;
import es.safareyes.modelos.Usuario;

import es.safareyes.Interfaces.IEstiloRepository;
import es.safareyes.modelos.Estilo;
import java.util.List;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RegistroService {
    private final IUsuarioRepository usuarioRepository;
    private final IPerfilRepository perfilRepository;
    private final IEstiloRepository estiloRepository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Transactional
    public Long registrar(RegistroUsuarioDTO dto) {
        //comprobar que tiene al menos 16 años
        if (dto.getFechaNacimiento() == null ||
                dto.getFechaNacimiento().isAfter(
                        LocalDate.now().minusYears(16))) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Debes tener al menos 16 años"
            );
        }
        String username = dto.getUsername().trim();
        String email = dto.getEmail().trim()
                .toLowerCase(Locale.ROOT);

        //comprobar que el nombre de usuario no existe
        if (usuarioRepository.existsByUsernameIgnoreCase(username)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El nombre de usuario ya existe");
        }

        //comprobar que el correo no existe
        if (perfilRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El correo electrónico ya existe");
        }


        // Comprobar que no hay estilos repetidos
        List<Long> ids = dto.getIdsEstilos();

        if (new HashSet<>(ids).size() != ids.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No puedes seleccionar estilos repetidos"
            );
        }

        // Comprobar que los estilos existen
        List<Estilo> estilos = estiloRepository.findAllById(ids);
        if (estilos.size() != ids.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Uno o varios estilos no existen");
        }


        //crear usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setRol("usuario");
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuarioRepository.save(usuario);

        //crear perfil
        Perfil perfil = new Perfil();
        perfil.setNombre(dto.getNombre());
        perfil.setApellido1(dto.getApellido1());
        perfil.setApellido2(dto.getApellido2());
        perfil.setEmail(email);
        perfil.setFechaNacimiento(dto.getFechaNacimiento());
        perfil.setUsuario(usuario);
        perfil.setEstilos(new HashSet<>(estilos));
        perfilRepository.save(perfil);
        return usuario.getId();
    }
}