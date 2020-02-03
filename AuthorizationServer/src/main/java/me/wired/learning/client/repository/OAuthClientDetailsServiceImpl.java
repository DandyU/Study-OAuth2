package me.wired.learning.client.repository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OAuthClientDetailsServiceImpl implements OAuthClientDetailsService {

    private final OAuthClientDetailsRepository oAuthClientDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public OAuthClientDetailsServiceImpl(OAuthClientDetailsRepository oAuthClientDetailsRepository, PasswordEncoder passwordEncoder) {
        this.oAuthClientDetailsRepository = oAuthClientDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuthClientDetails save(OAuthClientDetails clientDetails) {
        clientDetails.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()));
        return oAuthClientDetailsRepository.save(clientDetails);
    }

    @Override
    public Optional<OAuthClientDetails> findByClientId(String clientId) {
        return oAuthClientDetailsRepository.findByClientId(clientId);
    }

}
