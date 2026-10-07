import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    Instant orderedAt = Instant.parse("2026-10-07T10:00:00Z");

    @Test
    void addOrderTest() throws InvalidIDException {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, orderedAt);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() throws InvalidIDException {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        // WHEN & THEN
        assertThrows(InvalidIDException.class, () -> shopService.addOrder(productsIds));
    }

    @Test
    void getOrdersByStatus() throws InvalidIDException {
        ShopService shopService = new ShopService();
        Order firstOrder = shopService.addOrder(List.of());
        Order secondOrder = shopService.addOrder(List.of());

        List<Order> actual = shopService.getOrdersByStatus(OrderStatus.PROCESSING);

        assertEquals(2, actual.size());
        assertTrue(actual.containsAll(List.of(firstOrder, secondOrder)));
    }

    @Test
    void getOrdersByStatus_ReturnsEmptyList_WhenNoOrdersMatch() throws InvalidIDException {
        ShopService shopService = new ShopService();
        shopService.addOrder(List.of());

        List<Order> actual = shopService.getOrdersByStatus(OrderStatus.COMPLETED);

        assertTrue(actual.isEmpty());
    }

    @Test
    void getOrdersByStatus_ReturnsEmptyList_WhenRepoIsEmpty() {
        ShopService shopService = new ShopService();

        List<Order> actual = shopService.getOrdersByStatus(OrderStatus.PROCESSING);

        assertTrue(actual.isEmpty());
    }

    @Test
    void updateOrder_whenValidId_expectUpdatedOrder() throws InvalidIDException {
        ShopService shopService = new ShopService();
        Order originalOrder = shopService.addOrder(List.of("1"));

        Order actual = shopService.updateOrder(
                originalOrder.id(),
                OrderStatus.IN_DELIVERY
        );

        Order expected = originalOrder.withStatus(OrderStatus.IN_DELIVERY);

        assertEquals(expected, actual);
        assertEquals(
                List.of(expected),
                shopService.getOrdersByStatus(OrderStatus.IN_DELIVERY)
        );
        assertTrue(
                shopService.getOrdersByStatus(OrderStatus.PROCESSING).isEmpty()
        );
    }

    @Test
    void updateOrder_whenCompleted_expectCompletedOrder() throws InvalidIDException {
        ShopService shopService = new ShopService();
        Order originalOrder = shopService.addOrder(List.of("1"));
        shopService.updateOrder(originalOrder.id(), OrderStatus.IN_DELIVERY);

        Order actual = shopService.updateOrder(
                originalOrder.id(),
                OrderStatus.COMPLETED
        );

        assertEquals(originalOrder.withStatus(OrderStatus.COMPLETED), actual);
        assertEquals(
                List.of(actual),
                shopService.getOrdersByStatus(OrderStatus.COMPLETED)
        );
        assertTrue(
                shopService.getOrdersByStatus(OrderStatus.IN_DELIVERY).isEmpty()
        );
    }

    @Test
    void updateOrder_whenInvalidId_expectInvalidIDException() {
        ShopService shopService = new ShopService();

        InvalidIDException exception = assertThrows(
                InvalidIDException.class,
                () -> shopService.updateOrder("unknown-id", OrderStatus.COMPLETED)
        );

        assertEquals("Ungültige ID: unknown-id", exception.getMessage());
    }
}
