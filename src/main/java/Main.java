import java.util.List;

public class Main {

    public static void main(String[] args) throws InvalidIDException {
        ProductRepo productRepo = new ProductRepo();
        OrderRepo orderRepo = new OrderMapRepo();
        IdService idService = new IdService();

        ShopService shopService = new ShopService(productRepo, orderRepo, idService);

        productRepo.addProduct(new Product("2", "Banane"));
        productRepo.addProduct(new Product("3", "Orange"));

        Order firstOrder = shopService.addOrder(List.of("1", "2"));
        Order secondOrder = shopService.addOrder(List.of("2", "3"));
        Order thirdOrder = shopService.addOrder(List.of("1", "2", "3"));

        System.out.println(firstOrder);
        System.out.println(secondOrder);
        System.out.println(thirdOrder);
    }
}