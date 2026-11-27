package com.davyd.ordercore.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndVisibleTrue(Long id);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Product> findWithLockByIdAndVisibleTrue(Long id);
}
