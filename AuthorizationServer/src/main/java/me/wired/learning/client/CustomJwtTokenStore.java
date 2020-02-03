package me.wired.learning.client;

import me.wired.learning.client.repository.OAuthRefreshToken;
import me.wired.learning.client.repository.OAuthRefreshTokenService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class CustomJwtTokenStore extends JwtTokenStore {

    private OAuthRefreshTokenService oAuthRefreshTokenService;

    public CustomJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer, OAuthRefreshTokenService oAuthRefreshTokenService) {
        super(jwtTokenEnhancer);
        this.oAuthRefreshTokenService = oAuthRefreshTokenService;
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        super.readRefreshToken(tokenValue);

        String tokenId = extractTokenKey(tokenValue);
        Optional<OAuthRefreshToken> optionalTokenInfo = oAuthRefreshTokenService.readRefreshToken(tokenId);
        if (!optionalTokenInfo.isPresent())
            return null;

        return SerializationUtils.deserialize(optionalTokenInfo.get().getToken());
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        super.storeRefreshToken(refreshToken, authentication);

        final byte[] refreshTokenByteArray = SerializationUtils.serialize(refreshToken);
        final byte[] authenticationByteArray = SerializationUtils.serialize(authentication);
        OAuthRefreshToken token = OAuthRefreshToken.builder()
                .tokenId(extractTokenKey(refreshToken.getValue()))
                .token(refreshTokenByteArray)
                .authentication(authenticationByteArray)
                .build();
        OAuthRefreshToken temp = oAuthRefreshTokenService.storeRefreshToken(token);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        super.removeRefreshToken(token);

        oAuthRefreshTokenService.removeRefreshToken(extractTokenKey(token.getValue()));
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        super.storeAccessToken(token, authentication);

        // TODO: Access Token도 DB 관리를 하고 싶으면 구현 필요
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        super.removeAccessToken(token);

        // TODO: Access Token도 DB 관리를 하고 싶으면 구현 필요
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        super.removeAccessTokenUsingRefreshToken(refreshToken);

        // TODO: Access Token도 DB 관리를 하고 싶으면 구현 필요
    }

    private String extractTokenKey(String value) {
        if (value == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(value.getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }

}
