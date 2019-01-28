package me.wired.learning.client.repository;

import java.util.Optional;

public interface OAuthClientDetailsService {

    Optional<OAuthClientDetails> findByClientId(String clientId);

    OAuthClientDetails save(OAuthClientDetails clientDetails);

}
