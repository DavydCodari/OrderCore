package com.davyd.ordercore.order;

import com.davyd.ordercore.order.dto.response.OrderResponse;
import com.davyd.ordercore.security.principal.OrderCorePrincipal;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@PreAuthorize("hasRole('CUSTOMER')")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(
            @Parameter(hidden = true) @AuthenticationPrincipal OrderCorePrincipal principal,
            @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(principal.getUserId(), id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @Parameter(hidden = true) @AuthenticationPrincipal OrderCorePrincipal principal,
            @PathVariable Long id) {
        orderService.cancelOrder(principal.getUserId(), id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deliver")
    public ResponseEntity<Void> confirmDelivery(
            @Parameter(hidden = true) @AuthenticationPrincipal OrderCorePrincipal principal,
            @PathVariable Long id) {
        orderService.confirmDelivery(principal.getUserId(), id);
        return ResponseEntity.noContent().build();
    }
}
