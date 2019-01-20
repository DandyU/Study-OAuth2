package me.wired.learning.course;

import me.wired.learning.common.BaseControllerTest;
import me.wired.learning.common.TestDescription;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CourseControllerTest extends BaseControllerTest {

    @Test
    @TestDescription("정상 Course 생성")
    public void testCreateCourse() throws Exception {
        CourseDto courseDto = newNormalCourseDto(1);

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
    @TestDescription("비정상 Prices Course 생성")
    public void testCreateWrongCourse1() throws Exception {
        CourseDto courseDto = newWrongCourseDto1(1);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(courseDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("비정상 일시 Course 생성")
    public void testCreateWrongCourse2() throws Exception {
        CourseDto courseDto = newWrongCourseDto2(1);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(courseDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    private CourseDto newNormalCourseDto(int i)  {
        return CourseDto.builder()
                .name("강좌 " + i)
                .description("강좌 설명 " + i)
                .startEnrollmentDateTime(LocalDateTime.of(2019, 2, 1, 0, 0))
                .endEnrollmentDateTime(LocalDateTime.of(2019, 2, 28, 23, 59))
                .startCourseDateTime(LocalDateTime.of(2019, 2, 7, 0, 0))
                .endCourseDateTime(LocalDateTime.of(2019, 2, 28, 23, 59))
                .location(null)
                .defaultPrice(100000)
                .sellingPrice(50000)
                .maxEnrollment(100)
                .build();
    }

    private CourseDto newWrongCourseDto1(int i)  {
        return CourseDto.builder()
                .name("강좌 " + i)
                .description("강좌 설명 " + i)
                .startEnrollmentDateTime(LocalDateTime.of(2019, 2, 1, 0, 0))
                .endEnrollmentDateTime(LocalDateTime.of(2019, 2, 28, 23, 59))
                .startCourseDateTime(LocalDateTime.of(2019, 2, 7, 0, 0))
                .endCourseDateTime(LocalDateTime.of(2019, 2, 28, 23, 59))
                .location(null)
                .defaultPrice(100000)
                .sellingPrice(150000)
                .maxEnrollment(100)
                .build();
    }

    private CourseDto newWrongCourseDto2(int i)  {
        return CourseDto.builder()
                .name("강좌 " + i)
                .description("강좌 설명 " + i)
                .startEnrollmentDateTime(LocalDateTime.of(2019, 2, 1, 0, 0))
                .endEnrollmentDateTime(LocalDateTime.of(2019, 2, 20, 23, 59))
                .startCourseDateTime(LocalDateTime.of(2019, 2, 7, 0, 0))
                .endCourseDateTime(LocalDateTime.of(2019, 2, 28, 23, 59))
                .location(null)
                .defaultPrice(100000)
                .sellingPrice(50000)
                .maxEnrollment(100)
                .build();
    }

}