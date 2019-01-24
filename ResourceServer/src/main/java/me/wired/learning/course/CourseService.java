package me.wired.learning.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Course save(Course course);

    Optional<Course> findById(String id);

    List<Course> findAll();

    Page<Course> findAll(Pageable pageable);

    void deleteById(String id);

    void deleteAll();

}
