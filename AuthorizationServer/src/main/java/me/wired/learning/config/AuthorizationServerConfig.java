package me.wired.learning.config;

import me.wired.learning.client.CustomClientDetailsService;
import me.wired.learning.user.XUserDetailService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends OAuth2AuthorizationServerConfiguration {

    private final PasswordEncoder passwordEncoder;
    private final XUserDetailService xUserDetailService;
    private final CustomClientDetailsService customClientDetailsService;
    private final TokenStore tokenStore;
    private final AccessTokenConverter tokenConverter;
    private final BaseClientDetails baseClientDetails;
    private final AuthenticationConfiguration authenticationConfiguration;

    public AuthorizationServerConfig(PasswordEncoder passwordEncoder,
                                     XUserDetailService xUserDetailService,
                                     BaseClientDetails baseClientDetails,
                                     AuthenticationConfiguration authenticationConfiguration,
                                     ObjectProvider<TokenStore> tokenStore,
                                     ObjectProvider<AccessTokenConverter> tokenConverter,
                                     AuthorizationServerProperties properties,
                                     CustomClientDetailsService customClientDetailsService) throws Exception {
        super(baseClientDetails, authenticationConfiguration, tokenStore, tokenConverter, properties);
        this.passwordEncoder = passwordEncoder;
        this.xUserDetailService = xUserDetailService;
        this.tokenConverter = tokenConverter.getObject();
        this.tokenStore = tokenStore.getObject();
        this.baseClientDetails = baseClientDetails;
        this.authenticationConfiguration = authenticationConfiguration;
        this.customClientDetailsService = customClientDetailsService;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder)
                .tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("isAuthenticated()")
        ;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*clients.inMemory()
                .withClient(preUsers.getClientId())
                .secret(passwordEncoder.encode(preUsers.getClientSecret()))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
                .accessTokenValiditySeconds(10 * 60)
                .refreshTokenValiditySeconds(30 * 60)
                ;*/

        clients.withClientDetails(customClientDetailsService); // ClientDetails를 DB를 참조하도록 함
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        /*endpoints.authenticationManager(authenticationManager)
                //.tokenStore(new InMemoryTokenStore())
                .tokenStore(tokenStore)
                ;*/

        endpoints.userDetailsService(xUserDetailService);
        endpoints.reuseRefreshTokens(false);
        if (this.tokenConverter != null)
            endpoints.accessTokenConverter(tokenConverter);
        if (this.tokenStore != null)
            endpoints.tokenStore(tokenStore);
        if (this.baseClientDetails.getAuthorizedGrantTypes().contains("password"))
            endpoints.authenticationManager(authenticationConfiguration.getAuthenticationManager());
    }

}

