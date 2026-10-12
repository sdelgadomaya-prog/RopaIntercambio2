package es.safareyes.dta;

import java.time.LocalDateTime;

public interface ConsultaPrendaDetalleProyeccion {
    Long getIdPrenda();
    String getTitulo();
    String getDescripcion();
    String getMarca();
    String getTalla();
    String getEstado();
    Boolean getDisponibilidad();
    Boolean getPermiteIntercambio();
    Boolean getPermitePrestamo();
    LocalDateTime getFechaPublicacion();
    String getCategoria();
    Long getIdPropietario();
    String getPropietario();
    Double getMediaPuntuacion();
    Long getNumValoraciones();
    String getFotos();
}