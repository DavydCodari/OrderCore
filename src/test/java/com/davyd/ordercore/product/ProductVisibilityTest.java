package com.davyd.ordercore.product;

import com.davyd.ordercore.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
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
}
