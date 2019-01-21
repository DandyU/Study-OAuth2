package me.wired.learning.user;

import me.wired.learning.common.BaseController;
import me.wired.learning.course.Course;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/users", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class XUserController extends BaseController {

    private final XUserService xUserService;
    private final ModelMapper modelMapper;


    private XUserController(XUserService xUserService, ModelMapper modelMapper) {
        this.xUserService = xUserService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid XUserDto userDto, Errors errors) {
        if (errors.hasErrors())
            return badRequest(errors);

        XUser user = modelMapper.map(userDto, XUser.class);
        XUser newUser = xUserService.save(user);
        URI uri = URI.create(linkTo(XUserController.class).slash(newUser.getId()).toString());
        return ResponseEntity.created(uri).body(newUser);
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
