package es.safareyes;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.safareyes.dta.ConsultaPrendaDetalleProyeccion;
import es.safareyes.dta.ConsultaPrendasPagProyeccion;
import es.safareyes.dta.InformeActividadProyeccion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import es.safareyes.Interfaces.IPrendaRepository;
import es.safareyes.modelos.Prenda;

@SpringBootTest
@Transactional
class TestRepositorioPrenda {

    @Autowired
    private IPrendaRepository repository;

    @Test
    void consultarTodos() {
        List<Prenda> todas = repository.findAll();
        assertThat(todas).isNotNull();
    }


    //1ºconsulta
    @Test
    void consultarPrendasPaginadas() {
        Page<ConsultaPrendasPagProyeccion> pagina =
                repository.obtenerPrendasPaginadas(null, null, null, PageRequest.of(0, 10));

        System.out.println("Total: " + pagina.getTotalElements());
        pagina.forEach(p -> System.out.println(
                p.getIdPrenda() + " - " + p.getTitulo() + " - " + p.getCategoria()
                        + " - " + p.getPropietaria()));          // breakpoint aquí

        assertThat(pagina).isNotNull();
    }



    //2ºconsulta
    @Test
    void consultarDetallePrenda() {
        Optional<ConsultaPrendaDetalleProyeccion> detalle = repository.obtenerDetallePrenda(1L);
        assertTrue(detalle.isPresent());
        System.out.println(detalle.get().getTitulo() + " | " + detalle.get().getPropietario()
                + " | " + detalle.get().getMediaPuntuacion() + " | " + detalle.get().getFotos());
        System.out.println("Marca: " + detalle.get().getMarca() + " | Talla: " + detalle.get().getTalla());

    }

    //consulta nº8
    @Test
    void consultarInformeActividad() {
        List<InformeActividadProyeccion> lista = repository.obtenerInformeActividad(
                LocalDate.of(2020, 1, 1), LocalDate.now());

        System.out.println(" INFORME DE ACTIVIDAD ");
        lista.forEach(i -> System.out.println(i.getMes() + " | " + i.getCategoria()
                + " | publicaciones: " + i.getPublicaciones()
                + " | intercambios: " + i.getIntercambios()
                + " | préstamos: " + i.getPrestamos()));
    }
}