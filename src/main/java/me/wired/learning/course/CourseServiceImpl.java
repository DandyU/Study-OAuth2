package me.wired.learning.course;

import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        super();
        this.courseRepository = courseRepository;
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

}