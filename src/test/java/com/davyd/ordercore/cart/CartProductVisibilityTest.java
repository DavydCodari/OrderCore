package com.davyd.ordercore.cart;

import com.davyd.ordercore.product.Product;
import com.davyd.ordercore.product.ProductRepository;
import com.davyd.ordercore.product.exception.ProductNotFoundException;
import com.davyd.ordercore.user.User;
import com.davyd.ordercore.user.UserRole;
import com.davyd.ordercore.user.UserService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartProductVisibilityTest {
    @Test void hiddenProductCannotBeAddedToCart() {
        var carts = mock(ShoppingCartRepository.class);
        var items = mock(CartItemRepository.class);
        var products = mock(ProductRepository.class);
        when(carts.findForUpdate(42)).thenReturn(Optional.of(mock(ShoppingCart.class)));
        var service = new ShoppingCartService(carts, mock(UserService.class),
                new ShoppingCartMapper(), items, products);

        assertThrows(ProductNotFoundException.class, () -> service.addCartItem(42, 7, 1));
        verify(items, never()).saveAndFlush(any());
    }

    @Test void existingCartItemShowsProductIsHidden() {
        var product = new Product("Book", null, BigDecimal.ONE, 3);
        var cart = new ShoppingCart(new User("Alice", "alice@example.com", UserRole.CUSTOMER));
        var item = new CartItem(product, cart, 1);
        product.update(null, null, null, null, false);

        assertFalse(new ShoppingCartMapper().toItemResponse(item).productVisible());
    }
}
