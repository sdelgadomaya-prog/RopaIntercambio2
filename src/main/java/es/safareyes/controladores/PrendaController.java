package es.safareyes.controladores;
import es.safareyes.dtos.PrendaCreadaDTO;
import es.safareyes.dtos.PrendaDTO;
import es.safareyes.dtos.PrendaDetalleDTO;
import es.safareyes.dtos.CrearPrendaDTO;
import es.safareyes.dtos.EditarPrendaDTO;
import es.safareyes.servicios.PrendaService;

import es.safareyes.dtos.AgregarFotoDTO;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/prendas")
@AllArgsConstructor
public class PrendaController {
    private PrendaService service;

    // 1. GET - Listar prendas con filtros y paginación
    @GetMapping
    public ResponseEntity<Page<PrendaDTO>> consultarPrendas(
            @RequestParam(required = false) Long categoria,
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String talla,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(
                service.consultarPrendas(categoria, texto, talla, pageable));
    }

    // 2. GET - Consultar detalle de una prenda
    @GetMapping("/{id}")
    public ResponseEntity<PrendaDetalleDTO> consultarDetalle(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                service.consultarDetalle(id));
    }

    // 3. POST - Crear una prenda
    @PostMapping
    public ResponseEntity<PrendaCreadaDTO> crearPrenda(
            @Valid @RequestBody CrearPrendaDTO dto) {

        Long id = service.crearPrenda(dto);
        PrendaCreadaDTO respuesta = new PrendaCreadaDTO(
                "Prenda creada correctamente",
                id,
                "BORRADOR");
        return ResponseEntity.created(
                URI.create("/api/v1/prendas/" + id)).body(respuesta);
    }

    // 4. PUT - Modificar una prenda
    @PutMapping("/{id}")
    public ResponseEntity<Void> editarPrenda(
            @PathVariable Long id,
            @Valid @RequestBody EditarPrendaDTO dto) {
        service.editarPrenda(id, dto);
        return ResponseEntity.noContent().build();
    }

    // 5. DELETE - Retirar una prenda
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> retirarPrenda(
            @PathVariable Long id) {
        service.retirarPrenda(id);
        return ResponseEntity.noContent().build();
    }

    // Añadir una fotografía a una prenda
    @PostMapping("/{id}/fotos")
    public ResponseEntity<Void> agregarFoto(
            @PathVariable Long id,
            @Valid @RequestBody AgregarFotoDTO dto) {

        service.agregarFoto(id, dto.url());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}