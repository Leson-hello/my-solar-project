package com.s.solar_backend.service;

import com.s.solar_backend.dto.QuoteRequestDTO;
import com.s.solar_backend.entity.QuoteRequest;
import com.s.solar_backend.repository.QuoteRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuoteRequestService {

    private final QuoteRequestRepository quoteRequestRepository;

    @Transactional
    public QuoteRequestDTO createQuoteRequest(QuoteRequestDTO dto) {
        QuoteRequest quoteRequest = new QuoteRequest();
        updateQuoteRequestFromDTO(quoteRequest, dto);
        quoteRequest.setStatus("PENDING");

        QuoteRequest savedRequest = quoteRequestRepository.save(quoteRequest);
        return convertToDTO(savedRequest);
    }

    public Page<QuoteRequestDTO> getAllQuoteRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return quoteRequestRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::convertToDTO);
    }

    public Page<QuoteRequestDTO> getQuoteRequestsByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return quoteRequestRepository.findByStatusOrderByCreatedAtDesc(status, pageable)
                .map(this::convertToDTO);
    }

    public Optional<QuoteRequestDTO> getQuoteRequestById(Long id) {
        return quoteRequestRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public QuoteRequestDTO updateQuoteRequest(Long id, QuoteRequestDTO dto) {
        QuoteRequest quoteRequest = quoteRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quote request not found"));

        updateQuoteRequestFromDTO(quoteRequest, dto);
        if (dto.getStatus() != null) {
            quoteRequest.setStatus(dto.getStatus());
        }
        if (dto.getAdminNotes() != null) {
            quoteRequest.setAdminNotes(dto.getAdminNotes());
        }

        QuoteRequest updatedRequest = quoteRequestRepository.save(quoteRequest);
        return convertToDTO(updatedRequest);
    }

    @Transactional
    public void deleteQuoteRequest(Long id) {
        quoteRequestRepository.deleteById(id);
    }

    @Transactional
    public QuoteRequest save(QuoteRequest quoteRequest) {
        return quoteRequestRepository.save(quoteRequest);
    }

    public long countByStatus(String status) {
        return quoteRequestRepository.countByStatus(status);
    }

    // Filter by quoteType
    public Page<QuoteRequestDTO> getQuoteRequestsByType(String type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return quoteRequestRepository.findByQuoteTypeOrderByCreatedAtDesc(type, pageable)
                .map(this::convertToDTO);
    }

    public Page<QuoteRequestDTO> getQuoteRequestsByTypeAndStatus(String type, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return quoteRequestRepository.findByQuoteTypeAndStatusOrderByCreatedAtDesc(type, status, pageable)
                .map(this::convertToDTO);
    }

    public long countByTypeAndStatus(String type, String status) {
        return quoteRequestRepository.countByQuoteTypeAndStatus(type, status);
    }

    // Filter by solution (ESCO, HYBRID, ESS)
    public Page<QuoteRequestDTO> getAllSolutionQuotes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return quoteRequestRepository.findBySolutionInOrderByCreatedAtDesc(
                java.util.Arrays.asList("ESCO", "HYBRID", "ESS"), pageable)
                .map(this::convertToDTO);
    }

    public Page<QuoteRequestDTO> getQuoteRequestsBySolution(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return quoteRequestRepository.findBySolutionInAndStatusOrderByCreatedAtDesc(
                java.util.Arrays.asList("ESCO", "HYBRID", "ESS"), status, pageable)
                .map(this::convertToDTO);
    }

    public long countSolutionByStatus(String status) {
        return quoteRequestRepository.countBySolutionInAndStatus(
                java.util.Arrays.asList("ESCO", "HYBRID", "ESS"), status);
    }

    // Filter by specific solution type
    public Page<QuoteRequestDTO> getQuoteRequestsBySolutionType(String solution, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return quoteRequestRepository.findBySolutionOrderByCreatedAtDesc(solution, pageable)
                .map(this::convertToDTO);
    }

    public Page<QuoteRequestDTO> getQuoteRequestsBySolutionTypeAndStatus(String solution, String status, int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size);
        return quoteRequestRepository.findBySolutionAndStatusOrderByCreatedAtDesc(solution, status, pageable)
                .map(this::convertToDTO);
    }

    public long countBySolutionAndStatus(String solution, String status) {
        return quoteRequestRepository.countBySolutionAndStatus(solution, status);
    }

    private void updateQuoteRequestFromDTO(QuoteRequest quoteRequest, QuoteRequestDTO dto) {
        quoteRequest.setFullName(dto.getFullName());
        quoteRequest.setEmail(dto.getEmail());
        quoteRequest.setPhone(dto.getPhone());
        quoteRequest.setAddress(dto.getAddress());
        quoteRequest.setProductType(dto.getProductType());
        quoteRequest.setEstimatedPower(dto.getEstimatedPower());
        quoteRequest.setInstallationType(dto.getInstallationType());
        quoteRequest.setMessage(dto.getMessage());
        if (dto.getQuoteType() != null) {
            quoteRequest.setQuoteType(dto.getQuoteType());
        }
        if (dto.getMonthlyBill() != null) {
            quoteRequest.setMonthlyBill(dto.getMonthlyBill());
        }
        if (dto.getRecommendedPackage() != null) {
            quoteRequest.setRecommendedPackage(dto.getRecommendedPackage());
        }
    }

    private QuoteRequestDTO convertToDTO(QuoteRequest quoteRequest) {
        QuoteRequestDTO dto = new QuoteRequestDTO();
        dto.setId(quoteRequest.getId());
        dto.setFullName(quoteRequest.getFullName());
        dto.setEmail(quoteRequest.getEmail());
        dto.setPhone(quoteRequest.getPhone());
        dto.setAddress(quoteRequest.getAddress());
        dto.setProductType(quoteRequest.getProductType());
        dto.setEstimatedPower(quoteRequest.getEstimatedPower());
        dto.setInstallationType(quoteRequest.getInstallationType());
        dto.setMessage(quoteRequest.getMessage());
        dto.setStatus(quoteRequest.getStatus());
        dto.setAdminNotes(quoteRequest.getAdminNotes());
        dto.setQuoteType(quoteRequest.getQuoteType());
        dto.setMonthlyBill(quoteRequest.getMonthlyBill());
        dto.setRecommendedPackage(quoteRequest.getRecommendedPackage());
        dto.setSolution(quoteRequest.getSolution());
        dto.setCompany(quoteRequest.getCompany());
        dto.setProvince(quoteRequest.getProvince());
        dto.setCreatedAt(quoteRequest.getCreatedAt());
        dto.setUpdatedAt(quoteRequest.getUpdatedAt());
        return dto;
    }
}
