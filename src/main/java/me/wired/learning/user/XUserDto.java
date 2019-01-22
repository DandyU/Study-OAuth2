package me.wired.learning.user;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

import static me.wired.learning.common.RegularExpression.REGEX_EMAIL_VALIDATION;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class XUserDto {

    @NotEmpty
    @Pattern(regexp = REGEX_EMAIL_VALIDATION)
    @Size(min = 1, max = 254)
    private String variableId;

    @NotEmpty
    @Size(min = 5, max = 64)
    private String password;

    @NotEmpty
    @Size(min = 2, max = 128)
    private String name;

    @NotEmpty
    private Set<XUserRole> roles;

}
