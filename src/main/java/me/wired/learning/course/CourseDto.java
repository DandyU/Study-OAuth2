package me.wired.learning.course;

import lombok.*;
import me.wired.learning.user.XUser;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseDto {

    private String name;

    private String description;

    private LocalDateTime startEnrollmentDateTime;

    private LocalDateTime endEnrollmentDateTime;

    private LocalDateTime startCourseDateTime;

    private LocalDateTime endCourseDateTime;

    private String location;

    private int defaultPrice;

    private int sellingPrice;

    private int maxEnrollment;

}
