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

    // Filter by quoteType
    Page<QuoteRequest> findByQuoteTypeOrderByCreatedAtDesc(String quoteType, Pageable pageable);

    Page<QuoteRequest> findByQuoteTypeAndStatusOrderByCreatedAtDesc(String quoteType, String status, Pageable pageable);

    long countByQuoteType(String quoteType);

    long countByQuoteTypeAndStatus(String quoteType, String status);

    // Filter by solution (ESCO, HYBRID, ESS)
    Page<QuoteRequest> findBySolutionInOrderByCreatedAtDesc(java.util.List<String> solutions, Pageable pageable);

    Page<QuoteRequest> findBySolutionInAndStatusOrderByCreatedAtDesc(java.util.List<String> solutions, String status,
            Pageable pageable);

    long countBySolutionInAndStatus(java.util.List<String> solutions, String status);

    // Filter by specific solution
    Page<QuoteRequest> findBySolutionOrderByCreatedAtDesc(String solution, Pageable pageable);

    Page<QuoteRequest> findBySolutionAndStatusOrderByCreatedAtDesc(String solution, String status, Pageable pageable);

    long countBySolutionAndStatus(String solution, String status);
}
