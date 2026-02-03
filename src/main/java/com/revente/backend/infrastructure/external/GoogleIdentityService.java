package com.revente.backend.infrastructure.external;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.revente.backend.infrastructure.exception.custom.FirebaseAuthenticationException;

@Service
public class GoogleIdentityService {

    @Value("${firebase.api-key}")
    private String apiKey;

    @Value("${firebase.otp.mock-mode:false}")
    private boolean mockMode;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String SEND_OTP_URL = "https://identitytoolkit.googleapis.com/v1/accounts:sendVerificationCode?key=";
    private static final String VERIFY_OTP_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPhoneNumber?key=";

    public String sendOtp(String phone) {
        if (mockMode) {
            System.out.println("----- MOCK OTP MODE -----");
            System.out.println("Sending OTP to: " + phone);
            System.out.println("Code: 123456");
            System.out.println("-------------------------");
            return "MOCK_SESSION_" + phone;
        }

        String url = SEND_OTP_URL + apiKey;

        Map<String, String> body = new HashMap<>();
        body.put("phoneNumber", phone);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
            if (response != null && response.containsKey("sessionInfo")) {
                return (String) response.get("sessionInfo");
            }
            throw new FirebaseAuthenticationException("No se pudo obtener sessionInfo de Google");
        } catch (Exception e) {
            throw new FirebaseAuthenticationException("Error al enviar OTP: " + e.getMessage());
        }
    }

    public String verifyOtp(String sessionInfo, String code) {
        if (mockMode && sessionInfo.startsWith("MOCK_SESSION_")) {
            if ("123456".equals(code)) {
                String phone = sessionInfo.replace("MOCK_SESSION_", "");
                return "MOCK_ID_TOKEN:" + phone;
            }
            throw new FirebaseAuthenticationException("Código OTP simulado inválido (Use 123456)");
        }

        String url = VERIFY_OTP_URL + apiKey;

        Map<String, String> body = new HashMap<>();
        body.put("sessionInfo", sessionInfo);
        body.put("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
            if (response != null && response.containsKey("idToken")) {
                return (String) response.get("idToken"); // Returns the Firebase ID Token
            }
            throw new FirebaseAuthenticationException("Validación fallida o respuesta incompleta de Google");
        } catch (Exception e) {
            throw new FirebaseAuthenticationException("Error al verificar OTP: " + e.getMessage());
        }
    }
}
