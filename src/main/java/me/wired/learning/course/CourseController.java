package me.wired.learning.course;

import me.wired.learning.common.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/courses")
public class CourseController extends BaseController {

    private CourseRepository courseRepository;

    private CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @PostMapping
    public ResponseEntity createCourse(@RequestBody CourseDto courseDto, Errors errors) {
        if (errors.hasErrors())
            return badRequest(errors);
        return null;
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
