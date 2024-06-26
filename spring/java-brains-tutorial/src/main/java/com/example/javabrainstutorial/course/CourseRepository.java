package com.example.javabrainstutorial.course;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, String> {

    public List<Course> getCourseByTopic_Id(String topicId);
}
