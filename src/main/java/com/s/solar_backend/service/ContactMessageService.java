package com.s.solar_backend.service;

import com.s.solar_backend.model.ContactMessage;
import com.s.solar_backend.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessage save(ContactMessage message) {
        return contactMessageRepository.save(message);
    }

    public Page<ContactMessage> getAllMessages(Pageable pageable) {
        return contactMessageRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<ContactMessage> getMessagesByStatus(String status, Pageable pageable) {
        return contactMessageRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
    }

    public ContactMessage getById(Long id) {
        return contactMessageRepository.findById(id).orElse(null);
    }

    public ContactMessage markAsRead(Long id) {
        ContactMessage message = getById(id);
        if (message != null) {
            message.setStatus("READ");
            message.setReadAt(LocalDateTime.now());
            return contactMessageRepository.save(message);
        }
        return null;
    }

    public ContactMessage markAsReplied(Long id) {
        ContactMessage message = getById(id);
        if (message != null) {
            message.setStatus("REPLIED");
            return contactMessageRepository.save(message);
        }
        return null;
    }

    public void delete(Long id) {
        contactMessageRepository.deleteById(id);
    }

    public long countPending() {
        return contactMessageRepository.countByStatus("PENDING");
    }

    public long countAll() {
        return contactMessageRepository.count();
    }
}
