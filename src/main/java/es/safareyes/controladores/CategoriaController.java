package es.safareyes.controladores;

import es.safareyes.dtos.CategoriaDTO;
import es.safareyes.servicios.CategoriaService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> consultarCategorias() {
        List<CategoriaDTO> categorias =
                categoriaService.consultarTodas();
        return ResponseEntity.ok(categorias);
    }
}