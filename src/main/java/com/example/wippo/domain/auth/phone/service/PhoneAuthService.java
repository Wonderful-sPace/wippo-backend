package com.example.wippo.domain.auth.phone.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.wippo.domain.auth.phone.entity.PhoneVerification;
import com.example.wippo.domain.auth.phone.repository.PhoneVerificationRepository;

import lombok.RequiredArgsConstructor;

// MVP: 간단한 6자리 코드 + 해시
@Service
@RequiredArgsConstructor
public class PhoneAuthService {
    private final PhoneVerificationRepository repo;
    private final SmsSender sms;
    private final SecureRandom rng = new SecureRandom();

    @Value("${wippo.phone.code.pepper}") private String pepper;
    @Value("${wippo.phone.code.ttl-minutes:3}") private int ttlMinutes;
    @Value("${wippo.phone.code.max-attempts:5}") private int maxAttempts;

    public void sendCode(String phoneE164){
        String code = String.format("%06d", rng.nextInt(1_000_000));
        String salt = java.util.UUID.randomUUID().toString().replace("-", "");
        String hash = sha256Hex(code + ":" + salt + ":" + pepper);

        // 같은 번호의 이전 레코드를 정리하고 새로 저장(선택)
        // repo.deleteAllByPhoneNumber(phoneE164);

        repo.save(PhoneVerification.builder()
            .phoneNumber(phoneE164)
            .codeHash(hash)
            .salt(salt)
            .expiresAt(LocalDateTime.now().plusMinutes(ttlMinutes))
            .attemptCount(0)
            .build());

        sms.send(phoneE164, "[Wippo] 인증코드 " + code + " (" + ttlMinutes + "분 이내 유효)");
    }


    @jakarta.transaction.Transactional
    public boolean verifyCode(String phoneE164, String code){
        var pv = repo.findTopByPhoneNumberOrderByIdDesc(phoneE164)
            .orElseThrow(() -> new IllegalStateException("code not found"));

        if (pv.isExpired()) return false;
        if (pv.getAttemptCount() >= maxAttempts) return false;

        String computed = sha256Hex(code + ":" + pv.getSalt() + ":" + pepper);
        boolean ok = constantTimeEquals(computed, pv.getCodeHash());

        pv.setAttemptCount(pv.getAttemptCount() + 1);
        if (ok) repo.deleteById(pv.getId()); 
        else repo.save(pv);
        
        return ok;
    }
    
    private String sha256Hex(String s) {
        try {
            var md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            var sb = new StringBuilder(out.length * 2);
            for (byte b : out) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        byte[] x = a.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] y = b.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (x.length != y.length) return false;
        int res = 0;
        for (int i = 0; i < x.length; i++) res |= x[i] ^ y[i];
        return res == 0;
    }

    public interface SmsSender { void send(String phoneE164, String message); }
}
