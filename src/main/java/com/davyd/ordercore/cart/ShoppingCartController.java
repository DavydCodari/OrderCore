package com.davyd.ordercore.cart;

import com.davyd.ordercore.cart.dto.response.ShoppingCartResponse;
import com.davyd.ordercore.security.principal.OrderCorePrincipal;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
}
