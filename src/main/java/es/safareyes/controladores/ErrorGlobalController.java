package es.safareyes.controladores;

import es.safareyes.excepciones.ElementoNoEncontrado;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ErrorGlobalController {

    //ERROR 404 - Elemento no encontrado
    @ExceptionHandler(ElementoNoEncontrado.class)
    public ProblemDetail elementoNoEncontrado(
            ElementoNoEncontrado e) {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage());
        problema.setTitle("Elemento no encontrado");
        problema.setProperty("timestamp", LocalDateTime.now());
        return problema;
    }

    // ERROR 400 - Datos incorrectos del DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail datosIncorrectos(
            MethodArgumentNotValidException e) {
        List<String> errores = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Hay datos incorrectos en la petición");
        problema.setTitle("Datos incorrectos");
        problema.setProperty("errores", errores);
        problema.setProperty("timestamp", LocalDateTime.now());
        return problema;
    }

    //errrores 403, 409, 422 y otros del servicio
    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail errorDeServicio(
            ResponseStatusException e) {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason() != null
                        ? e.getReason()
                        : "No se ha podido realizar la operación");
        int codigo = e.getStatusCode().value();
        if (codigo == 403) {
            problema.setTitle("Acceso denegado");

        } else if (codigo == 409) {
            problema.setTitle("Conflicto");

        } else if (codigo == 422) {
            problema.setTitle("Regla de negocio incumplida");

        } else {
            problema.setTitle("Error en la petición");
        }
        problema.setProperty("timestamp", LocalDateTime.now());
        return problema;
    }

    //error 400 - JSON incorrecto o vacío
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail jsonIncorrecto(
            HttpMessageNotReadableException e) {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "El JSON enviado está vacío o no tiene el formato correcto");
        problema.setTitle("JSON incorrecto");
        problema.setProperty("timestamp", LocalDateTime.now());
        return problema;
    }
}