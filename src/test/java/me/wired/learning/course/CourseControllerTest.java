package me.wired.learning.course;

import me.wired.learning.common.BaseControllerTest;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CourseControllerTest extends BaseControllerTest {

    private CourseDto newCourseDto(int i)  {
        return CourseDto.builder()
                .name("강좌 " + i)
                .description("강좌 설명 " + i)
                .startEnrollmentDateTime(LocalDateTime.of(2019, 2, 1, 00, 0))
                .endEnrollmentDateTime(LocalDateTime.of(2019, 2, 28, 24, 0))
                .startCourseDateTime(LocalDateTime.of(2019, 2, 7, 00, 0))
                .endCourseDateTime(LocalDateTime.of(2019, 2, 28, 24, 0))
                .location(null)
                .defaultPrice(100000)
                .sellingPrice(50000)
                .maxEnrollment(100)
                .build();
    }

    @Test
    public void testCreateCourse() throws Exception {
        CourseDto courseDto = newCourseDto(1);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(courseDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("offline").value(false))
                .andExpect(jsonPath("free").value(false))
        ;
    }

    @Test
    public void testReadCourse() {
    }

    @Test
    public void testReadCourses() {
    }

    @Test
    public void testUpdateCourse() {
    }

    @Test
    public void testDeleteCourse() {
    }

    @Test
    public void testDeleteCourses() {
    }

}