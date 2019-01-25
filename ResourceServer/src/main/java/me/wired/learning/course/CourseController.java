package me.wired.learning.course;

import me.wired.learning.common.BaseController;
import me.wired.learning.user.XUser;
import me.wired.learning.user.XUserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/courses", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class CourseController extends BaseController {

    private final CourseValidator courseValidator;
    private final CourseService courseService;
    private final ModelMapper modelMapper;
    private final XUserService xUserService;

    public CourseController(CourseValidator courseValidator,
                            CourseServiceImpl courseService,
                            ModelMapper modelMapper, XUserService xUserService) {
        this.courseValidator = courseValidator;
        this.courseService = courseService;
        this.modelMapper = modelMapper;
        this.xUserService = xUserService;
    }

    @PostMapping
    public ResponseEntity createCourse(@RequestBody @Valid CourseDto courseDto, Errors errors, @AuthenticationPrincipal OAuth2Authentication authentication) {
        if (errors.hasErrors())
            return badRequest(errors);

        courseValidator.validate(courseDto, errors);
        if (errors.hasErrors())
            return badRequest(errors);

        Course course = modelMapper.map(courseDto, Course.class);
        course.update();
        Optional<XUser> optionalXUser = getXUser(xUserService, authentication);
        if (!optionalXUser.isPresent())
            return badRequest(setAuthenticationErrors(errors));

        course.setUser(optionalXUser.get());
        Course newCourse = courseService.save(course);
        CourseResource courseResource = new CourseResource(newCourse);
        courseResource.add(new Link("/static/docs/index.html#resources-course-create").withRel("profile"));
        return ResponseEntity.created(URI.create(courseResource.getLink("self").getHref())).body(courseResource);
    }

    @GetMapping("/{id}")
    public ResponseEntity readCourse(@PathVariable String id) {
        Optional<Course> optionalCourse = courseService.findById(id);
        if (!optionalCourse.isPresent())
            return ResponseEntity.notFound().build();

        Course course = optionalCourse.get();
        CourseResource courseResource = new CourseResource(course);
        courseResource.add(new Link("/static/docs/index.html#resources-course-read").withRel("profile"));
        return ResponseEntity.ok().body(courseResource);
    }

    @GetMapping
    public ResponseEntity readCourses(Pageable pageable, PagedResourcesAssembler<Course> assembler) {
        Page<Course> page = courseService.findAll(pageable);
        PagedResources<Resource<Course>> resource = assembler.toResource(page, e -> new CourseResource(e));
        resource.add(linkTo(CourseController.class).withRel("create-course"));
        resource.add(new Link("/static/docs/index.html#resources-courses-read").withRel("profile"));
        return ResponseEntity.ok().body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCourse(@PathVariable String id, @RequestBody @Valid CourseDto courseDto, Errors errors, @AuthenticationPrincipal OAuth2Authentication authentication) {
        if (errors.hasErrors())
            return badRequest(errors);

        courseValidator.validate(courseDto, errors);
        if (errors.hasErrors())
            return badRequest(errors);

        Optional<Course> optionalCourse = courseService.findById(id);
        if (!optionalCourse.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Optional<XUser> optionalXUser = getXUser(xUserService, authentication);
        if (!optionalXUser.isPresent())
            return badRequest(setAuthenticationErrors(errors));

        XUser xUser = optionalXUser.get();
        Course oldCourse = optionalCourse.get();
        if (!xUser.isAdmin() && !oldCourse.getUser().getVariableId().equals(xUser.getVariableId()))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        modelMapper.map(courseDto, oldCourse);
        oldCourse.update();
        Course newCourse = courseService.save(oldCourse);
        CourseResource courseResource = new CourseResource(newCourse);
        courseResource.add(new Link("/static/docs/index.html#resources-course-update").withRel("profile"));
        return ResponseEntity.ok().body(courseResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCourse(@PathVariable String id, @AuthenticationPrincipal OAuth2Authentication authentication) {
        Optional<Course> optionalCourse = courseService.findById(id);
        if (!optionalCourse.isPresent())
            return ResponseEntity.notFound().build();

        Optional<XUser> optionalXUser = getXUser(xUserService, authentication);
        if (!optionalXUser.isPresent())
            return ResponseEntity.badRequest().body("XUser not found by principal");

        XUser xUser = optionalXUser.get();
        Course course = optionalCourse.get();
        if (!xUser.isAdmin() && !course.getUser().getVariableId().equals(xUser.getVariableId()))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping()
    public ResponseEntity deleteCourses(@AuthenticationPrincipal OAuth2Authentication authentication) {
        Optional<XUser> optionalXUser = getXUser(xUserService, authentication);
        if (!optionalXUser.isPresent())
            return ResponseEntity.badRequest().body("XUser not found by principal");

        XUser xUser = optionalXUser.get();
        if (!xUser.isAdmin())
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        courseService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}
