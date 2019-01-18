package me.wired.learning.user;

import me.wired.learning.common.BaseController;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/users", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class XUserController extends BaseController {

    private XUserRepository xUserRepository;

    private XUserController(XUserRepository xUserRepository) {
        this.xUserRepository = xUserRepository;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody XUser xUser, Errors errors) {
        if (errors.hasErrors())
            return badRequest(errors);

        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity readUser(@PathVariable String id) {
        return null;
    }

    @GetMapping
    public ResponseEntity readUsers() {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable String id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        return null;
    }

    @DeleteMapping()
    public ResponseEntity deleteUsers() {
        return null;
    }

}
