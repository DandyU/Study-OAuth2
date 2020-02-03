package me.wired.learning.client.repository;

import java.util.Optional;

public interface OAuthRefreshTokenService {

    OAuthRefreshToken storeRefreshToken(OAuthRefreshToken token);

    void removeRefreshToken(String tokenId);

    Optional<OAuthRefreshToken> readRefreshToken(String tokenId);

}
