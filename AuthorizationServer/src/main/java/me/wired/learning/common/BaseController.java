package me.wired.learning.common;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

public class BaseController {

    protected ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

}
