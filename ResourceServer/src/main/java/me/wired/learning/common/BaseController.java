package me.wired.learning.common;

import me.wired.learning.user.XUser;
import me.wired.learning.user.XUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.Errors;

import java.util.Optional;

public class BaseController {

    protected ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

    protected Optional<XUser> getXUser(XUserService xUserService, OAuth2Authentication authentication) {
        return xUserService.findByVariableId((String) authentication.getUserAuthentication().getPrincipal());
    }

    protected Errors setAuthenticationErrors(Errors errors) {
        errors.rejectValue("principal", "NotFound", "XUser not found by principal");
        errors.reject("principal", "XUser not found by principal");
        return errors;
    }

}
