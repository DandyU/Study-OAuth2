package me.wired.learning.course;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CourseGenerator {

    public static CourseDto newNormalCourseDto(int i)  {
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

    public static CourseDto newWrongCourseDto1(int i)  {
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

    public static CourseDto newWrongCourseDto2(int i)  {
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
