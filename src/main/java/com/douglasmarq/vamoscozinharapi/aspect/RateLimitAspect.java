package com.douglasmarq.vamoscozinharapi.aspect;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.douglasmarq.vamoscozinharapi.annotation.RateLimit;
import com.douglasmarq.vamoscozinharapi.exception.ApiException;

@Aspect
@Component
public class RateLimitAspect {

    private final ConcurrentHashMap<String, Deque<Instant>> requestCounts =
            new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String clientIp = getClientIp(request);

        if (!isAllowed(clientIp, rateLimit.requests(), rateLimit.windowSeconds())) {
            throw new ApiException(HttpStatus.TOO_MANY_REQUESTS, rateLimit.messageKey());
        }
        return joinPoint.proceed();
    }

    private boolean isAllowed(String clientIp, int maxRequests, int windowSeconds) {
        Instant now = Instant.now();
        Instant windowStart = now.minusSeconds(windowSeconds);
        boolean[] allowed = {true};

        requestCounts.compute(
                clientIp,
                (k, queue) -> {
                    Deque<Instant> q = (queue == null) ? new ArrayDeque<>() : queue;
                    while (!q.isEmpty() && q.peekFirst().isBefore(windowStart)) {
                        q.pollFirst();
                    }
                    if (q.size() >= maxRequests) {
                        allowed[0] = false;
                    } else {
                        q.offerLast(now);
                    }
                    return q.isEmpty() ? null : q;
                });
        return allowed[0];
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
