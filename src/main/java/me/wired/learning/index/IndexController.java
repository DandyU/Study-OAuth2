package me.wired.learning.index;

import me.wired.learning.course.CourseController;
import me.wired.learning.user.XUserController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/api")
public class IndexController {

    @GetMapping()
    public ResourceSupport index() {
        ResourceSupport resourceSupport = new ResourceSupport();
        resourceSupport.add(linkTo(XUserController.class).withRel("users"));
        resourceSupport.add(linkTo(CourseController.class).withRel("courses"));
        return resourceSupport;
    }

}
