package com.revente.backend.application.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpSessionManager {

    // Helper to store: Phone -> SessionInfo (from Google Identity Toolkit)
    // In production, this should be Redis with TTL.
    private final Map<String, String> sessionStore = new ConcurrentHashMap<>();

    public void saveSession(String phone, String sessionInfo) {
        sessionStore.put(phone, sessionInfo);
    }

    public String getSession(String phone) {
        return sessionStore.get(phone);
    }

    public void removeSession(String phone) {
        sessionStore.remove(phone);
    }
}
