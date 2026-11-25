package es.safareyes.excepciones;
public class ElementoNoEncontrado extends RuntimeException {
    public ElementoNoEncontrado(String message) {
        super(message);
    }
}
