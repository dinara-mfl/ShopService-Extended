public class InvalidIDException extends Exception {
    public InvalidIDException(String productId) {
        super("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
    }
}