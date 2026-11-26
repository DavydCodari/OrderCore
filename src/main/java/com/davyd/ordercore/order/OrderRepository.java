package com.davyd.ordercore.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndCustomer_Id(long id, long customerId);

    boolean existsByIdAndCustomer_Id(long id, long customerId);

    @Modifying
    @Query("update Order o set o.status = :cancelled where o.id = :id " +
            "and o.customer.id = :customerId and o.status = :created")
    int cancelCreatedOrder(@Param("id") long id, @Param("customerId") long customerId,
                           @Param("created") OrderStatus created, @Param("cancelled") OrderStatus cancelled);

    @Modifying
    @Query("update Order o set o.status = :delivered where o.id = :id " +
            "and o.customer.id = :customerId and o.status = :processing")
    int confirmDelivery(@Param("id") long id, @Param("customerId") long customerId,
                        @Param("processing") OrderStatus processing, @Param("delivered") OrderStatus delivered);
}
