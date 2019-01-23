package me.wired.learning.course;

import me.wired.learning.common.BaseControllerTest;
import me.wired.learning.common.TestDescription;
import me.wired.learning.user.XUser;
import me.wired.learning.user.XUserRole;
import me.wired.learning.user.XUserService;
import me.wired.learning.yaml.PreUsers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CourseControllerTest extends BaseControllerTest {

    @Autowired
    PreUsers preUsers;

    @Autowired
    XUserService xUserService;

    private String getAdminBearerToken() throws Exception {
        //OAuth2의 Grant Type 중 Password, Refresh Token만 지원
        final String clientId = preUsers.getClientId();
        final String clientSecret = preUsers.getClientSecret();
        final String userVariableId = preUsers.getAdminVariableId();
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

    private String getUserBearerToken() throws Exception {
        //OAuth2의 Grant Type 중 Password, Refresh Token만 지원
        final String clientId = preUsers.getClientId();
        final String clientSecret = preUsers.getClientSecret();
        final String userVariableId = preUsers.getUserVariableId();
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

    @Test
    @TestDescription("정상 Course 생성")
    public void testCreateCourse() throws Exception {
        CourseDto courseDto = CourseGenerator.newNormalCourseDto(1);

        mockMvc.perform(post("/api/courses")
                .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(courseDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("offline").value(false))
                .andExpect(jsonPath("free").value(false))
                .andDo(document("create-course",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type header"),
                                headerWithName(HttpHeaders.ACCEPT).description("Accept Header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Course name"),
                                fieldWithPath("description").description("Course description"),
                                fieldWithPath("startEnrollmentDateTime").description("Course startEnrollmentDateTime"),
                                fieldWithPath("endEnrollmentDateTime").description("Course endEnrollmentDateTime"),
                                fieldWithPath("startCourseDateTime").description("Course startCourseDateTime"),
                                fieldWithPath("endCourseDateTime").description("Course endCourseDateTime"),
                                fieldWithPath("location").description("Course location"),
                                fieldWithPath("defaultPrice").description("Course defaultPrice"),
                                fieldWithPath("sellingPrice").description("Course sellingPrice"),
                                fieldWithPath("maxEnrollment").description("Course maxEnrollment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type header"),
                                headerWithName(HttpHeaders.LOCATION).description("Location header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Course ID"),
                                fieldWithPath("name").description("Course name"),
                                fieldWithPath("description").description("Course description"),
                                fieldWithPath("startEnrollmentDateTime").description("Course startEnrollmentDateTime"),
                                fieldWithPath("endEnrollmentDateTime").description("Course endEnrollmentDateTime"),
                                fieldWithPath("startCourseDateTime").description("Course startCourseDateTime"),
                                fieldWithPath("endCourseDateTime").description("Course endCourseDateTime"),
                                fieldWithPath("location").description("Course location"),
                                fieldWithPath("defaultPrice").description("Course defaultPrice"),
                                fieldWithPath("sellingPrice").description("Course sellingPrice"),
                                fieldWithPath("maxEnrollment").description("Course maxEnrollment"),
                                fieldWithPath("offline").description("Course offline"),
                                fieldWithPath("free").description("Course free"),
                                fieldWithPath("user").description("Course owner"),
                                fieldWithPath("user.id").description("ID of user"),
                                fieldWithPath("_links.self.href").description("Link to self"),
                                fieldWithPath("_links.update-course.href").description("Link to update"),
                                fieldWithPath("_links.delete-course.href").description("Link to delete"),
                                fieldWithPath("_links.profile.href").description("Link to profile")
                        ),
                        links(
                                linkWithRel("self").description("Link to self"),
                                linkWithRel("update-course").description("Link to update"),
                                linkWithRel("delete-course").description("Link to delete"),
                                linkWithRel("profile").description("Link to profile")
                        )
                ));
    }

    @Test
    @TestDescription("비정상 Prices Course 생성")
    public void testCreateWrongCourse1() throws Exception {
        CourseDto courseDto = CourseGenerator.newWrongCourseDto1(1);

        mockMvc.perform(post("/api/courses")
                .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(courseDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("잘못된 일시로 Course 생성")
    public void testCreateWrongCourse2() throws Exception {
        CourseDto courseDto = CourseGenerator.newWrongCourseDto2(1);

        mockMvc.perform(post("/api/courses")
                .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(courseDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("Course 읽기 테스트")
    public void readCourse() throws Exception {
        CourseDto courseDto = CourseGenerator.newNormalCourseDto(100);

        ResultActions resultActions = mockMvc.perform(post("/api/courses")
                .header(HttpHeaders.AUTHORIZATION, getUserBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(courseDto)))
                .andDo(print())
                .andExpect(status().isCreated());

        String responseData = resultActions.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String id = (String) jsonParser.parseMap(responseData).get("id");
        mockMvc.perform(get("/api/courses/{id}", id)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("id").exists())
                .andDo(document("read-course",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept Header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Course ID"),
                                fieldWithPath("name").description("Course name"),
                                fieldWithPath("description").description("Course description"),
                                fieldWithPath("startEnrollmentDateTime").description("Course startEnrollmentDateTime"),
                                fieldWithPath("endEnrollmentDateTime").description("Course endEnrollmentDateTime"),
                                fieldWithPath("startCourseDateTime").description("Course startCourseDateTime"),
                                fieldWithPath("endCourseDateTime").description("Course endCourseDateTime"),
                                fieldWithPath("location").description("Course location"),
                                fieldWithPath("defaultPrice").description("Course defaultPrice"),
                                fieldWithPath("sellingPrice").description("Course sellingPrice"),
                                fieldWithPath("maxEnrollment").description("Course maxEnrollment"),
                                fieldWithPath("offline").description("Course offline"),
                                fieldWithPath("free").description("Course free"),
                                fieldWithPath("user").description("Course owner"),
                                fieldWithPath("user.id").description("ID of user"),
                                fieldWithPath("_links.self.href").description("Link to self"),
                                fieldWithPath("_links.update-course.href").description("Link to update"),
                                fieldWithPath("_links.delete-course.href").description("Link to delete"),
                                fieldWithPath("_links.profile.href").description("Link to profile")
                        ),
                        links(
                                linkWithRel("self").description("Link to self"),
                                linkWithRel("update-course").description("Link to update"),
                                linkWithRel("delete-course").description("Link to delete"),
                                linkWithRel("profile").description("Link to profile")
                        )
                ));
    }

    @Test
    @TestDescription("존재하지 않는 Course 읽기 테스트")
    public void readWrongCourse() throws Exception {
        String id = "CourseNotExists";
        mockMvc.perform(get("/api/courses/{id}", id)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @TestDescription("여러 개의 Course 읽기 테스트(1 페이지, 10개, name/desc 정렬)")
    public void readCourses1() throws Exception {
        final String bearerToken = getUserBearerToken();
        IntStream.range(0, 30).forEach(i -> {
            CourseDto courseDto = CourseGenerator.newNormalCourseDto(i);
            try {
                ResultActions resultActions = mockMvc.perform(post("/api/courses")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                        //.andDo(print())
                        .andExpect(status().isCreated());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        int page = 1;
        int size = 10;
        String sort = "name,DESC";
        mockMvc.perform(get("/api/courses")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort)
                .accept(MediaTypes.HAL_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("read-courses",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept Header")
                        ),
                        requestParameters(
                                parameterWithName("page").description("request page"),
                                parameterWithName("size").description("request size"),
                                parameterWithName("sort").description("request sort")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type header")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.courseList[].id").description("Course ID"),
                                fieldWithPath("_embedded.courseList[].name").description("Course name"),
                                fieldWithPath("_embedded.courseList[].description").description("Course description"),
                                fieldWithPath("_embedded.courseList[].startEnrollmentDateTime").description("Course startEnrollmentDateTime"),
                                fieldWithPath("_embedded.courseList[].endEnrollmentDateTime").description("Course endEnrollmentDateTime"),
                                fieldWithPath("_embedded.courseList[].startCourseDateTime").description("Course startCourseDateTime"),
                                fieldWithPath("_embedded.courseList[].endCourseDateTime").description("Course endCourseDateTime"),
                                fieldWithPath("_embedded.courseList[].location").description("Course location"),
                                fieldWithPath("_embedded.courseList[].defaultPrice").description("Course defaultPrice"),
                                fieldWithPath("_embedded.courseList[].sellingPrice").description("Course sellingPrice"),
                                fieldWithPath("_embedded.courseList[].maxEnrollment").description("Course maxEnrollment"),
                                fieldWithPath("_embedded.courseList[].offline").description("Course offline"),
                                fieldWithPath("_embedded.courseList[].free").description("Course free"),
                                fieldWithPath("_embedded.courseList[].user").description("Course owner"),
                                fieldWithPath("_embedded.courseList[].user.id").description("ID of user"),
                                fieldWithPath("_embedded.courseList[]._links.self.href").description("Link to self"),
                                fieldWithPath("_embedded.courseList[]._links.update-course.href").description("Link to update"),
                                fieldWithPath("_embedded.courseList[]._links.delete-course.href").description("Link to delete"),
                                fieldWithPath("page.size").description("size per page"),
                                fieldWithPath("page.totalElements").description("Total elements"),
                                fieldWithPath("page.totalPages").description("Total pages"),
                                fieldWithPath("page.number").description("number of page"),
                                fieldWithPath("_links.first.href").description("Link to first page"),
                                fieldWithPath("_links.prev.href").description("Link to previous page"),
                                fieldWithPath("_links.self.href").description("Link to self"),
                                fieldWithPath("_links.next.href").description("Link to next page"),
                                fieldWithPath("_links.last.href").description("Link to last page"),
                                fieldWithPath("_links.create-course.href").description("Link to create course"),
                                fieldWithPath("_links.profile.href").description("Link to profile")
                        ),
                        links(
                                linkWithRel("first").description("Link to first page"),
                                linkWithRel("prev").description("Link to previous page"),
                                linkWithRel("self").description("Link to self"),
                                linkWithRel("next").description("Link to next page"),
                                linkWithRel("last").description("Link to last page"),
                                linkWithRel("create-course").description("Link to create course"),
                                linkWithRel("profile").description("Link to profile")
                        )
                ));
    }

    @Test
    @TestDescription("Course가 한 개도 존재하지 않을 때, 여러 개의 Course 읽기 테스트(1 페이지, 10개, name/desc 정렬)")
    public void readCourses2() throws Exception {
        int page = 1;
        int size = 10;
        String sort = "name,DESC";
        mockMvc.perform(get("/api/courses")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @TestDescription("Course 수정 테스트")
    public void updateNormalCourse() throws Exception {
        final String bearerToken = getUserBearerToken();
        CourseDto courseDto = CourseGenerator.newNormalCourseDto(100);
        ResultActions resultActions = mockMvc.perform(post("/api/courses")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(courseDto)))
                .andDo(print())
                .andExpect(status().isCreated());

        String responseData = resultActions.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        final String id = (String) jsonParser.parseMap(responseData).get("id");
        final String newName = "이름 수정";
        final String newDescription = "설명 수정";
        final String newLocation = "마곡중앙로";
        courseDto.setName(newName);
        courseDto.setDescription(newDescription);
        courseDto.setLocation(newLocation);
        courseDto.setDefaultPrice(0);
        courseDto.setSellingPrice(0);

        mockMvc.perform(put("/api/courses/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(courseDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("name").value(newName))
                .andExpect(jsonPath("description").value(newDescription))
                .andExpect(jsonPath("defaultPrice").value(0))
                .andExpect(jsonPath("sellingPrice").value(0))
                .andExpect(jsonPath("location").value(newLocation))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("free").value(true))
                .andDo(document("update-course",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type header"),
                                headerWithName(HttpHeaders.ACCEPT).description("Accept Header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Course name"),
                                fieldWithPath("description").description("Course description"),
                                fieldWithPath("startEnrollmentDateTime").description("Course startEnrollmentDateTime"),
                                fieldWithPath("endEnrollmentDateTime").description("Course endEnrollmentDateTime"),
                                fieldWithPath("startCourseDateTime").description("Course startCourseDateTime"),
                                fieldWithPath("endCourseDateTime").description("Course endCourseDateTime"),
                                fieldWithPath("location").description("Course location"),
                                fieldWithPath("defaultPrice").description("Course defaultPrice"),
                                fieldWithPath("sellingPrice").description("Course sellingPrice"),
                                fieldWithPath("maxEnrollment").description("Course maxEnrollment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Course ID"),
                                fieldWithPath("name").description("Course name"),
                                fieldWithPath("description").description("Course description"),
                                fieldWithPath("startEnrollmentDateTime").description("Course startEnrollmentDateTime"),
                                fieldWithPath("endEnrollmentDateTime").description("Course endEnrollmentDateTime"),
                                fieldWithPath("startCourseDateTime").description("Course startCourseDateTime"),
                                fieldWithPath("endCourseDateTime").description("Course endCourseDateTime"),
                                fieldWithPath("location").description("Course location"),
                                fieldWithPath("defaultPrice").description("Course defaultPrice"),
                                fieldWithPath("sellingPrice").description("Course sellingPrice"),
                                fieldWithPath("maxEnrollment").description("Course maxEnrollment"),
                                fieldWithPath("offline").description("Course offline"),
                                fieldWithPath("free").description("Course free"),
                                fieldWithPath("user").description("Course owner"),
                                fieldWithPath("user.id").description("ID of user"),
                                fieldWithPath("_links.self.href").description("Link to self"),
                                fieldWithPath("_links.update-course.href").description("Link to update"),
                                fieldWithPath("_links.delete-course.href").description("Link to delete"),
                                fieldWithPath("_links.profile.href").description("Link to profile")
                        ),
                        links(
                                linkWithRel("self").description("Link to self"),
                                linkWithRel("update-course").description("Link to update"),
                                linkWithRel("delete-course").description("Link to delete"),
                                linkWithRel("profile").description("Link to profile")
                        )
                ));
    }

    @Test
    @TestDescription("Course 한개 삭제")
    public void deleteCourse() throws Exception {
        final String bearerToken = getUserBearerToken();
        CourseDto courseDto = CourseGenerator.newNormalCourseDto(40);
        ResultActions resultActions = mockMvc.perform(post("/api/courses")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(courseDto)))
                .andDo(print())
                .andExpect(status().isCreated());

        String responseData = resultActions.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String id = (String) jsonParser.parseMap(responseData).get("id");
        mockMvc.perform(delete("/api/courses/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("delete-course"))
        ;
    }

    @Test
    @TestDescription("존재하지 않는 Course 한개 삭제")
    public void deleteWrongCourse() throws Exception {
        String id = "CourseNotExists";
        mockMvc.perform(delete("/api/courses/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, getUserBearerToken()))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @TestDescription("Course 전체 삭제")
    public void deleteCourses() throws Exception {
        final String bearerToken = getAdminBearerToken();
        IntStream.range(0, 30).forEach(i -> {
            CourseDto courseDto = CourseGenerator.newNormalCourseDto(i);
            try {
                ResultActions resultActions = mockMvc.perform(post("/api/courses")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                        .andDo(print())
                        .andExpect(status().isCreated());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mockMvc.perform(delete("/api/courses")
                .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("delete-courses"))
        ;
    }
}
