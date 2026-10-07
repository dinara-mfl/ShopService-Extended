public class InvalidIDException extends Exception {
    public InvalidIDException(String id) {
        super("Ungültige ID: " + id);    }
}