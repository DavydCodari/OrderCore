package com.davyd.ordercore.product;

import com.davyd.ordercore.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductVisibilityTest {
    @Test void newProductsAreVisibleAndAdminCanHideAndRestoreThem() {
        var product = new Product("Book", null, BigDecimal.ONE, 3);
        assertTrue(product.isVisible());
        product.update(null, null, null, null, false);
        assertFalse(product.isVisible());
        product.update(null, null, null, null, null);
        assertFalse(product.isVisible());
        product.update(null, null, null, null, true);
        assertTrue(product.isVisible());
    }

    @Test void hiddenProductIsNotAvailableFromPublicLookupButAdminCanSeeIt() {
        var repository = mock(ProductRepository.class);
        var product = new Product("Book", null, BigDecimal.ONE, 3);
        product.update(null, null, null, null, false);
        when(repository.findById(7L)).thenReturn(Optional.of(product));
        var service = new ProductService(repository, new ProductMapper());

        assertThrows(ProductNotFoundException.class, () -> service.getProduct(7));
        assertFalse(service.getProductForAdmin(7).visible());
    }

    @Test void productListingsUseVisibilityFilterAndFixedPaginationOrder() {
        var repository = mock(ProductRepository.class);
        var visible = new Product("Visible", null, BigDecimal.ONE, 3);
        var hidden = new Product("Hidden", null, BigDecimal.ONE, 3);
        hidden.update(null, null, null, null, false);
        var expectedPage = PageRequest.of(1, 2, Sort.by("id"));
        when(repository.findByVisibleTrue(expectedPage)).thenReturn(new PageImpl<>(List.of(visible), expectedPage, 3));
        when(repository.findAll(expectedPage)).thenReturn(new PageImpl<>(List.of(visible, hidden), expectedPage, 4));
        var service = new ProductService(repository, new ProductMapper());
        var requestedPage = PageRequest.of(1, 2, Sort.by("name"));

        var publicProducts = service.getProducts(requestedPage);
        var adminProducts = service.getProductsForAdmin(requestedPage);

        assertEquals(3, publicProducts.getTotalElements());
        assertTrue(publicProducts.getContent().stream().allMatch(p -> p.visible()));
        assertEquals(4, adminProducts.getTotalElements());
        assertTrue(adminProducts.getContent().stream().anyMatch(p -> !p.visible()));
        verify(repository).findByVisibleTrue(expectedPage);
        verify(repository).findAll(expectedPage);
    }
}
