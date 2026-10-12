package es.safareyes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.List;

import es.safareyes.dta.ConsultaCategoriaCantidadProyeccion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.safareyes.Interfaces.ICategoriaRepository;
import es.safareyes.modelos.Categoria;

@SpringBootTest
@Transactional
class TestRepositorioCategoria {

    @Autowired
    private ICategoriaRepository repository;

    @Test
    void consultarTodos() {
        List<Categoria> todas = repository.findAll();
        assertThat(todas).isNotNull(); // breakpoint aquí para ver "todas"
    }

    //3ºconsulta test
    @Test
    void consultarCategoriasConCantidad() {
        List<ConsultaCategoriaCantidadProyeccion> lista = repository.obtenerCategoriasConCantidad();
        assertFalse(lista.isEmpty());

        System.out.println(" CATEGORÍAS CON PRENDAS DISPONIBLES");
        lista.forEach(c -> System.out.println(
                c.getIdCategoria() + " | " + c.getNombre() + " | " + c.getPrendasDisponibles()));
    }
}