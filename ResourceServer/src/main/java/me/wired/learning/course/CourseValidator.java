package me.wired.learning.course;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class CourseValidator {

    public void validate(CourseDto courseDto, Errors errors) {
        LocalDateTime startEnrollmentDateTime = courseDto.getStartEnrollmentDateTime();
        LocalDateTime endEnrollmentDateTime = courseDto.getEndEnrollmentDateTime();
        LocalDateTime startCourseDateTime = courseDto.getStartCourseDateTime();
        LocalDateTime endCourseDateTime = courseDto.getEndCourseDateTime();

        if (courseDto.getDefaultPrice() < courseDto.getSellingPrice()) {
            errors.rejectValue("defaultPrice", "WrongValue", "defaultPrice is less than sellingPrice");
            errors.rejectValue("sellingPrice", "WrongValue", "sellingPrice is more than defaultPrice");
            errors.reject("WrongPrices", "Value of prices wrong");
        }

        if (endEnrollmentDateTime.isBefore(startEnrollmentDateTime) ||
                endEnrollmentDateTime.isBefore(startCourseDateTime) ||
                endEnrollmentDateTime.isBefore(endCourseDateTime)) {
            errors.rejectValue("endEnrollmentDateTime", "WrongValue", "Check endEnrollmentDateTime");
            errors.reject("WrongEndEnrollmentDateTime", "Value of endEnrollmentDateTime wrong");
        }

        if (startCourseDateTime.isAfter(endCourseDateTime) ||
                startCourseDateTime.isAfter(endEnrollmentDateTime) ||
                startCourseDateTime.isBefore(startEnrollmentDateTime)) {
            errors.rejectValue("startCourseDateTime", "WrongValue", "Check startCourseDateTime");
            errors.reject("WrongStartCourseDateTime", "Value of startCourseDateTime wrong");
        }

        if (endCourseDateTime.isAfter(endEnrollmentDateTime) ||
                endCourseDateTime.isBefore(startCourseDateTime) ||
                endCourseDateTime.isBefore(startEnrollmentDateTime)) {
            errors.rejectValue("endCourseDateTime", "WrongValue", "Check endCourseDateTime");
            errors.reject("WrongEndCourseDateTime", "Value of endCourseDateTime wrong");
        }
    }

}
