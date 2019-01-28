package me.wired.learning.config;

import me.wired.learning.client.repository.OAuthClientDetails;
import me.wired.learning.client.repository.OAuthClientDetailsService;
import me.wired.learning.user.XUser;
import me.wired.learning.user.XUserRole;
import me.wired.learning.user.XUserService;
import me.wired.learning.yaml.PreUsers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            PreUsers preUsers;

            @Autowired
            XUserService xUserService;

            @Autowired
            OAuthClientDetailsService oAuthClientDetailsService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                // APP 구동 시에 수행할 작업 등록
                init();
            }

            private void init() {
                if (!xUserService.findByVariableId(preUsers.getAdminVariableId()).isPresent()) {
                    XUser user = XUser.builder()
                            .variableId(preUsers.getAdminVariableId())
                            .password(preUsers.getAdminPassword())
                            .roles(new HashSet<>(Arrays.asList(XUserRole.ADMIN)))
                            .build();
                    xUserService.save(user);
                }
                if (!xUserService.findByVariableId(preUsers.getUserVariableId()).isPresent()) {
                    XUser user = XUser.builder()
                            .variableId(preUsers.getUserVariableId())
                            .password(preUsers.getUserPassword())
                            .roles(new HashSet<>(Arrays.asList(XUserRole.USER)))
                            .build();
                    xUserService.save(user);
                }
                if (!oAuthClientDetailsService.findByClientId(preUsers.getClientId()).isPresent()) {
                    OAuthClientDetails clientDetails = OAuthClientDetails.builder()
                            .clientId(preUsers.getClientId())
                            .clientSecret(preUsers.getClientSecret())
                            .authorizedGrantTypes("password,refresh_token")
                            .scope("read,write")
                            .accessTokenValidity(new Integer(30 * 60))
                            .refreshTokenValidity(new Integer(60 * 60))
                            .build();
                    oAuthClientDetailsService.save(clientDetails);
                }
            }

        };
    }
}
