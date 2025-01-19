package com.companyx.prices.adapter.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, Long> {

    @Query(value = "SELECT * FROM price p WHERE p.product_id = :productId AND p.brand_id = :brandId AND :applicationTime BETWEEN p.start_date AND p.end_date ORDER BY p.priority DESC LIMIT 1", nativeQuery = true)
    Optional<PriceEntity> findTopApplicablePrices(@Param("productId") Integer productId, @Param("brandId") Integer brandId, @Param("applicationTime") LocalDateTime applicationTime);
}
