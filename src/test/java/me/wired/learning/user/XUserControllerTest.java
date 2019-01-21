package me.wired.learning.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.wired.learning.common.BaseControllerTest;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashSet;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class XUserControllerTest extends BaseControllerTest {

    @Test
    public void createUser() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(newNormalXUserDto())))
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }

    public XUserDto newNormalXUserDto() {
        return XUserDto.builder()
                .subId("test@gmail.com")
                .name("Steven Allan Spielberg")
                .password("12345")
                .roles(new HashSet<>(Arrays.asList(XUserRole.ADMIN, XUserRole.USER)))
                .build();
    }
}