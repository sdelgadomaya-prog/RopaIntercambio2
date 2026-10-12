package es.safareyes;

import es.safareyes.Interfaces.IPerfilRepository;
import es.safareyes.dta.ConsultaPerfilAfinProyeccion;
import es.safareyes.dta.ConsultaPerfilProyeccion;
import es.safareyes.modelos.Perfil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Transactional // mantiene la sesión abierta para ver estilos y usuario (LAZY)
class TestRepositorioPerfil {

    private static final String EMAIL_SAUL = "delgadosaul677@gmail.com";

    @Autowired
    private IPerfilRepository repository;



    @Test
    void consultarPorEmail() {
        List<Perfil> todos = repository.findAll();


    }

    //consulta nº4
    @Test
    void consultarPerfil() {
        Optional<ConsultaPerfilProyeccion> perfil = repository.obtenerPerfil(2L);
        assertTrue(perfil.isPresent());

        ConsultaPerfilProyeccion p = perfil.get();
        System.out.println("PERFIL PÚBLICO ");
        System.out.println("Nombre: " + p.getNombreCompleto() + " | Ciudad: " + p.getCiudad());
        System.out.println("Biografía: " + p.getBiografia());
        System.out.println("Estilos: " + p.getEstilos());
        System.out.println("Tallas: " + p.getTallas());
        System.out.println("Reputación: " + p.getReputacion());
        System.out.println("Prendas: " + p.getPrendas());
    }

    //consulta nº5
    @Test
    void consultarPerfilesAfines() {
        List<ConsultaPerfilAfinProyeccion> lista = repository.obtenerPerfilesAfines(1L);
        assertFalse(lista.isEmpty());

        System.out.println("PERFILES AFINES AL PERFIL 1");
        lista.forEach(p -> {
            System.out.println(p.getIdPerfilAfin() + " | " + p.getNombreCompleto() + " | " + p.getCiudad());
            System.out.println("Estilos en común (" + p.getNumEstilosComunes() + "): " + p.getEstilosComunes());
            System.out.println("Reputación: " + p.getReputacion());
        });
    }

}




