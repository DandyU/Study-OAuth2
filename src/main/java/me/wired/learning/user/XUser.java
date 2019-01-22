package me.wired.learning.user;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "variableId"})
@Builder
@Entity
public class XUser {

    @Id
    private String id;

    private String variableId;

    private String password;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<XUserRole> roles;

    @PrePersist
    private void initId(){
        this.setId(UUID.randomUUID().toString());
    }

}
