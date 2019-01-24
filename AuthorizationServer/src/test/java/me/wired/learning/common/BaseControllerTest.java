package me.wired.learning.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.wired.learning.yaml.PreUsers;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles(value = "test")
@Ignore // Disable BaseControllerTest
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected ModelMapper modelMapper;
    @Autowired
    protected PreUsers preUsers;

    protected String getAdminBearerToken(String prefixId) throws Exception {
        //OAuth2의 Grant Type 중 Password, Refresh Token만 지원
        final String clientId = preUsers.getClientId();
        final String clientSecret = preUsers.getClientSecret();
        final String userVariableId = prefixId == null ? preUsers.getAdminVariableId() : prefixId + preUsers.getAdminVariableId();
        final String userPassword = preUsers.getAdminPassword();

        ResultActions resultActions = mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", userVariableId)
                .param("password", userPassword)
                .param("grant_type", "password"))
                .andDo(print());
        String jsonData = resultActions.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        String bearerToken = OAuth2AccessToken.BEARER_TYPE + " " + parser.parseMap(jsonData).get(OAuth2AccessToken.ACCESS_TOKEN).toString();
        return bearerToken;
    }

    protected String getUserBearerToken(String prefixId) throws Exception {
        //OAuth2의 Grant Type 중 Password, Refresh Token만 지원
        final String clientId = preUsers.getClientId();
        final String clientSecret = preUsers.getClientSecret();
        final String userVariableId = prefixId == null ? preUsers.getUserVariableId() : prefixId + preUsers.getUserVariableId();
        final String userPassword = preUsers.getUserPassword();

        ResultActions resultActions = mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", userVariableId)
                .param("password", userPassword)
                .param("grant_type", "password"))
                .andDo(print());
        String jsonData = resultActions.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        String bearerToken = OAuth2AccessToken.BEARER_TYPE + " " + parser.parseMap(jsonData).get(OAuth2AccessToken.ACCESS_TOKEN).toString();
        return bearerToken;
    }

}
