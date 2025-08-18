package com.example.wippo.domain.auth.onboarding;

import com.example.wippo.domain.auth.token.TokenPair;

public record FinalizeResult(Long userId, String displayName, TokenPair tokens) {

}