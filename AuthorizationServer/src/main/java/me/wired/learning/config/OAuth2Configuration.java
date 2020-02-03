package me.wired.learning.config;

import lombok.RequiredArgsConstructor;
import me.wired.learning.client.CustomJwtTokenStore;
import me.wired.learning.client.repository.OAuthClientDetailsService;
import me.wired.learning.client.repository.OAuthRefreshTokenService;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@RequiredArgsConstructor
public class OAuth2Configuration {

    private final OAuthClientDetailsService oAuthClientDetailsService;
    private final OAuthRefreshTokenService oAuthRefreshTokenService;

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new CustomJwtTokenStore(jwtAccessTokenConverter, oAuthRefreshTokenService);
        //return new JwtTokenStore(jwtAccessTokenConverter); DB에 Refresh Token 사용을 위해서 CustomJwtTokenStore 사용
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(ResourceServerProperties resourceServerProperties) {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(resourceServerProperties.getJwt().getKeyValue());
        return accessTokenConverter;
    }

    /*@Bean AuthorizationServerConfig::configure(ClientDetailsServiceConfigurer clients)에서 설정하도록 함
    @Primary
    public ClientDetailsService newClientDetailsService() {
        return new CustomClientDetailsService(oAuthClientDetailsService);
    }*/

    /*@Bean
    @Primary
    public DefaultTokenServices tokenService(TokenStore tokenStore) {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        return defaultTokenServices;
    }*/

}
