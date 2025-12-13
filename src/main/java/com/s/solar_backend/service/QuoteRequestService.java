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

    public long countByStatus(String status) {
        return quoteRequestRepository.countByStatus(status);
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
        dto.setCreatedAt(quoteRequest.getCreatedAt());
        dto.setUpdatedAt(quoteRequest.getUpdatedAt());
        return dto;
    }
}
