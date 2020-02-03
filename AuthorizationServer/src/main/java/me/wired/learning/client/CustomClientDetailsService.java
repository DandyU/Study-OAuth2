package me.wired.learning.client;

import lombok.RequiredArgsConstructor;
import me.wired.learning.client.repository.OAuthClientDetails;
import me.wired.learning.client.repository.OAuthClientDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomClientDetailsService implements ClientDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomClientDetailsService.class);
    private final OAuthClientDetailsService oAuthClientDetailsService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Optional<OAuthClientDetails> clientDetails = oAuthClientDetailsService.findByClientId(clientId);
        if (!clientDetails.isPresent())
            throw new NoSuchClientException("No client with requested id: " + clientId);

        return clientDetails.get();
    }

}
