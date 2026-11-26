package com.davyd.ordercore.cart;

import com.davyd.ordercore.cart.dto.response.ShoppingCartResponse;
import com.davyd.ordercore.cart.dto.request.AddCartItemRequest;
import com.davyd.ordercore.cart.dto.request.UpdateCartItemQuantityRequest;
import com.davyd.ordercore.cart.dto.response.CartItemResponse;
import com.davyd.ordercore.security.principal.OrderCorePrincipal;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@PreAuthorize("hasRole('CUSTOMER')")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService){
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ResponseEntity<ShoppingCartResponse> getShoppingCart(
            @Parameter(hidden = true) @AuthenticationPrincipal OrderCorePrincipal principal){
        return ResponseEntity.ok(shoppingCartService.getShoppingCart(principal.getUserId()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addCartItem(
            @Parameter(hidden = true) @AuthenticationPrincipal OrderCorePrincipal principal,
            @Valid @RequestBody AddCartItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                shoppingCartService.addCartItem(principal.getUserId(), request.productId(), request.quantity()));
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartItemResponse> updateCartItemQuantity(
            @Parameter(hidden = true) @AuthenticationPrincipal OrderCorePrincipal principal,
            @PathVariable Long itemId, @Valid @RequestBody UpdateCartItemQuantityRequest request) {
        return ResponseEntity.ok(shoppingCartService.updateCartItemQuantity(
                principal.getUserId(), itemId, request.quantity()));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteCartItem(
            @Parameter(hidden = true) @AuthenticationPrincipal OrderCorePrincipal principal,
            @PathVariable Long itemId) {
        shoppingCartService.deleteCartItem(principal.getUserId(), itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items")
    public ResponseEntity<Void> clearCart(
            @Parameter(hidden = true) @AuthenticationPrincipal OrderCorePrincipal principal) {
        shoppingCartService.clearCart(principal.getUserId());
        return ResponseEntity.noContent().build();
    }
}
