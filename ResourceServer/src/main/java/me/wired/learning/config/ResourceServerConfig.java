package me.wired.learning.config;

import lombok.RequiredArgsConstructor;
import me.wired.learning.yaml.PreUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Configuration
@EnableResourceServer
@EnableConfigurationProperties(PreUsers.class) // Configuration에서 Component를 사용하기 위함
@RequiredArgsConstructor
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final PreUsers preUsers;
    private final TokenStore jwtTokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("course")
        .tokenStore(jwtTokenStore)
        ;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.anonymous()
                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/api/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/users").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler())
                ;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(getKey()); // get key from auth
        //converter.setSigningKey("secret"); // static key
        //converter.setVerifierKey(publicKey);
        return converter;
    }

    private String getKey() {
        final URI uri;
        try {
            uri = new URI("http://localhost:8081/oauth/token_key");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        final String clientInfo = Base64Utils.encodeToString((preUsers.getClientId() + ":" + preUsers.getClientSecret()).getBytes());
        final String basicAuth = "Basic " + clientInfo;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", basicAuth);
        ResponseEntity<String> response = new RestTemplate().exchange(uri, HttpMethod.GET, new HttpEntity(headers), String.class);
        Map<String, Object> map = new Jackson2JsonParser().parseMap(response.getBody());
        if (!map.containsKey("value"))
            return null;

        return (String) map.get("value");
    }
}
