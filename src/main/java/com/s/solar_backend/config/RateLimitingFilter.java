package com.s.solar_backend.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A very simple in-memory Rate Limiting Filter.
 * WARNING: This is for demonstration and basic protection only.
 * In a production environment, use a Redis-based solution or a proper API
 * Gateway.
 */
@Component
@Slf4j
public class RateLimitingFilter implements Filter {

    // Helper class to store request count and timestamp
    private static class RequestCounter {
        AtomicInteger count = new AtomicInteger(0);
        long windowStart = System.currentTimeMillis();
    }

    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

    // Configuration
    private static final int MAX_REQUESTS_PER_MINUTE = 300; // Increased from 100
    private static final long WINDOW_SIZE_MS = 60 * 1000; // 1 minute

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {

            // Skip rate limiting for static resources
            String path = httpRequest.getRequestURI();
            if (isStaticResource(path)) {
                chain.doFilter(request, response);
                return;
            }

            String clientIp = getClientIp(httpRequest);

            if (isRateLimited(clientIp)) {
                httpResponse.setStatus(429); // Too Many Requests
                httpResponse.getWriter().write("Too many requests. Please try again later.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isStaticResource(String path) {
        return path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/images/") ||
                path.startsWith("/photo/") ||
                path.startsWith("/uploads/") ||
                path.endsWith(".css") ||
                path.endsWith(".js") ||
                path.endsWith(".png") ||
                path.endsWith(".jpg") ||
                path.endsWith(".jpeg") ||
                path.endsWith(".ico") ||
                path.endsWith(".woff2");
    }

    private boolean isRateLimited(String clientIp) {
        RequestCounter counter = requestCounts.computeIfAbsent(clientIp, k -> new RequestCounter());

        long now = System.currentTimeMillis();

        // If window has passed, reset counter
        if (now - counter.windowStart > WINDOW_SIZE_MS) {
            counter.windowStart = now;
            counter.count.set(0);
        }

        // Increment count
        int requests = counter.count.incrementAndGet();

        if (requests > MAX_REQUESTS_PER_MINUTE) {
            log.warn("Rate limit exceeded for IP: " + clientIp);
            return true;
        }

        return false;
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
