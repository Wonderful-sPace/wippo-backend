package com.example.wippo.domain.auth.social.provider;

import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.example.wippo.domain.auth.social.Provider;

@Component
public class SocialClientRegistry {
    private final Map<Provider, SocialClient> byProvider;

    public SocialClientRegistry(List<SocialClient> clients) {
        this.byProvider = clients.stream()
                .collect(Collectors.toMap(SocialClient::provider, Function.identity()));
    }

    public SocialClient require(Provider provider) {
        var c = byProvider.get(provider);
        if (c == null) throw new IllegalArgumentException("Unsupported provider: " + provider);
        return c;
    }
}
