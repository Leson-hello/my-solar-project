package com.s.solar_backend.repository;

import com.s.solar_backend.entity.QuoteRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRequestRepository extends JpaRepository<QuoteRequest, Long> {

    Page<QuoteRequest> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    Page<QuoteRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);

    long countByStatus(String status);
}
