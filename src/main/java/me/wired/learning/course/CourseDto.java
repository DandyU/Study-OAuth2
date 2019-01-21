package me.wired.learning.course;

import lombok.*;
import me.wired.learning.user.XUser;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private LocalDateTime startEnrollmentDateTime;

    @NotNull
    private LocalDateTime endEnrollmentDateTime;

    @NotNull
    private LocalDateTime startCourseDateTime;

    @NotNull
    private LocalDateTime endCourseDateTime;

    private String location;

    @Min(0)
    private long defaultPrice;

    @Min(0)
    private long sellingPrice;

    @Min(0)
    private int maxEnrollment;

}
