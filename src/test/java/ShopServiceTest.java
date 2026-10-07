import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() throws InvalidIDException {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING);
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
}
