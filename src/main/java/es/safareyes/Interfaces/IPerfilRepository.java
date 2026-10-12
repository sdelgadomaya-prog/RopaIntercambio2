package es.safareyes.Interfaces;

import es.safareyes.dta.ConsultaPerfilAfinProyeccion;
import es.safareyes.dta.ConsultaPerfilProyeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import es.safareyes.modelos.Perfil;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IPerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByEmail(String email);

    Optional<Perfil> findByUsuarioId(Long idUsuario);

    List<Perfil> findByCiudadIgnoreCase(String ciudad);

    @Query("select p from Perfil p join p.estilos e where e.nombre = :nombre")
    List<Perfil> buscarPorNombreEstilo(@Param("nombre") String nombreEstilo);

    @Query(value = "select * from perfil where ciudad = :ciudad", nativeQuery = true)
    List<Perfil> buscarPorCiudadNativa(@Param("ciudad") String ciudad);

    //consulta nº4
    @Query(nativeQuery = true, value =
            "SELECT " +
                    "  p.id_perfil AS \"idPerfil\", " +
                    "  CONCAT_WS(' ', p.nombre, p.apellido1, p.apellido2) AS \"nombreCompleto\", " +
                    "  p.ciudad AS \"ciudad\", " +
                    "  STRING_AGG(e.nombre, ', ') AS \"estilos\", " +
                    "  p.biografia AS \"biografia\", " +
                    "  CONCAT('Talla pantalón: ', p.talla_pantalon, " +
                    "         ' | Talla camiseta: ', p.talla_camiseta, " +
                    "         ' | Talla zapato: ', p.talla_zapatos) AS \"tallas\", " +
                    "  rep.reputacion AS \"reputacion\", " +
                    "  (SELECT STRING_AGG(pr.titulo, ', ' ORDER BY pr.fecha_publicacion DESC) " +
                    "     FROM prenda pr " +
                    "    WHERE pr.id_perfil = p.id_perfil AND pr.disponibilidad = TRUE) AS \"prendas\" " +
                    "FROM perfil p " +
                    "  JOIN reputacion_perfil rep ON rep.id_perfil = p.id_perfil " +
                    "  LEFT JOIN perfil_estilos pe ON pe.id_perfil = p.id_perfil " +
                    "  LEFT JOIN estilos e ON e.id_estilo = pe.id_estilo " +
                    "WHERE p.id_perfil = :id " +
                    "GROUP BY p.id_perfil, rep.reputacion")
    Optional<ConsultaPerfilProyeccion> obtenerPerfil(@Param("id") Long id);


    //consulta nº5
    @Query(nativeQuery = true, value =
            "SELECT " +
                    "  p.id_perfil AS \"idPerfilAfin\", " +
                    "  CONCAT_WS(' ', p.nombre, p.apellido1, p.apellido2) AS \"nombreCompleto\", " +
                    "  p.ciudad AS \"ciudad\", " +
                    "  COUNT(e.id_estilo) AS \"numEstilosComunes\", " +
                    "  STRING_AGG(e.nombre, ', ') AS \"estilosComunes\", " +
                    "  rep.reputacion AS \"reputacion\" " +
                    "FROM perfil p " +
                    "  JOIN perfil_estilos pe ON pe.id_perfil = p.id_perfil " +
                    "  JOIN estilos e ON e.id_estilo = pe.id_estilo " +
                    "  JOIN reputacion_perfil rep ON rep.id_perfil = p.id_perfil " +
                    "WHERE p.id_perfil <> :idPerfil " +
                    "  AND pe.id_estilo IN (SELECT id_estilo FROM perfil_estilos " +
                    "                        WHERE id_perfil = :idPerfil) " +
                    "GROUP BY p.id_perfil, rep.reputacion, rep.media_puntuacion " +
                    "ORDER BY COUNT(e.id_estilo) DESC, rep.media_puntuacion DESC NULLS LAST")
    List<ConsultaPerfilAfinProyeccion> obtenerPerfilesAfines(@Param("idPerfil") Long idPerfil);
}


