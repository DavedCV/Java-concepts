package com.davidcv.learnjpaandhibernate.course;

import com.davidcv.learnjpaandhibernate.course.jpa.CourseJpaRepository;
import com.davidcv.learnjpaandhibernate.course.springdatajpa.CourseSpringDataJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CourseCommandLineRunner implements CommandLineRunner {

    private CourseSpringDataJpaRepository courseSpringDataJpaRepository;

    public CourseCommandLineRunner(CourseSpringDataJpaRepository courseSpringDataJpaRepository) {
        this.courseSpringDataJpaRepository = courseSpringDataJpaRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Test the insertion operation
        courseSpringDataJpaRepository.save(new Course(1, "Learn AWS", "in28minutes"));
        courseSpringDataJpaRepository.save(new Course(2, "Learn React", "in28minutes"));
        courseSpringDataJpaRepository.save(new Course(3, "Learn Java", "in28minutes"));

        // Test the delete operation
        courseSpringDataJpaRepository.deleteById(1L);

        // Test the select operation
        System.out.println(courseSpringDataJpaRepository.findById(2L));
        System.out.println(courseSpringDataJpaRepository.findById(3L));
    }
}
