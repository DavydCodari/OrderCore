package com.davyd.ordercore.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndVisibleTrue(Long id);

    Page<Product> findByVisibleTrue(Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Product> findWithLockByIdAndVisibleTrue(Long id);
}
