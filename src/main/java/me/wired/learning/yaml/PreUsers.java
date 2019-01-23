package me.wired.learning.yaml;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pre-user")
@Getter
@Setter
public class PreUsers {

    String adminVariableId;
    String adminPassword;
    String userVariableId;
    String userPassword;
    String clientId;
    String clientSecret;

}
