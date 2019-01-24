package me.wired.learning.course;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class CourseResource extends Resource<Course> {

    public CourseResource(Course course, Link... links) {
        super(course, links);
        add(linkTo(CourseController.class).slash(course.getId()).withSelfRel());
        add(linkTo(CourseController.class).slash(course.getId()).withRel("update-course"));
        add(linkTo(CourseController.class).slash(course.getId()).withRel("delete-course"));
    }

}
