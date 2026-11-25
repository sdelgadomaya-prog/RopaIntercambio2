package es.safareyes.servicios;

import es.safareyes.Interfaces.IPrendaRepository;
import es.safareyes.dtos.InformeActividadProyeccion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InformeService {
    private final IPrendaRepository prendaRepository;

    // 16. GET - Informe de actividad
    @Transactional(readOnly = true)
    public List<InformeActividadProyeccion> obtenerInformeActividad(
            LocalDate desde,
            LocalDate hasta) {

        //comprobar que se indican las dos fechas
        if (desde == null || hasta == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes indicar las fechas desde y hasta");
        }

        //comprobar que la fecha inicial no sea posterior
        if (desde.isAfter(hasta)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha desde no puede ser posterior a hasta");
        }

        //obtener el informe desde PostgreSQL
        return prendaRepository.obtenerInformeActividad(desde, hasta);
    }
}
