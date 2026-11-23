package com.davyd.ordercore.model;

import com.davyd.ordercore.auth.UserCredentials;
import com.davyd.ordercore.cart.CartItem;
import com.davyd.ordercore.cart.ShoppingCart;
import com.davyd.ordercore.order.Order;
import com.davyd.ordercore.order.OrderItem;
import com.davyd.ordercore.order.OrderStatus;
import com.davyd.ordercore.payment.Payment;
import com.davyd.ordercore.payment.PaymentStatus;
import com.davyd.ordercore.product.Product;
import com.davyd.ordercore.user.User;
import com.davyd.ordercore.user.UserRole;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {
    private static final BigDecimal PRICE = new BigDecimal("12.50");

    private User user() { return new User("Alice", "alice@example.com", UserRole.CUSTOMER); }
    private Product product() { return new Product("Book", "Description", PRICE, 3); }
    private Order order() { return new Order(user(), OrderStatus.CREATED); }

    @Nested
    class Users {
        @Test void createsUser() {
            User user = user();
            assertEquals("Alice", user.getName());
            assertEquals("alice@example.com", user.getEmail());
            assertEquals(UserRole.CUSTOMER, user.getRole());
            assertNull(user.getId());
            assertNull(user.getCreatedAt());
        }

        @Test void validatesUserFields() {
            assertThrows(IllegalArgumentException.class, () -> new User(null, "a@b.com", UserRole.ADMIN));
            assertThrows(IllegalArgumentException.class, () -> new User(" ", "a@b.com", UserRole.ADMIN));
            assertDoesNotThrow(() -> new User("a".repeat(100), "a@b.com", UserRole.ADMIN));
            assertThrows(IllegalArgumentException.class, () -> new User("a".repeat(101), "a@b.com", UserRole.ADMIN));
            assertDoesNotThrow(() -> new User("Alice", "a".repeat(249) + "@b.com", UserRole.CUSTOMER));
            assertThrows(IllegalArgumentException.class, () -> new User("Alice", "a".repeat(250) + "@b.com", UserRole.CUSTOMER));
            for (String email : new String[] {"", "invalid", "a@", "a b@c.com"}) {
                assertThrows(IllegalArgumentException.class, () -> new User("Alice", email, UserRole.CUSTOMER));
            }
            assertThrows(IllegalArgumentException.class, () -> new User("Alice", null, UserRole.CUSTOMER));
            assertThrows(IllegalArgumentException.class, () -> new User("Alice", "a@b.com", null));
        }
    }

    @Nested
    class Credentials {
        @Test void validatesAndStoresUserCredentials() {
            User user = user();
            UserCredentials credentials = new UserCredentials(user, "hash");
            assertSame(user, credentials.getUser());
            assertNull(user.getId());
            assertEquals("hash", credentials.getHashedPassword());
            assertDoesNotThrow(() -> new UserCredentials(user, "a".repeat(255)));
            assertThrows(IllegalArgumentException.class, () -> new UserCredentials(null, "hash"));
            for (String hash : new String[] {null, " ", "a".repeat(256)}) {
                assertThrows(IllegalArgumentException.class, () -> new UserCredentials(user, hash));
            }
        }
    }

    @Nested
    class Products {
        @Test void createsProductAndAcceptsBoundaries() {
            Product product = new Product("a".repeat(100), "b".repeat(100), BigDecimal.ZERO, 0);
            assertEquals("a".repeat(100), product.getName());
            assertEquals("b".repeat(100), product.getDescription());
            assertEquals(BigDecimal.ZERO, product.getPrice());
            assertEquals(0, product.getStockQuantity());
            assertDoesNotThrow(() -> new Product("Book", null, new BigDecimal("9999999999.99"), 1));
        }

        @Test void rejectsInvalidProductFields() {
            assertThrows(IllegalArgumentException.class, () -> new Product(null, null, PRICE, 1));
            assertThrows(IllegalArgumentException.class, () -> new Product(" ", null, PRICE, 1));
            assertThrows(IllegalArgumentException.class, () -> new Product("a".repeat(101), null, PRICE, 1));
            assertThrows(IllegalArgumentException.class, () -> new Product("Book", "a".repeat(101), PRICE, 1));
            assertThrows(IllegalArgumentException.class, () -> new Product("Book", null, PRICE, -1));
            for (BigDecimal price : new BigDecimal[] {null, new BigDecimal("-0.01"), new BigDecimal("1.001"), new BigDecimal("10000000000.00")}) {
                assertThrows(IllegalArgumentException.class, () -> new Product("Book", null, price, 1));
            }
        }
    }

    @Nested
    class Carts {
        @Test void validatesAndStoresCartForCustomer() {
            User customer = user();
            assertSame(customer, new ShoppingCart(customer).getCustomer());
            assertThrows(IllegalArgumentException.class, () -> new ShoppingCart(null));
        }

        @Test void validatesAndStoresCartItem() {
            Product product = product();
            ShoppingCart cart = new ShoppingCart(user());
            CartItem item = new CartItem(product, cart, 1);
            assertSame(product, item.getProduct());
            assertSame(cart, item.getShoppingCart());
            assertEquals(1, item.getQuantity());
            assertThrows(IllegalArgumentException.class, () -> new CartItem(null, cart, 1));
            assertThrows(IllegalArgumentException.class, () -> new CartItem(product, null, 1));
            assertThrows(IllegalArgumentException.class, () -> new CartItem(product, cart, 0));
            assertThrows(IllegalArgumentException.class, () -> new CartItem(product, cart, -1));
        }
    }

    @Nested
    class Orders {
        @Test void validatesAndStoresOrder() {
            User customer = user();
            Order order = new Order(customer, OrderStatus.CREATED);
            assertSame(customer, order.getCustomer());
            assertEquals(OrderStatus.CREATED, order.getStatus());
            assertThrows(IllegalArgumentException.class, () -> new Order(null, OrderStatus.CREATED));
            assertThrows(IllegalArgumentException.class, () -> new Order(customer, null));
        }

        @Test void validatesAndStoresOrderItem() {
            Product product = product();
            Order order = order();
            OrderItem item = new OrderItem(product, order, 1, PRICE);
            assertSame(product, item.getProduct());
            assertSame(order, item.getOrder());
            assertEquals(1, item.getQuantity());
            assertEquals(PRICE, item.getPrice());
            assertDoesNotThrow(() -> new OrderItem(product, order, 1, BigDecimal.ZERO));
            assertDoesNotThrow(() -> new OrderItem(product, order, 1, new BigDecimal("9999999999.99")));
            assertThrows(IllegalArgumentException.class, () -> new OrderItem(null, order, 1, PRICE));
            assertThrows(IllegalArgumentException.class, () -> new OrderItem(product, null, 1, PRICE));
            assertThrows(IllegalArgumentException.class, () -> new OrderItem(product, order, 0, PRICE));
            assertThrows(IllegalArgumentException.class, () -> new OrderItem(product, order, -1, PRICE));
            for (BigDecimal price : new BigDecimal[] {null, new BigDecimal("-1"), new BigDecimal("1.001"), new BigDecimal("10000000000")}) {
                assertThrows(IllegalArgumentException.class, () -> new OrderItem(product, order, 1, price));
            }
        }
    }

    @Nested
    class Payments {
        @Test void validatesAndStoresPayment() {
            Order order = order();
            Payment payment = new Payment(order, PRICE, "EUR", PaymentStatus.PENDING);
            assertSame(order, payment.getOrder());
            assertEquals(PRICE, payment.getPrice());
            assertEquals("EUR", payment.getCurrency());
            assertEquals(PaymentStatus.PENDING, payment.getStatus());
            assertDoesNotThrow(() -> new Payment(order, BigDecimal.ZERO, "USD", PaymentStatus.COMPLETED));
            assertDoesNotThrow(() -> new Payment(order, new BigDecimal("9999999999.99"), "USD", PaymentStatus.COMPLETED));
            assertThrows(IllegalArgumentException.class, () -> new Payment(null, PRICE, "EUR", PaymentStatus.PENDING));
            assertThrows(IllegalArgumentException.class, () -> new Payment(order, PRICE, "EUR", null));
            for (String currency : new String[] {null, "", " ", "eu", "eur", "EURO", "E1R"}) {
                assertThrows(IllegalArgumentException.class, () -> new Payment(order, PRICE, currency, PaymentStatus.PENDING));
            }
            for (BigDecimal price : new BigDecimal[] {null, new BigDecimal("-1"), new BigDecimal("1.001"), new BigDecimal("10000000000")}) {
                assertThrows(IllegalArgumentException.class, () -> new Payment(order, price, "EUR", PaymentStatus.PENDING));
            }
        }
    }
}
