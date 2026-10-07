import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final IdService idService;

    public Order addOrder(List<String> productIds) throws InvalidIDException {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Product productToOrder = productRepo.getProductById(productId)
                    .orElseThrow(() -> new InvalidIDException(productId));

            products.add(productToOrder);
        }

        Order newOrder = new Order(idService.generateId(), products, OrderStatus.PROCESSING, Instant.now());

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepo.getOrders().stream()
                .filter(order -> order.status() == status)
                .toList();
    }

    public Order updateOrder(String orderId, OrderStatus status) throws InvalidIDException {
        Order order = orderRepo.getOrderById(orderId);

        if (order == null) {
            throw new InvalidIDException(orderId);
        }

        Order updatedOrder = order.withStatus(status);

        orderRepo.removeOrder(orderId);
        return orderRepo.addOrder(updatedOrder);
    }

    public Map<OrderStatus, Order> getOldestOrderPerStatus() {
        return orderRepo.getOrders().stream()
                .collect(Collectors.toMap(
                        Order::status,
                        order -> order,
                        BinaryOperator.minBy(Comparator.comparing(Order::orderedAt))
                ));
    }
}
