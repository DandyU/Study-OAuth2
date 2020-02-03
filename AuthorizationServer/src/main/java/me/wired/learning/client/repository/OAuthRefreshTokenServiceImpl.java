package me.wired.learning.client.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthRefreshTokenServiceImpl implements OAuthRefreshTokenService {

    private final OAuthRefreshTokenRepository oAuthRefreshTokenRepository;

    @Override
    public OAuthRefreshToken storeRefreshToken(OAuthRefreshToken token) {
        return oAuthRefreshTokenRepository.save(token);
    }

    @Override
    public void removeRefreshToken(String tokenId) {
        oAuthRefreshTokenRepository.deleteById(tokenId);
    }

    @Override
    public Optional<OAuthRefreshToken> readRefreshToken(String tokenId) {
        return oAuthRefreshTokenRepository.findById(tokenId);
    }

}
