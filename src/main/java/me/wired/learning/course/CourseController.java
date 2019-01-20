package me.wired.learning.course;

import me.wired.learning.common.BaseController;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/courses", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class CourseController extends BaseController {

    private final CourseValidator courseValidator;
    private final CourseService courseService;
    private final ModelMapper modelMapper;

    public CourseController(CourseValidator courseValidator,
                            CourseServiceImpl courseService,
                             ModelMapper modelMapper) {
        this.courseValidator = courseValidator;
        this.courseService = courseService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity createCourse(@RequestBody @Valid CourseDto courseDto, Errors errors) {
        if (errors.hasErrors())
            return badRequest(errors);

        courseValidator.validate(courseDto, errors);
        if (errors.hasErrors())
            return badRequest(errors);

        Course newCourse = courseService.save(modelMapper.map(courseDto, Course.class));
        ControllerLinkBuilder selfLinkBuilder = linkTo(CourseController.class).slash(newCourse.getId());
        return ResponseEntity.created(selfLinkBuilder.toUri()).body(newCourse);
    }

    @GetMapping("/{id}")
    public ResponseEntity readCourse(@PathVariable String id) {
        return null;
    }

    @GetMapping
    public ResponseEntity readCourses() {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCourse(@PathVariable String id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCourse(@PathVariable String id) {
        return null;
    }

    @DeleteMapping()
    public ResponseEntity deleteCourses() {
        return null;
    }

}
