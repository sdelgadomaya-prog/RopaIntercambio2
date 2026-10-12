package es.safareyes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import es.safareyes.dta.ConsultaSolicitudProyeccion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.safareyes.Interfaces.ISolicitudRepository;
import es.safareyes.modelos.Solicitud;

@SpringBootTest
@Transactional // perfiles y prendas son LAZY
class TestRepositorioSolicitud {

    @Autowired
    private ISolicitudRepository repository;

    @Test
    void consultarTodos() {
        List<Solicitud> todas = repository.findAll();
        assertThat(todas).isNotNull(); // breakpoint aquí
    }

    //consultaº6

    @Test
    void consultarSolicitudesEnviadas() {
        List<ConsultaSolicitudProyeccion> lista = repository.obtenerSolicitudesEnviadas(2L, null);
        System.out.println(" SOLICITUDES ENVIADAS (" + lista.size() + ") ");
        lista.forEach(s -> System.out.println(s.getIdSolicitud() + " | " + s.getTipoSolicitud()
                + " | " + s.getEstado() + " | de " + s.getIdPerfilOrigen() + " para " + s.getIdPerfilDestino()));
    }

    @Test
    void consultarSolicitudesRecibidas() {
        List<ConsultaSolicitudProyeccion> lista = repository.obtenerSolicitudesRecibidas(1L, null);
        System.out.println("SOLICITUDES RECIBIDAS (" + lista.size() + ") ");
        lista.forEach(s -> System.out.println(s.getIdSolicitud() + " | " + s.getTipoSolicitud()
                + " | " + s.getEstado() + " | de " + s.getIdPerfilOrigen() + " para " + s.getIdPerfilDestino()));
    }
}