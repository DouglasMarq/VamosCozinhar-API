package com.douglasmarq.vamoscozinharapi.aspect;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import jakarta.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.douglasmarq.vamoscozinharapi.annotation.RateLimit;

@Aspect
@Component
public class RateLimitAspect {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<LocalDateTime>> requestCounts =
            new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String clientIp = getClientIp(request);

        if (!isAllowed(clientIp, rateLimit.requests(), rateLimit.windowSeconds())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(rateLimit.message());
        }

        return joinPoint.proceed();
    }

    private boolean isAllowed(String clientIp, int maxRequests, int windowSeconds) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now.minusSeconds(windowSeconds);

        ConcurrentLinkedQueue<LocalDateTime> requests =
                requestCounts.computeIfAbsent(clientIp, k -> new ConcurrentLinkedQueue<>());

        requests.removeIf(timestamp -> timestamp.isBefore(windowStart));

        if (requests.size() >= maxRequests) {
            return false;
        }

        requests.offer(now);
        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
