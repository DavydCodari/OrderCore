package com.davyd.ordercore.cart;

import com.davyd.ordercore.product.ProductRepository;
import com.davyd.ordercore.user.UserService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class CartAccountClosureTest {
    @Test void locksCartAndDeletesItemsBeforeCart() {
        var carts = mock(ShoppingCartRepository.class);
        var items = mock(CartItemRepository.class);
        var cart = mock(ShoppingCart.class);
        when(carts.findForUpdate(42)).thenReturn(Optional.of(cart));
        var service = new ShoppingCartService(carts, mock(UserService.class),
                mock(ShoppingCartMapper.class), items, mock(ProductRepository.class));

        service.deleteCartForAccountClosure(42);

        var sequence = inOrder(carts, items);
        sequence.verify(carts).findForUpdate(42);
        sequence.verify(items).deleteItemsFromCart(42);
        sequence.verify(carts).delete(cart);
    }

    @Test void missingCartDoesNotPreventAccountClosure() {
        var carts = mock(ShoppingCartRepository.class);
        var items = mock(CartItemRepository.class);
        var service = new ShoppingCartService(carts, mock(UserService.class),
                mock(ShoppingCartMapper.class), items, mock(ProductRepository.class));

        service.deleteCartForAccountClosure(42);

        verifyNoInteractions(items);
        verify(carts, never()).delete(any());
    }
}
