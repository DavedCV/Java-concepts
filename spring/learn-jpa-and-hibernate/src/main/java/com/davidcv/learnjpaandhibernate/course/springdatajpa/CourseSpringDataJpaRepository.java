package com.davidcv.learnjpaandhibernate.course.springdatajpa;

import com.davidcv.learnjpaandhibernate.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseSpringDataJpaRepository extends JpaRepository<Course, Long> {
}
