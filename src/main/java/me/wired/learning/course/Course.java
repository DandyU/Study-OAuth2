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
@EqualsAndHashCode (of = {"id"})
@Builder
@Entity
public class Course {

    @Id
    private String id;

    private String name;

    private String description;

    private LocalDateTime startEnrollmentDateTime;

    private LocalDateTime endEnrollmentDateTime;

    private LocalDateTime startCourseDateTime;

    private LocalDateTime endCourseDateTime;

    private String location;

    private long defaultPrice;

    private long sellingPrice;

    private int maxEnrollment;

    private boolean offline;

    private boolean free;

    @ManyToOne
    private XUser user;

    @PrePersist
    private void initId(){
        this.setId(UUID.randomUUID().toString());
    }

}
