package com.davidcv.learnjpaandhibernate.course.jdbc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CourseJdbcCommandLineRunner implements CommandLineRunner {

    private CourseJdbcRepository courseJdbcRepository;

    public CourseJdbcCommandLineRunner(CourseJdbcRepository courseJdbcRepository) {
        this.courseJdbcRepository = courseJdbcRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Test the insertion operation
        courseJdbcRepository.insert(new Course(1, "Learn AWS", "in28minutes"));
        courseJdbcRepository.insert(new Course(2, "Learn React", "in28minutes"));
        courseJdbcRepository.insert(new Course(3, "Learn Java", "in28minutes"));

        // Test the delete operation
        courseJdbcRepository.delete(1);

        // Test the select operation
        System.out.println(courseJdbcRepository.select(2));
    }
}
