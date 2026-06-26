package com.safehome.seismic_portal.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimitService {

    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> windowStart = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS = 10;
    private static final long WINDOW_MS = 60 * 60 * 1000; // 1 hour

    public boolean isAllowed(String ipAddress) {
        long now = System.currentTimeMillis();

        windowStart.putIfAbsent(ipAddress, now);
        requestCounts.putIfAbsent(ipAddress, new AtomicInteger(0));

        long windowStartTime = windowStart.get(ipAddress);

        // Reset window if 1 hour has passed
        if (now - windowStartTime > WINDOW_MS) {
            windowStart.put(ipAddress, now);
            requestCounts.put(ipAddress, new AtomicInteger(0));
        }

        int count = requestCounts.get(ipAddress).incrementAndGet();
        return count <= MAX_REQUESTS;
    }
}