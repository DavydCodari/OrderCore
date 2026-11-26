package com.davyd.ordercore.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByShoppingCart_ShoppingCartIdOrderByIdAsc(long cartId);

    @Modifying
    @Query("delete from CartItem item where item.id = :itemId and item.shoppingCart.shoppingCartId = :cartId")
    int deleteItemFromCart(@Param("itemId") long itemId, @Param("cartId") long cartId);

    @Modifying
    @Query("delete from CartItem item where item.shoppingCart.shoppingCartId = :cartId")
    int deleteItemsFromCart(@Param("cartId") long cartId);
}
