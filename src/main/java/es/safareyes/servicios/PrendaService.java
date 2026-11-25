package es.safareyes.servicios;

import es.safareyes.Interfaces.IPrendaRepository;
import es.safareyes.Interfaces.ICategoriaRepository;
import es.safareyes.Interfaces.IPerfilRepository;
import es.safareyes.Interfaces.ISolicitudRepository;
import es.safareyes.modelos.Solicitud;
import es.safareyes.conversores.PrendaMapper;

import es.safareyes.Interfaces.IFotoRepository;
import es.safareyes.modelos.Foto;
import es.safareyes.modelos.EstadoPublicacionPrenda;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import es.safareyes.dtos.PrendaDTO;
import es.safareyes.dtos.PrendaDetalleDTO;
import es.safareyes.dtos.CrearPrendaDTO;
import es.safareyes.dtos.EditarPrendaDTO;
import es.safareyes.dtos.ConsultaPrendasPagProyeccion;

import es.safareyes.excepciones.ElementoNoEncontrado;
import es.safareyes.modelos.Prenda;
import es.safareyes.modelos.EstadoPublicacionPrenda;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrendaService {

    private final IPrendaRepository repository;
    private final ICategoriaRepository categoriaRepository;
    private final IPerfilRepository perfilRepository;
    private final PrendaMapper mapper;
    private final ISolicitudRepository solicitudRepository;
    private final IFotoRepository fotoRepository;

    // 1. GET - Listar prendas
    public Page<PrendaDTO> consultarPrendas(
            Long categoria,
            String texto,
            String talla,
            Pageable pageable) {

        Page<ConsultaPrendasPagProyeccion> prendas =
                repository.obtenerPrendasPaginadas(
                        categoria, texto, talla, pageable
                );
        return prendas.map(mapper::toDTO);
    }

    // 2. GET - Consultar detalle de una prenda
    public PrendaDetalleDTO consultarDetalle(Long id) {
        return repository.obtenerDetallePrenda(id)
                .map(mapper::toDetalleDTO)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No se ha encontrado la prenda con ID " + id));
    }

    // 3. POST - Crear una prenda
    @Transactional
    public Long crearPrenda(CrearPrendaDTO dto) {
        Prenda prenda = mapper.toEntity(dto);
        prenda.setCategoria(
                categoriaRepository.findById(dto.idCategoria())
                        .orElseThrow(() -> new ElementoNoEncontrado(
                                "No existe la categoría indicada"))
        );

        prenda.setPerfil(
                perfilRepository.findById(dto.idPerfil())
                        .orElseThrow(() -> new ElementoNoEncontrado(
                                "No existe el perfil indicado"))
        );

        prenda.setEstadoPublicacion(EstadoPublicacionPrenda.BORRADOR);
        prenda.setDisponibilidad(false);
        prenda.setPermitePrestamo(Boolean.TRUE.equals(dto.permitePrestamo()));
        prenda.setPermiteIntercambio(Boolean.TRUE.equals(dto.permiteIntercambio()));
        return repository.save(prenda).getIdPrenda();
    }

    // 4. PUT - Modificar una prenda
    @Transactional
    public void editarPrenda(Long id, EditarPrendaDTO dto) {
        //buscar la prenda existente
        Prenda prenda = repository.findById(id)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No se ha encontrado la prenda con ID " + id));

        //actualizar los datos recibidos
        mapper.actualizar(prenda, dto);

        //cambiar la categoría solo si se ha enviado otra
        if (dto.idCategoria() != null) {
            prenda.setCategoria(
                    categoriaRepository.findById(dto.idCategoria())
                            .orElseThrow(() -> new ElementoNoEncontrado(
                                    "No existe la categoría indicada"))
            );
        }
        // Guardar los cambios
        repository.save(prenda);
    }

    // 5. DELETE - Retirar una prenda (baja lógica)
    @Transactional
    public void retirarPrenda(Long id) {

        //buscar la prenda por su ID
        Prenda prenda = repository.findById(id)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No se ha encontrado la prenda con ID " + id));

        //cambiar su estado a RETIRADA
        prenda.setEstadoPublicacion(
                EstadoPublicacionPrenda.RETIRADA);

        //la prenda deja de estar disponible
        prenda.setDisponibilidad(false);

        //cancelar solicitudes pendientes donde se pide esta prenda
        for (Solicitud solicitud : solicitudRepository
                .findByPrendaSolicitada_IdPrendaAndEstado(id, "Pendiente")) {
            solicitud.setEstado("Cancelada");
            solicitudRepository.save(solicitud);
        }

        //cancelar solicitudes pendientes donde se ofrece esta prenda
        for (Solicitud solicitud : solicitudRepository
                .findByPrendaOfrecida_IdPrendaAndEstado(id, "Pendiente")) {
            solicitud.setEstado("Cancelada");
            solicitudRepository.save(solicitud);
        }
        //guardar los cambios en PostgreSQL
        repository.save(prenda);
    }


    @Transactional
    public void agregarFoto(Long idPrenda, String url) {

        // Buscar la prenda
        Prenda prenda = repository.findById(idPrenda)
                .orElseThrow(() -> new ElementoNoEncontrado(
                        "No existe la prenda con ID " + idPrenda));

        // No permitir fotos en prendas retiradas
        if (prenda.getEstadoPublicacion() ==
                EstadoPublicacionPrenda.RETIRADA) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "No puedes añadir fotos a una prenda retirada"
            );
        }

        // Contar las fotos actuales
        long cantidad = fotoRepository.countByPrenda_IdPrenda(idPrenda);

        // Máximo 5 fotos
        if (cantidad >= 5) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Una prenda no puede tener más de 5 fotografías"
            );
        }

        // Crear la fotografía
        Foto foto = new Foto();
        foto.setUrl(url);
        foto.setOrden((int) cantidad + 1);
        foto.setPrenda(prenda);
        fotoRepository.save(foto);

        // Primera foto: publicar la prenda
        if (cantidad == 0 &&
                prenda.getEstadoPublicacion() ==
                        EstadoPublicacionPrenda.BORRADOR) {
            prenda.setEstadoPublicacion(EstadoPublicacionPrenda.DISPONIBLE);
            prenda.setDisponibilidad(true);
            repository.save(prenda);
        }
    }

}




