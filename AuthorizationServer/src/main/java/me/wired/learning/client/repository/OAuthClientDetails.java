package me.wired.learning.client.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.IOException;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"clientId"})
public class OAuthClientDetails implements ClientDetails {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Id
    @Column(nullable = false, updatable = false)
    private String id;
    @Column(nullable = false, unique = true)
    private String clientId;
    private String resourceIds;
    private String clientSecret;
    private String scope;
    @Column(nullable = false)
    private String authorizedGrantTypes;
    private String registeredRedirectUri;
    private String authorities;
    @Column(nullable = false)
    private Integer accessTokenValiditySeconds;
    @Column(nullable = false)
    private Integer refreshTokenValiditySeconds;
    private String autoApproveScope;
    private String additionalInformation;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @PrePersist
    private void initId(){
        this.setId(UUID.randomUUID().toString());
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        if (StringUtils.isEmpty(this.resourceIds)) {
            return new HashSet<>();
        } else {
            return StringUtils.commaDelimitedListToSet(this.resourceIds);
        }
    }

    @Override
    public boolean isSecretRequired() {
        return !StringUtils.isEmpty(this.clientSecret);
    }

    @Override
    public String getClientSecret() {
        return this.clientSecret;
    }

    @Override
    public boolean isScoped() {
        return this.getScope().size() > 0;
    }

    @Override
    public Set<String> getScope() {
        return StringUtils.commaDelimitedListToSet(this.scope);
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return StringUtils.commaDelimitedListToSet(this.authorizedGrantTypes);
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return StringUtils.commaDelimitedListToSet(this.registeredRedirectUri);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Set<String> set = StringUtils.commaDelimitedListToSet(this.authorities);
        Set<GrantedAuthority> result = new HashSet<>();
        set.forEach(authority -> result.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return authority;
            }
        }));
        return result;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return this.getAutoApproveScope().contains(scope);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAdditionalInformation() {
        try {
            return mapper.readValue(this.additionalInformation, Map.class);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    public Set<String> getAutoApproveScope() {
        return StringUtils.commaDelimitedListToSet(this.autoApproveScope);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = StringUtils.collectionToCommaDelimitedString(resourceIds);
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setScope(Set<String> scope) {
        this.scope = StringUtils.collectionToCommaDelimitedString(scope);
    }

    public void setAuthorizedGrantTypes(Set<String> authorizedGrantType) {
        this.authorizedGrantTypes = StringUtils.collectionToCommaDelimitedString(authorizedGrantType);
    }

    public void setRegisteredRedirectUri(Set<String> registeredRedirectUriList) {
        this.registeredRedirectUri = StringUtils.collectionToCommaDelimitedString(registeredRedirectUriList);
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = StringUtils.collectionToCommaDelimitedString(authorities);
    }

    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public void setAutoApproveScope(Set<String> autoApproveScope) {
        this.autoApproveScope = StringUtils.collectionToCommaDelimitedString(autoApproveScope);
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        try {
            this.additionalInformation = mapper.writeValueAsString(additionalInformation);
        } catch (IOException e) {
            this.additionalInformation = "";
        }
    }

}
