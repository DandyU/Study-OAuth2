package me.wired.learning.config;

import me.wired.learning.yaml.PreUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
@EnableConfigurationProperties(PreUsers.class) // Configuration에서 Component를 사용하기 위함
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    RemoteTokenServices remoteTokenServices; // this.getRemoteTokenServices()

    @Autowired
    PreUsers preUsers;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("course")
        .tokenServices(remoteTokenServices)
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
    public RemoteTokenServices getRemoteTokenServices(@Autowired PreUsers preUsers) {
        RemoteTokenServices remoteTokenService = new RemoteTokenServices();
        remoteTokenService.setCheckTokenEndpointUrl("http://localhost:8081/oauth/check_token");
        remoteTokenService.setClientId(preUsers.getClientId());
        remoteTokenService.setClientSecret(preUsers.getClientSecret());
        return remoteTokenService;
    }

}
