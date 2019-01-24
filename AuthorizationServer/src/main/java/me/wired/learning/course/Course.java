package me.wired.learning.course;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.wired.learning.user.XUser;
import me.wired.learning.user.XUserSerializer;

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
    @JsonSerialize(using = XUserSerializer.class)
    private XUser user;

    @PrePersist
    private void initId(){
        setId(UUID.randomUUID().toString());
    }

    public void update() {
        if (defaultPrice == 0 && sellingPrice == 0)
            free = true;
        else
            free = false;

        if (location == null || location.trim().isEmpty())
            offline = false;
        else
            offline = true;
    }
}
