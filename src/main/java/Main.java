import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws InvalidIDException, IOException {
        ProductRepo productRepo = new ProductRepo();
        OrderRepo orderRepo = new OrderMapRepo();
        IdService idService = new IdService();

        ShopService shopService = new ShopService(productRepo, orderRepo, idService);

        productRepo.addProduct(new Product("2", "Banane"));
        productRepo.addProduct(new Product("3", "Orange"));
        productRepo.addProduct(new Product("4", "Birne"));

        Order firstOrder = shopService.addOrder(List.of("1", "2"));
        Order secondOrder = shopService.addOrder(List.of("2", "3"));
        Order thirdOrder = shopService.addOrder(List.of("1", "2", "3"));

        System.out.println(firstOrder);
        System.out.println(secondOrder);
        System.out.println(thirdOrder);

        Map<String, String> orderIds = new HashMap<>();

        for (String line : Files.readAllLines(Path.of("transactions.txt"))) {
            if (line.isBlank()) {
                continue;
            }

            String[] parts = line.trim().split("\\s+");

            switch (parts[0]) {
                case "addOrder" -> {
                    Order order = shopService.addOrder(
                            Arrays.asList(parts).subList(2, parts.length)
                    );
                    orderIds.put(parts[1], order.id());
                }
                case "setStatus" -> shopService.updateOrder(
                        orderIds.get(parts[1]),
                        OrderStatus.valueOf(parts[2])
                );
                case "printOrders" -> shopService.printOrders();
                default -> throw new IllegalArgumentException(
                        "Unbekannter Befehl: " + parts[0]
                );
            }
        }
    }
}